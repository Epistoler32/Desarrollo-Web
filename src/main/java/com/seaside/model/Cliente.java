package com.seaside.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
Entidad que representa a un cliente registrado en la plataforma.
Cada cliente tiene un carrito de compras y una UserEntity asociada para el manejo de autenticación y roles.

- Relación OneToOne con UserEntity.
- La contraseña se marca @Transient para no duplicarla en esta tabla
*/
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

    // la contraseña real encriptada se guarda en UserEntity

    @Transient
    private String contrasena;

    @Column(length = 20, nullable = false, unique = true)
    private String telefono;

    @Column(length = 70, nullable = false)
    private String direccion;

    @JsonIgnoreProperties({ "carritoProductos", "cliente" })
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    /*
    Relación 1:1 con la tabla unificada de usuarios.
    Se usa CascadeType.ALL para que al guardar el cliente se guarde también el UserEntity asociado.
    */
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // Constructor sin id ni carrito
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