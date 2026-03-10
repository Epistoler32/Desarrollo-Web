package com.seaside.model;

<<<<<<< HEAD
import jakarta.annotation.Generated;
=======
>>>>>>> 35fdb6b46a04d1708de73276a297ac0f613d8c0a
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(length = 50, nullable = false)
    private String apellido;
    @Column(length = 70, nullable = false, unique = true)
    private String correo;
    @Column(length = 50, nullable = false)
    private String contrasena;
    @Column(length = 20, nullable = false, unique = true)
    private String telefono;
    @Column(length = 70, nullable = false)
    private String direccion;

    public Cliente(String nombre, String apellido, String correo, String contrasena, String telefono, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
    }
}