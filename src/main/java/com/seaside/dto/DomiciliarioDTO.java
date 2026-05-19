package com.seaside.dto;

import com.seaside.model.Domiciliario;

// Expone solo datos operacionales, nunca la contraseña ni datos internos de pedido

public class DomiciliarioDTO {

    public Integer id;
    public String nombre;
    public String apellido;
    public String correo;
    public String telefono;
    public String cedula;
    public boolean activo;
    public boolean disponible;

    public static DomiciliarioDTO from(Domiciliario d) {
        DomiciliarioDTO dto = new DomiciliarioDTO();
        dto.id          = d.getId();
        dto.nombre      = d.getNombre();
        dto.apellido    = d.getApellido();
        dto.correo      = d.getCorreo();
        dto.telefono    = d.getTelefono();
        dto.cedula      = d.getCedula();
        dto.activo      = d.isActivo();
        dto.disponible  = d.isDisponible();
        return dto;
    }
}