package com.seaside.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seaside.dto.ClienteDTO;
import com.seaside.dto.LoginResponseDTO;
import com.seaside.model.Carrito;
import com.seaside.model.Cliente;
import com.seaside.model.Role;
import com.seaside.model.UserEntity;
import com.seaside.repository.RoleRepository;
import com.seaside.repository.UserRepository;
import com.seaside.security.JwtTokenGenerator;
import com.seaside.service.ClienteService;

/*
Controlador REST para autenticación de clientes
+agregado
- signup: crea UserEntity con rol CLIENTE, retorna ClienteDTO (sin contraseña)
- login:  usa AuthenticationManager + JWT, retorna LoginResponseDTO con token
*/
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    /*
    Registra un nuevo cliente.
    1. Verifica que el correo no exista ya en users ni en cliente
    2. Crea UserEntity con contraseña encriptada y rol CLIENTE
    3. Asocia el UserEntity al Cliente
    4. Retorna ClienteDTO (sin contraseña)
    */
    @PostMapping("/signup")
    public ResponseEntity<?> procesarSignup(@RequestBody Cliente cliente) {
        // Verificar que el correo no esté en uso
        if (userRepository.existsByUsername(cliente.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe una cuenta con ese correo."));
        }
        if (clienteService.existeCorreo(cliente.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe una cuenta con ese correo."));
        }

        // Crear y guardar UserEntity con contraseña encriptada
        UserEntity userEntity = new UserEntity(
                cliente.getCorreo(),
                passwordEncoder.encode(cliente.getContrasena())
        );
        Role rolCliente = roleRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
        userEntity.setRoles(List.of(rolCliente));
        UserEntity savedUser = userRepository.save(userEntity);

        // Asociar el UserEntity al Cliente y guardar
        if (cliente.getCarrito() == null) {
            cliente.setCarrito(new Carrito(LocalDateTime.now()));
        }
        cliente.setUser(savedUser);
        Cliente guardado = clienteService.registrar(cliente);

        // Retornar DTO seguro (sin contraseña)
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteDTO.from(guardado));
    }

    //Autentica un cliente y retorna un JWT token
    @PostMapping("/login")
    public ResponseEntity<?> procesarLogin(@RequestBody Map<String, String> credentials) {
        String correo    = credentials.get("correo");
        String contrasena = credentials.get("contrasena");

        if (correo == null || contrasena == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Correo y contraseña son obligatorios"));
        }

        try {
            // AuthenticationManager valida las credenciales contra la BD (usa CustomUserDetailsService)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correo, contrasena)
            );

            // Guardar autenticación en el SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar JWT token
            String token = jwtTokenGenerator.generateToken(authentication);

            return ResponseEntity.ok(new LoginResponseDTO(token, correo, "CLIENTE", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Correo o contraseña incorrectos."));
        }
    }

    /** Devuelve los datos del cliente autenticado (requiere JWT válido). */
    @GetMapping("/me")
    public ResponseEntity<?> obtenerPerfil() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No autenticado."));
        }
        return clienteService.buscarPorCorreo(auth.getName())
                .map(c -> ResponseEntity.ok(ClienteDTO.from(c)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /** Elimina la cuenta del cliente autenticado. */
    @DeleteMapping("/me")
    public ResponseEntity<?> eliminarCuenta() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Cliente cliente = clienteService.buscarPorCorreo(auth.getName()).orElse(null);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        clienteService.eliminar(cliente.getId());
        return ResponseEntity.noContent().build();
    }

    /** Actualiza nombre, apellido, teléfono y dirección del cliente autenticado. */
    @PutMapping("/me")
    public ResponseEntity<?> actualizarPerfil(@RequestBody Map<String, String> datos) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No autenticado."));
        }
        Cliente cliente = clienteService.buscarPorCorreo(auth.getName())
                .orElse(null);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Cliente no encontrado."));
        }
        if (datos.containsKey("nombre"))    cliente.setNombre(datos.get("nombre"));
        if (datos.containsKey("apellido"))  cliente.setApellido(datos.get("apellido"));
        if (datos.containsKey("telefono"))  cliente.setTelefono(datos.get("telefono"));
        if (datos.containsKey("direccion")) cliente.setDireccion(datos.get("direccion"));
        Cliente actualizado = clienteService.actualizar(cliente);
        return ResponseEntity.ok(ClienteDTO.from(actualizado));
    }
}