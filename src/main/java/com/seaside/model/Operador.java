package com.seaside.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
Entidad que representa a un operador del sistema (se identifican con "usuario")

- contrasena marcada @Transient: la contraseña encriptada vive en UserEntity.
- Relación OneToOne con UserEntity para login unificado.
- El campo "usuario" se usa como username en UserEntity.
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

    /** @Transient: la contraseña encriptada se guarda en UserEntity. */
    @Transient
    private String contrasena;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}