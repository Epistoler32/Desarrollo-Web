package com.seaside.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellido;

    @Column(length = 70, nullable = false, unique = true)
    private String correo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 50, nullable = false)
    private String contrasena;

    @Column(length = 20, nullable = false, unique = true)
    private String telefono;

    @Column(length = 70, nullable = false)
    private String direccion;

    // Relación uno a uno con Carrito
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    public Cliente(String nombre, String apellido, String correo,
                   String contrasena, String telefono, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Constructor completo sin id
    public Cliente(String nombre, String apellido, String correo,
                   String contrasena, String telefono, String direccion,
                   Carrito carrito) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        this.carrito = carrito;
    }

    // Constructor con id explícito y sin carrito
    public Cliente(Integer id, String nombre, String apellido, String correo,
                   String contrasena, String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
    }
}