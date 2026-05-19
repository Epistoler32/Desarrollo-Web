package com.seaside.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seaside.dto.PedidoRequest;
import com.seaside.model.*;
import com.seaside.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;

//Pruebas del controlador PedidoController usando Mocks de MockMvc
// @WebMvcTest carga únicamente la capa web controlador
// @MockBean simula el PedidoService para que el controlador tenga una dependencia funcional sin tocar la base de datos.
// MockMvc simula las peticiones HTTP

//paso de esto
/*
@WebMvcTest(controllers = PedidoController.class)
@ActiveProfiles("test")
@Import(PedidoController.class)
*/

// a esto
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PedidoControllerMockTest {

    // MockMvc simula las peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    // Mock del servicio (dependencia del controlador)
    @MockBean
    private PedidoService pedidoService;

    // ObjectMapper convierte objetos Java ↔ JSON
    @Autowired
    private ObjectMapper objectMapper;

    // Datos de prueba reutilizables
    private Pedido pedidoFalso;
    private Cliente clienteFalso;

    //Construye los objetos de prueba antes de cada @Test
    @BeforeEach
    void setUp() {
        clienteFalso = new Cliente(
                "Ana", "Prueba", "ana@test.com",
                "pass", "3001234567", "Calle 1");
        clienteFalso.setId(1);

        pedidoFalso = new Pedido(
                LocalDate.now(), LocalDate.now().plusDays(1),
                "Pendiente", 42000.0, clienteFalso);
        pedidoFalso.setId(1);
    }

    // TEST 1: GET /api/pedidos - Obtener todos los pedidos
    // Verifica: status 200, tipo JSON, cantidad correcta

    // GET sin parámetros debe retornar la lista completa de pedidos con status 200 OK
    @Test
    void getAll_sinParametros_retornaListaConStatus200() throws Exception {
        // Arrange - el servicio mock devuelve dos pedidos falsos
        Pedido pedido2 = new Pedido(LocalDate.now(), LocalDate.now(), "Entregado", 35000.0, clienteFalso);
        pedido2.setId(2);
        when(pedidoService.findAll()).thenReturn(List.of(pedidoFalso, pedido2));

        // Act
        ResultActions result = mockMvc.perform(
                get("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.length()").value(2))
              .andExpect(jsonPath("$[0].estado").value("Pendiente"))
              .andExpect(jsonPath("$[1].estado").value("Entregado"));

        verify(pedidoService, times(1)).findAll();
    }

    // TEST 2: GET /api/pedidos?activos=true - Solo pedidos activos

    // GET con parámetro activos=true debe invocar findActivos()y retornar solo los pedidos no finalizados.
    @Test
    void getAll_conParametroActivos_retornaSoloPedidosActivos() throws Exception {
        // Arrange
        when(pedidoService.findActivos()).thenReturn(List.of(pedidoFalso));

        // Act
        ResultActions result = mockMvc.perform(
                get("/api/pedidos")
                        .param("activos", "true")
                        .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.length()").value(1))
              .andExpect(jsonPath("$[0].estado").value("Pendiente"));

        verify(pedidoService, times(1)).findActivos();
        verify(pedidoService, never()).findAll();
    }

    // TEST 3: GET /api/pedidos/{id} - Buscar pedido por ID
    
    // GET con ID existente debe retornar el pedido y status 200
    @Test
    void getById_idExistente_retornaPedidoConStatus200() throws Exception {
        // Arrange
        when(pedidoService.findById(1)).thenReturn(Optional.of(pedidoFalso));

        // Act
        ResultActions result = mockMvc.perform(
                get("/api/pedidos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(1))
              .andExpect(jsonPath("$.estado").value("Pendiente"))
              .andExpect(jsonPath("$.total").value(42000.0));
    }

    // GET con ID inexistente debe retornar status 404
    @Test
    void getById_idInexistente_retornaStatus404() throws Exception {
        // Arrange
        when(pedidoService.findById(999)).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(
                get("/api/pedidos/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound())
              .andExpect(jsonPath("$.error").exists());
    }

    //  TEST 4: POST /api/pedidos - Crear un nuevo pedido

    // POST con un PedidoRequest válido debe crear el pedidoy retornar status 201 CREATED.

    @Test
    void crearPedido_requestValido_retornaStatus201ConPedido() throws Exception {
        // Arrange
        PedidoRequest.ItemRequest itemReq = new PedidoRequest.ItemRequest();
        itemReq.setProductoId(1);
        itemReq.setCantidad(1);
        itemReq.setAdicionales(List.of());

        PedidoRequest request = new PedidoRequest();
        request.setClienteId(1);
        request.setItems(List.of(itemReq));

        when(pedidoService.crearPedido(any(PedidoRequest.class))).thenReturn(pedidoFalso);

        // Act
        ResultActions result = mockMvc.perform(
                post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isCreated())
              .andExpect(jsonPath("$.id").value(1))
              .andExpect(jsonPath("$.estado").value("Pendiente"));

        verify(pedidoService, times(1)).crearPedido(any(PedidoRequest.class));
    }

    // POST con datos inválidos (clienteId inexistente) debe retornar 400.
    @Test
    void crearPedido_clienteInexistente_retornaStatus400() throws Exception {
        // Arrange
        PedidoRequest request = new PedidoRequest();
        request.setClienteId(9999);
        request.setItems(List.of());

        when(pedidoService.crearPedido(any(PedidoRequest.class)))
                .thenThrow(new IllegalArgumentException("Cliente no encontrado: 9999"));

        // Act
        ResultActions result = mockMvc.perform(
                post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.error").value("Cliente no encontrado: 9999"));
    }

    // TEST 5: PATCH /api/pedidos/{id}/estado - Actualizar estado

    // PATCH con ID válido y nuevo estado debe retornar 200 OK con mensaje.
    @Test
    void actualizarEstado_idValido_retornaStatus200ConMensaje() throws Exception {
        // Arrange
        when(pedidoService.findById(1)).thenReturn(Optional.of(pedidoFalso));
        doNothing().when(pedidoService).actualizarEstado(eq(1), eq("EN_PREPARACION"));

        Map<String, String> body = Map.of("estado", "EN_PREPARACION");

        // Act
        ResultActions result = mockMvc.perform(
                patch("/api/pedidos/{id}/estado", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.message").value("Estado actualizado"));

        verify(pedidoService, times(1)).actualizarEstado(1, "EN_PREPARACION");
    }

    // PATCH sin campo 'estado' en el body debe retornar 400 BAD REQUEST.
    @Test
    void actualizarEstado_sinCampoEstado_retornaStatus400() throws Exception {
        // Arrange - body vacío, sin el campo "estado"
        Map<String, String> bodyVacio = Map.of();

        // Act
        ResultActions result = mockMvc.perform(
                patch("/api/pedidos/{id}/estado", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bodyVacio)));

        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.error").exists());
    }

    // TEST 6: DELETE /api/pedidos/{id} - Eliminar pedido

    // DELETE con ID existente debe retornar 204 NO CONTENT.
    @Test
    void delete_idExistente_retornaStatus204() throws Exception {
        // Arrange
        when(pedidoService.findById(1)).thenReturn(Optional.of(pedidoFalso));
        doNothing().when(pedidoService).delete(1);

        // Act
        ResultActions result = mockMvc.perform(
                delete("/api/pedidos/{id}", 1));

        // Assert
        result.andExpect(status().isNoContent());

        verify(pedidoService, times(1)).delete(1);
    }

    // DELETE con ID inexistente debe retornar 404 NOT FOUND

    @Test
    void delete_idInexistente_retornaStatus404() throws Exception {
        // Arrange
        when(pedidoService.findById(999)).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(
                delete("/api/pedidos/{id}", 999));

        // Assert
        result.andExpect(status().isNotFound())
              .andExpect(jsonPath("$.error").exists());

        verify(pedidoService, never()).delete(999);
    }

    // TEST-7: GET /api/pedidos?clienteId=X - Pedidos por cliente

    // GET con parámetro clienteId debe retornar solo los pedidos de ese cliente
    @Test
    void getAll_conParametroClienteId_retornaPedidosDelCliente() throws Exception {
        // Arrange
        when(pedidoService.findByClienteId(1)).thenReturn(List.of(pedidoFalso));

        // Act
        ResultActions result = mockMvc.perform(
                get("/api/pedidos")
                        .param("clienteId", "1")
                        .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.length()").value(1))
              .andExpect(jsonPath("$[0].id").value(1));

        verify(pedidoService, times(1)).findByClienteId(1);
        verify(pedidoService, never()).findAll();
    }
}