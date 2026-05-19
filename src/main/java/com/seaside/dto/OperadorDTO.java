package com.seaside.dto;

import com.seaside.model.Operador;

// Expone solo nombre y usuario: nunca la contraseña.
public class OperadorDTO {

    public Integer id;
    public String nombre;
    public String usuario;

    public static OperadorDTO from(Operador o) {
        OperadorDTO dto = new OperadorDTO();
        dto.id      = o.getId();
        dto.nombre  = o.getNombre();
        dto.usuario = o.getUsuario();
        return dto;
    }
}