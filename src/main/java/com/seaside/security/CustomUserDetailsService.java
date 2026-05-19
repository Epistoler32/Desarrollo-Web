package com.seaside.security;

import com.seaside.model.Role;
import com.seaside.model.UserEntity;
import com.seaside.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
Servicio que conecta nuestra UserEntity con Spring Security

Spring Security necesita un UserDetailsService para poder:
- Buscar el usuario en la base de datos por username
- Comparar contraseñas con el PasswordEncoder
- Cargar los roles (authorities) del usuario

El método loadUserByUsername realiza el mapeo de UserEntity - UserDetails
que es la interfaz estándar que entiende Spring Security internamente
*/

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Carga un usuario desde la BD y lo transforma en UserDetails
    // Spring Security llama a este método automáticamente durante la autenticación
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userDB = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new User(
                userDB.getUsername(),
                userDB.getPassword(),
                mapRolesToAuthorities(userDB.getRoles())
        );
    }

    // Convierte nuestra lista de Role (entidad JPA) a la colección de GrantedAuthority que necesita Spring Security internamente.
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .collect(Collectors.toList());
    }
}