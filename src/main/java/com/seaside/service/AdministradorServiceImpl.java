package com.seaside.service;

import com.seaside.model.Administrador;
import com.seaside.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación de AdministradorService.
 * Busca el administrador por correo y verifica la contraseña en memoria.
 */
@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Override
    public Optional<Administrador> autenticar(String correo, String contrasena) {
        return administradorRepository.findByCorreo(correo)
                .filter(a -> a.getContrasena().equals(contrasena));
    }
}
