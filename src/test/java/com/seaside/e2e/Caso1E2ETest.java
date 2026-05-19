package com.seaside.e2e;

import com.seaside.e2e.pages.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Caso 1 — Registro de un nuevo producto por parte del administrador.
 *
 * Flujo:
 * 1. Landing page → navegar a /login
 * 2. Ir a portal admin desde /login
 * 3. Intentar login con credenciales incorrectas → verificar error
 * 4. Login correcto → llegar al dashboard
 * 5. Dashboard → /productos → crear nuevo producto
 * 6. Asignar 2 adicionales al producto → guardar
 * 7. Abrir nueva pestaña → /menu → detalle del producto → verificar 2
 * adicionales
 * 8. Volver a pestaña admin → agregar un 3.er adicional → guardar
 * 9. Ir a pestaña menú → refrescar → verificar 3 adicionales
 *
 * Prerequisitos:
 * - Backend corriendo en puerto 8080
 * - Angular corriendo en puerto 4200 (ng serve)
 * - Ejecutar con: mvn test -Pe2e
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Caso1E2ETest extends BaseE2ETest {

    // Shared state across ordered test methods (single browser session)
    private String adminTabHandle;
    private String menuTabHandle;
    private String selectedAdicional1;
    private String selectedAdicional2;
    private String selectedAdicional3;

    // ── Page objects ──────────────────────────────────────────────────────

    private LandingPage landingPage() {
        return new LandingPage(driver, wait);
    }

    private LoginPage loginPage() {
        return new LoginPage(driver, wait);
    }

    private AdminLoginPage adminLoginPage() {
        return new AdminLoginPage(driver, wait);
    }

    private AdminDashboardPage dashPage() {
        return new AdminDashboardPage(driver, wait);
    }

    private ProductosTablePage productosPage() {
        return new ProductosTablePage(driver, wait);
    }

    private ProductoCrearPage crearPage() {
        return new ProductoCrearPage(driver, wait);
    }

    private ProductoDetallePage detallePage() {
        return new ProductoDetallePage(driver, wait);
    }

    // ── Tests ─────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("1. Landing page carga correctamente y muestra el hero")
    void landingPageCarga() {
        navigateTo("/");
        LandingPage lp = landingPage();
        lp.waitForLoad();
        assertThat(lp.getHeroTitleText()).containsIgnoringCase("SeaSide");
    }

    @Test
    @Order(2)
    @DisplayName("2. Desde /login se puede llegar al portal de administrador")
    void navegarAAdminLogin() {
        navigateTo("/login");
        LoginPage lp = loginPage();
        lp.waitForLoad();
        lp.clickAdminLogin();
        wait.until(ExpectedConditions.urlContains("/admin/login"));
        assertThat(driver.getCurrentUrl()).contains("/admin/login");
    }

    @Test
    @Order(3)
    @DisplayName("3. Credenciales incorrectas muestran mensaje de error")
    void credencialesInvalidasMuestranError() {
        // Browser is already at /admin/login from test 2
        AdminLoginPage alp = adminLoginPage();
        alp.loginAs(TestData.ADMIN_CORREO, TestData.ADMIN_WRONG_PASS);
        String error = alp.getErrorText();
        assertThat(error).isNotBlank();
    }

    @Test
    @Order(4)
    @DisplayName("4. Login correcto redirige al dashboard de administrador")
    void loginCorrectoVaDashboard() {
        // Browser is still at /admin/login after the failed attempt in test 3
        AdminLoginPage alp = adminLoginPage();
        alp.loginAs(TestData.ADMIN_CORREO, TestData.ADMIN_PASSWORD);
        wait.until(ExpectedConditions.urlContains("/admin/dashboard"));
        assertThat(driver.getCurrentUrl()).contains("/admin/dashboard");
        assertThat(dashPage().getTitleText()).containsIgnoringCase("Portal Administrador");

        adminTabHandle = driver.getWindowHandle();
    }

    @Test
    @Order(5)
    @DisplayName("5. Desde el dashboard navegar a productos y crear un nuevo producto")
    void crearNuevoProducto() {
        // Already authenticated and on /admin/dashboard from test 4
        dashPage().clickGestionMenu();
        wait.until(ExpectedConditions.urlContains("/productos"));

        ProductosTablePage pt = productosPage();
        pt.waitForLoad();
        pt.clickNuevoProducto();
        wait.until(ExpectedConditions.urlContains("/productos/nuevo"));

        ProductoCrearPage cp = crearPage();
        cp.waitForLoad();
        cp.enterNombre(TestData.PRODUCTO_NOMBRE);
        cp.enterPrecio(TestData.PRODUCTO_PRECIO);
        cp.selectCategoria(TestData.PRODUCTO_CATEGORIA);
        cp.enterTiempo(TestData.PRODUCTO_TIEMPO);
        cp.enterDescripcion(TestData.PRODUCTO_DESCRIPCION);
        cp.guardar();

        wait.until(d -> !d.getCurrentUrl().contains("/nuevo") && d.getCurrentUrl().contains("/productos"));
        ProductosTablePage ptAfter = productosPage();
        ptAfter.waitForLoad();
        ptAfter.waitForRows();
        assertThat(ptAfter.isProductInTable(TestData.PRODUCTO_NOMBRE)).isTrue();
    }

    @Test
    @Order(6)
    @DisplayName("6. Asignar 2 adicionales al nuevo producto")
    void asignar2Adicionales() {
        // Already authenticated and on /productos from test 5
        ProductosTablePage pt = productosPage();
        pt.waitForLoad();
        pt.openAdicionalesFor(TestData.PRODUCTO_NOMBRE);
        pt.waitForModal();

        List<String> allNames = pt.getAllAdicionalNames();
        assertThat(allNames).hasSizeGreaterThanOrEqualTo(3);
        selectedAdicional1 = allNames.get(0);
        selectedAdicional2 = allNames.get(1);
        selectedAdicional3 = allNames.get(2); // saved for step 8

        pt.selectAdicionales(selectedAdicional1, selectedAdicional2);
        pt.saveAdicionales();
    }

    @Test
    @Order(7)
    @DisplayName("7. En nueva pestaña, el producto aparece en /menu con 2 adicionales")
    void verificar2AdicionalesEnMenu() {
        // Open a new tab for menu verification; admin tab stays at /productos
        openNewTab();
        menuTabHandle = driver.getWindowHandle();
        navigateTo("/menu");

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                org.openqa.selenium.By.cssSelector("app-menu-card")));
        driver.findElements(org.openqa.selenium.By.cssSelector("figcaption.gallery-caption"))
                .stream()
                .filter(el -> el.getText().equals(TestData.PRODUCTO_NOMBRE))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product card not found in menu: " + TestData.PRODUCTO_NOMBRE))
                .click();

        ProductoDetallePage dp = detallePage();
        dp.waitForLoad();
        List<String> adicionalesEnDetalle = dp.getAdicionalNames();
        assertThat(adicionalesEnDetalle).hasSize(2);
        assertThat(adicionalesEnDetalle).contains(selectedAdicional1, selectedAdicional2);
    }

    @Test
    @Order(8)
    @DisplayName("8. Administrador agrega un 3.er adicional al producto")
    void agregar3erAdicional() {
        // Switch back to the admin tab (still at /productos)
        switchToTab(adminTabHandle);
        ProductosTablePage pt = productosPage();
        pt.waitForLoad();
        pt.openAdicionalesFor(TestData.PRODUCTO_NOMBRE);
        pt.waitForModal();

        pt.selectAdicionales(selectedAdicional1, selectedAdicional2, selectedAdicional3);
        pt.saveAdicionales();
    }

    @Test
    @Order(9)
    @DisplayName("9. En el menú, el producto aparece ahora con 3 adicionales")
    void verificar3AdicionalesEnMenu() {
        // Switch to the menu tab and refresh the product detail page
        switchToTab(menuTabHandle);
        refresh();

        ProductoDetallePage dp = detallePage();
        dp.waitForLoad();
        List<String> adicionalesEnDetalle = dp.getAdicionalNames();
        assertThat(adicionalesEnDetalle).hasSize(3);
        assertThat(adicionalesEnDetalle).contains(selectedAdicional1, selectedAdicional2, selectedAdicional3);
    }
}
