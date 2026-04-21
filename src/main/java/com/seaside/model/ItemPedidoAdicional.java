package com.seaside.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ItemPedidoAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private double subtotal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "item_pedido_id", nullable = false)
    private ItemPedido itemPedido;

    @ManyToOne
    @JoinColumn(name = "adicional_id", nullable = false)
    private Adicionales adicional;

    public ItemPedidoAdicional(Integer cantidad, double subtotal,
                                ItemPedido itemPedido, Adicionales adicional) {
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.itemPedido = itemPedido;
        this.adicional = adicional;
    }
}