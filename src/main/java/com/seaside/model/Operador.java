package com.seaside.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa a un operador del sistema.
 * Los operadores gestionan pedidos desde el portal de operador.
 * Se autentican con usuario y contraseña (no correo).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false, unique = true)
    private String usuario;

    @Column(length = 50, nullable = false)
    private String contrasena;
}
