package com.seaside.dto;

/*
DTO de respuesta para el login exitoso
Conntiene el JWT token que el frontend debe guardar (localStorage) y los datos básicos del usuario autenticado

El frontend enviará este token en cada petición como Authorization: Bearer <token>
*/
public class LoginResponseDTO {

    public String token;
    public String username;
    public String rol;
    public Integer userId;

    public LoginResponseDTO(String token, String username, String rol, Integer userId) {
        this.token    = token;
        this.username = username;
        this.rol      = rol;
        this.userId   = userId;
    }
}