package com.seaside.dto;

import com.seaside.model.Adicionales;
import com.seaside.model.ItemPedido;
import com.seaside.model.ItemPedidoAdicional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO que aplana ItemPedido + sus ItemPedidoAdicional en un solo objeto
 * para evitar referencias circulares y exponer solo lo necesario al frontend.
 */
public class ItemPedidoResponse {

    public Integer id;
    public Integer cantidad;
    public double subtotal;
    public ProductoInfo producto;
    public List<AdicionalInfo> adicionales;

    //  Inner DTOs 

    public static class ProductoInfo {
        public Integer id;
        public String nombre;
        public double precio;
        public String imageUrl;

        public static ProductoInfo from(com.seaside.model.Producto p) {
            ProductoInfo info = new ProductoInfo();
            info.id       = p.getId();
            info.nombre   = p.getNombre();
            info.precio   = p.getPrecio();
            info.imageUrl = p.getImageUrl();
            return info;
        }
    }

    public static class AdicionalInfo {
        public Integer id;
        public String nombre;
        public double precio;
        public Integer cantidad;
        public double subtotal;

        public static AdicionalInfo from(ItemPedidoAdicional ipa) {
            AdicionalInfo info = new AdicionalInfo();
            Adicionales a = ipa.getAdicional();
            info.id       = a.getId();
            info.nombre   = a.getNombre();
            info.precio   = a.getPrecio();
            info.cantidad = ipa.getCantidad();
            info.subtotal = ipa.getSubtotal();
            return info;
        }
    }

    //  Factory 

    public static ItemPedidoResponse from(ItemPedido item) {
        ItemPedidoResponse r = new ItemPedidoResponse();
        r.id       = item.getId();
        r.cantidad = item.getCantidad();
        r.subtotal = item.getSubtotal();
        r.producto = ProductoInfo.from(item.getProducto());
        r.adicionales = item.getAdicionales()
                .stream()
                .map(AdicionalInfo::from)
                .collect(Collectors.toList());
        return r;
    }
}