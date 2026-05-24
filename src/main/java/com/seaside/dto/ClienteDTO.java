package com.seaside.dto;

import com.seaside.model.Cliente;

/*
Se usa en las respuestas de la API para NO exponer la contraseña al frontend
Solo transfiere los datos necesarios para la vista del cliente

PATRÓN DTO: separar la entidad de persistencia del objeto que se envía al cliente
*/

public class ClienteDTO {

    public String nombre;
    public String apellido;
    public String correo;
    public String telefono;
    public String direccion;
    public Integer carritoId;

    // Factory method convierte un Cliente en ClienteDTO
    public static ClienteDTO from(Cliente c) {
        ClienteDTO dto = new ClienteDTO();
        dto.nombre    = c.getNombre();
        dto.apellido  = c.getApellido();
        dto.correo    = c.getCorreo();
        dto.telefono  = c.getTelefono();
        dto.direccion = c.getDireccion();
        dto.carritoId = (c.getCarrito() != null) ? c.getCarrito().getId() : null;
        return dto;
    }
}