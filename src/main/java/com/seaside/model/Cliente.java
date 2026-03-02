package com.seaside.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    private Integer id;
    private String nombre;
    private String apellido;
    private String correo;      // único, usado para login
    private String contrasena;
    private String telefono;
    private String direccion;
}