package com.seaside.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidad que representa a un domiciliario.


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Domiciliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellido;

    @Column(length = 70, nullable = false, unique = true)
    private String correo;

    //la contraseña encriptada se guarda en UserEntity
    @Transient
    private String contrasena;

    @Column(length = 20, nullable = false)
    private String telefono;

    @Column(length = 100, nullable = false)
    private String direccion;

    @Column(nullable = false)
    private boolean activo;

    @Column(length = 20, nullable = false)
    private String cedula;

    @Column(nullable = false)
    private boolean disponible;

    @JsonIgnoreProperties({ "fechaCreacion", "fechaEntrega", "estado", "total", "cliente", "domiciliarioId", "items" })
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public Domiciliario(String nombre, String apellido, String correo,
            String contrasena, String telefono, String direccion,
            boolean activo, String cedula, boolean disponible) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        this.activo = activo;
        this.cedula = cedula;
        this.disponible = disponible;
    }
}