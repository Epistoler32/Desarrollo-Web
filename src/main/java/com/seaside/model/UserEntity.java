package com.seaside.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/*
Entidad unificada de usuarios para Spring Security
Todos los tipos de usuario se mapean a esta tabla para tener un login unificado y gestión de roles

Se llama UserEntity (no User) para evitar conflicto con la clase User de Spring Security
*/

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Username único: correo para Cliente/Admin/Domiciliario, usuario para Operador */
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /*
    Relación ManyToMany con Role a través de la tabla intermedia user_roles
    Un usuario puede tener múltiples roles
    EAGER para que Spring Security siempre cargue los roles junto con el usuario
    */

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_roles",
        joinColumns    = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
}