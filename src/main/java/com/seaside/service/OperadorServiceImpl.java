package com.seaside.service;

import com.seaside.model.Operador;
import com.seaside.repository.OperadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperadorServiceImpl implements OperadorService {

    @Autowired
    private OperadorRepository operadorRepository;

    @Override
    public List<Operador> findAll() {
        return operadorRepository.findAll();
    }

    @Override
    public Optional<Operador> findById(Integer id) {
        return operadorRepository.findById(id);
    }

    @Override
    public Operador save(Operador operador) {
        return operadorRepository.save(operador);
    }

    @Override
    public void delete(Integer id) {
        operadorRepository.deleteById(id);
    }

    /**
     * Delega la búsqueda por credenciales al repositorio, sin filtrar en memoria.
     */
    @Override
    public Optional<Operador> autenticar(String usuario, String contrasena) {
        return operadorRepository.findByUsuarioAndContrasena(usuario, contrasena);
    }
}