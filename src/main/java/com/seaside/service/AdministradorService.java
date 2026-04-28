package com.seaside.service;

import com.seaside.model.Administrador;
import java.util.Optional;

/**
 * Contrato de servicio para la autenticación de administradores.
 * Delega la verificación de credenciales al repositorio de administradores.
 */
public interface AdministradorService {
    /** Autentica un administrador por correo y contraseña. */
    Optional<Administrador> autenticar(String correo, String contrasena);
}
