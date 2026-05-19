package com.seaside.dto;

import com.seaside.model.Administrador;

// Expone solo los datos necesarios: nunca la contraseña

public class AdministradorDTO {

    public Integer id;
    public String nombre;
    public String apellido;
    public String correo;
    public String telefono;
    public String direccion;

    public static AdministradorDTO from(Administrador a) {
        AdministradorDTO dto = new AdministradorDTO();
        dto.id        = a.getId();
        dto.nombre    = a.getNombre();
        dto.apellido  = a.getApellido();
        dto.correo    = a.getCorreo();
        dto.telefono  = a.getTelefono();
        dto.direccion = a.getDireccion();
        return dto;
    }
}