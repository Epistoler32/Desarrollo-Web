package com.seaside.dto;

import java.util.List;

public class PedidoRequest {

    private Integer clienteId;
    private List<ItemRequest> items;

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

    public List<ItemRequest> getItems() { return items; }
    public void setItems(List<ItemRequest> items) { this.items = items; }

    // ── Item dentro del pedido ──────────────────────────────
    public static class ItemRequest {

        private Integer productoId;
        private Integer cantidad;
        private List<AdicionalRequest> adicionales;

        public Integer getProductoId() { return productoId; }
        public void setProductoId(Integer productoId) { this.productoId = productoId; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public List<AdicionalRequest> getAdicionales() { return adicionales; }
        public void setAdicionales(List<AdicionalRequest> adicionales) { this.adicionales = adicionales; }
    }

    // ── Adicional dentro de un item ─────────────────────────
    public static class AdicionalRequest {

        private Integer adicionalId;
        private Integer cantidad;

        public Integer getAdicionalId() { return adicionalId; }
        public void setAdicionalId(Integer adicionalId) { this.adicionalId = adicionalId; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}