package com.seaside.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Column(nullable = false)
    private LocalDate fechaEntrega;

    @Column(length = 50, nullable = false)
    private String estado;

    @Column(nullable = false)
    private double total;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();


    @Transient
    private Integer domiciliarioId;

    public Pedido(LocalDate fechaCreacion, LocalDate fechaEntrega, String estado, double total, Cliente cliente) {
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.total = total;
        this.cliente = cliente;
    }
}