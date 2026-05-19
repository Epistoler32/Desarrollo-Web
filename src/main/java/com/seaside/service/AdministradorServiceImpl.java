package com.seaside.service;

import com.seaside.model.Administrador;
import com.seaside.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
Implementación de AdministradorService.

CAMBIO SPRINT SEGURIDAD:
La autenticación por correo+contraseña ahora pasa por AuthenticationManager en el controlador (que usa Spring Security + BCrypt).
Este método auxiliar sigue disponible para obtener los datos del admin después de que AuthenticationManager ya validó las credenciales.
*/

@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
    Método auxiliar: busca el administrador por correo.
    La verificación de contraseña real la hace Spring Security a través de
    CustomUserDetailsService + BCryptPasswordEncoder.
    
    @deprecated Para autenticación use AuthenticationManager en el controlador.
    Este método se mantiene solo para obtener datos del admin post-login.
    */
    @Override
    public Optional<Administrador> autenticar(String correo, String contrasena) {
        // Busca por correo - la contraseña ya fue validada por Spring Security
        return administradorRepository.findByCorreo(correo);
    }
}