package com.seaside.e2e;

import com.seaside.e2e.pages.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Caso 2 — Flujo completo de pedido: cliente, operador y verificación final.
 *
 * Flujo:
 * 1. Cliente registrado inicia sesión
 * 2. Cliente navega a /pedido/nuevo, agrega 2 productos con 2 adicionales c/u,
 * verifica el carrito de compras y confirma el pedido
 * 3. Operador cambia el estado del pedido a EN_PREPARACION;
 * el cliente verifica el cambio en /perfil/pedidos
 * 4. Operador asigna domiciliario, cambia estado a EN_CAMINO y luego ENTREGADO
 * 5. Cliente revisa el pedido completado: verifica productos, adicionales y
 * total
 *
 * Prerequisitos:
 * - Backend corriendo en puerto 8080
 * - Angular corriendo en puerto 4200 (ng serve)
 * - Ejecutar con: mvn test -Pe2e
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Caso2E2ETest extends BaseE2ETest {

    // ── Shared state across ordered tests ─────────────────────────────────
    private static long pedidoId = -1;
    private static long expectedTotal = -1;
    private static String producto1Nombre;
    private static String producto2Nombre;
    private static List<String> item1Adicionales = new ArrayList<>();
    private static List<String> item2Adicionales = new ArrayList<>();

    // ── Page object factories ─────────────────────────────────────────────

    private ClienteLoginPage clienteLoginPage() {
        return new ClienteLoginPage(driver, wait);
    }

    private PedidoNuevoPage pedidoNuevoPage() {
        return new PedidoNuevoPage(driver, wait);
    }

    private CarritoPage carritoPage() {
        return new CarritoPage(driver, wait);
    }

    private OperadorLoginPage operadorLoginPage() {
        return new OperadorLoginPage(driver, wait);
    }

    private OperadorPedidosPage operadorPedidosPage() {
        return new OperadorPedidosPage(driver, wait);
    }

    private MisPedidosPage misPedidosPage() {
        return new MisPedidosPage(driver, wait);
    }

    private PedidoDetalleFullPage pedidoDetallePage() {
        return new PedidoDetalleFullPage(driver, wait);
    }

    // ── Auth helpers ──────────────────────────────────────────────────────

    private void loginAsCliente() {
        navigateTo("/login");
        ClienteLoginPage lp = clienteLoginPage();
        lp.waitForLoad();
        lp.loginAs(TestData.CLIENTE_CORREO, TestData.CLIENTE_PASSWORD);
        // Login redirects to "/" after a short timeout in the component
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
    }

    private void loginAsOperador() {
        navigateTo("/operador/login");
        OperadorLoginPage olp = operadorLoginPage();
        olp.waitForLoad();
        olp.loginAs(TestData.OPERADOR_USUARIO, TestData.OPERADOR_PASSWORD);
        wait.until(ExpectedConditions.urlContains("/pedidos"));
    }

    // ── Tests ─────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("1. Cliente ya registrado inicia sesión correctamente")
    void clienteLoginCorrecto() {
        navigateTo("/login");
        ClienteLoginPage lp = clienteLoginPage();
        lp.waitForLoad();
        lp.loginAs(TestData.CLIENTE_CORREO, TestData.CLIENTE_PASSWORD);
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
        assertThat(driver.getCurrentUrl()).endsWith("/");
    }

    @Test
    @Order(2)
    @DisplayName("2. Cliente agrega 2 productos con 2 adicionales c/u, verifica carrito y confirma pedido")
    void crearPedidoYVerificarCarrito() {
        loginAsCliente();

        // Clear any leftover carrito state from a previous run
        ((JavascriptExecutor) driver)
                .executeScript("localStorage.removeItem('seaside_carrito_estado');");

        navigateTo("/pedido/nuevo");
        PedidoNuevoPage pnp = pedidoNuevoPage();
        pnp.waitForLoad();

        // Add product 1 and select 2 adicionales
        producto1Nombre = pnp.addProductByIndex(0);
        item1Adicionales = pnp.expandAndSelectAdicionales(0, 2);
        assertThat(item1Adicionales).hasSizeGreaterThanOrEqualTo(2);

        // Add product 2 and select 2 adicionales
        producto2Nombre = pnp.addProductByIndex(1);
        item2Adicionales = pnp.expandAndSelectAdicionales(1, 2);
        assertThat(item2Adicionales).hasSizeGreaterThanOrEqualTo(2);

        // Capture total from paso 2 (computed by Angular's calcularTotal())
        String totalTextPaso2 = pnp.getTotalText();
        expectedTotal = parsePrecio(totalTextPaso2);
        assertThat(expectedTotal).isPositive();

        // ── Verify carrito ────────────────────────────────────────────────
        navigateTo("/carrito");
        CarritoPage cp = carritoPage();
        cp.waitForLoad();
        assertThat(cp.hasItems()).isTrue();
        assertThat(cp.getItemNames()).contains(producto1Nombre, producto2Nombre);
        // Total in carrito must match what pedido-crear computed
        assertThat(parsePrecio(cp.getTotalText())).isEqualTo(expectedTotal);

        // ── Return to pedido/nuevo and advance to paso 3 ──────────────────
        cp.continuarPedido();
        wait.until(ExpectedConditions.urlContains("/pedido/nuevo"));
        pnp.waitForLoad(); // items are restored from localStorage
        pnp.irPaso3();
        pnp.waitForPaso3();

        // Total in paso-3 summary must match
        assertThat(parsePrecio(pnp.getTotalTextPaso3())).isEqualTo(expectedTotal);

        // ── Confirm order ─────────────────────────────────────────────────
        pnp.confirmarPedido();
        pnp.waitForExito();
        pedidoId = pnp.getPedidoId();
        assertThat(pedidoId).isPositive();
    }

    @Test
    @Order(3)
    @DisplayName("3. Operador cambia el estado del pedido; cliente ve el cambio de estado")
    void operadorCambiaEstadoYClienteVerifica() {
        // Setup client tab first so localStorage has the client session
        loginAsCliente();
        String clienteTab = driver.getWindowHandle();

        // Open operator tab
        openNewTab();
        loginAsOperador();

        OperadorPedidosPage opp = operadorPedidosPage();
        opp.waitForLoad();
        opp.abrirModalEstadoParaPedido(pedidoId);
        opp.seleccionarEstado("EN_PREPARACION");
        opp.confirmarCambioEstado();

        // Switch back to client tab and verify the new estado
        switchToTab(clienteTab);
        navigateTo("/perfil/pedidos");
        MisPedidosPage mpp = misPedidosPage();
        mpp.waitForLoad();
        assertThat(mpp.getEstadoPedido(pedidoId)).isEqualToIgnoringCase("EN_PREPARACION");
    }

    @Test
    @Order(4)
    @DisplayName("4. Operador asigna domiciliario y lleva el pedido a EN_CAMINO; cliente verifica; operador completa a ENTREGADO")
    void operadorAsignaYCompletaPedido() {
        loginAsOperador();
        String operadorTab = driver.getWindowHandle();
        OperadorPedidosPage opp = operadorPedidosPage();
        opp.waitForLoad();

        // Assign a domiciliario (required before EN_CAMINO / ENTREGADO)
        opp.abrirModalAsignarParaPedido(pedidoId);
        opp.seleccionarPrimerDomiciliario();
        opp.confirmarAsignacion();

        // Change status: EN_PREPARACION → EN_CAMINO
        opp.abrirModalEstadoParaPedido(pedidoId);
        opp.seleccionarEstado("EN_CAMINO");
        opp.confirmarCambioEstado();

        // ── Cliente verifica EN_CAMINO ─────────────────────────────────────
        openNewTab();
        loginAsCliente();
        navigateTo("/perfil/pedidos");
        MisPedidosPage mpp = misPedidosPage();
        mpp.waitForLoad();
        assertThat(mpp.getEstadoPedido(pedidoId)).isEqualToIgnoringCase("EN_CAMINO");

        // Back to operator tab to complete the order
        switchToTab(operadorTab);

        // Change status: EN_CAMINO → ENTREGADO
        opp.abrirModalEstadoParaPedido(pedidoId);
        opp.seleccionarEstado("ENTREGADO");
        opp.confirmarCambioEstado();

        // Switch to "Ver todos" so ENTREGADO rows (hidden by soloActivos filter) appear
        opp.verTodos();
        // Verify in the operator table that the estado is now ENTREGADO
        assertThat(opp.getEstadoPedido(pedidoId)).isEqualToIgnoringCase("ENTREGADO");
    }

    @Test
    @Order(5)
    @DisplayName("5. Cliente revisa el historial: pedido ENTREGADO con productos, adicionales y total correctos")
    void clienteVerificaPedidoCompleto() {
        loginAsCliente();
        navigateTo("/perfil/pedidos");
        MisPedidosPage mpp = misPedidosPage();
        mpp.waitForLoad();

        // Verify the order is shown as ENTREGADO
        assertThat(mpp.getEstadoPedido(pedidoId)).isEqualToIgnoringCase("ENTREGADO");

        // Click the card to open full detail
        mpp.clickPedido(pedidoId);
        wait.until(ExpectedConditions.urlContains("/pedidos/" + pedidoId));

        PedidoDetalleFullPage dp = pedidoDetallePage();
        dp.waitForLoad();

        // ── Verify products ───────────────────────────────────────────────
        List<String> productosEnDetalle = dp.getProductNames();
        assertThat(productosEnDetalle).contains(producto1Nombre, producto2Nombre);

        // ── Verify adicionales ────────────────────────────────────────────
        List<String> adicionalesEnDetalle = dp.getAdicionalNames();
        assertThat(adicionalesEnDetalle).containsAll(item1Adicionales);
        assertThat(adicionalesEnDetalle).containsAll(item2Adicionales);

        // ── Verify total (computed from UI, not hardcoded) ────────────────
        long totalEnDetalle = parsePrecio(dp.getTotalText());
        assertThat(totalEnDetalle).isEqualTo(expectedTotal);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /**
     * Strips all non-digit characters from a formatted price string and
     * parses the result as a long. Works for any locale's thousand separator.
     *
     * Examples:
     * "$42,000" → 42000
     * "$42.000" → 42000
     * "$1,234,000" → 1234000
     */
    private static long parsePrecio(String text) {
        String digits = text.replaceAll("[^0-9]", "");
        return Long.parseLong(digits);
    }
}
