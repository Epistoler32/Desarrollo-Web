package com.seaside.service;

import com.seaside.model.Domiciliario;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestión de domiciliarios.
 * Define operaciones de consulta, persistencia y cambio de estado
 * (disponibilidad/activo).
 */
public interface DomiciliarioService {
    /** Devuelve todos los domiciliarios registrados. */
    List<Domiciliario> findAll();

    /** Devuelve solo los domiciliarios marcados como disponibles. */
    List<Domiciliario> findDisponibles();

    /** Busca un domiciliario por su id. */
    Optional<Domiciliario> findById(Integer id);

    /** Guarda o actualiza un domiciliario. */
    Domiciliario save(Domiciliario domiciliario);

    /** Cambia la disponibilidad de un domiciliario (libre o asignado). */
    void setDisponibilidad(Integer id, boolean disponible);

    /** Activa o desactiva un domiciliario (trabaja ese día o no). */
    void setActivo(Integer id, boolean activo);

    /** Elimina un domiciliario por su id. */
    void delete(Integer id);
}