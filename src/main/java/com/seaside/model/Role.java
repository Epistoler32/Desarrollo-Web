package com.seaside.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidad que representa un rol en el sistema. Se mapea a la tabla "roles".

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public Role(String nombre) {
        this.nombre = nombre;
    }
}