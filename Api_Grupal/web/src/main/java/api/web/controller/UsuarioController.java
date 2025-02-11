package api.web.controller;

import api.web.DTO.LoginRequest;
import api.web.DTO.LoginResponse;
import api.web.JwtUtil;
import api.web.entity.Usuario;
import api.web.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Validated // Habilitar validaciones a nivel de clase
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> checkExists(
            @RequestParam String nickname,
            @RequestParam String email) {
        Map<String, Boolean> response = usuarioService.checkIfNicknameOrEmailExists(nickname, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Buscar usuario por correo
        Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail());

        if (usuario == null || !usuario.getContrasenna().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        // Generar token JWT
        String token = jwtUtil.generateToken(usuario);

        LoginResponse response = new LoginResponse(token);
        return ResponseEntity.ok(response);
    }

    // Crear un nuevo usuario con validación de los datos entrantes
    @PostMapping
    public ResponseEntity<Object> crearUsuario(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
        // Verifica si hubo errores de validación
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }

        try {
            // Guardar el usuario en la base de datos
            Usuario usuarioGuardado = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
        } catch (Exception e) {
            // Imprimir el error detallado en la consola
            System.out.println("Error al crear el usuario: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }

    // Obtener un usuario por ID
    @GetMapping("/{token}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable String token) {
        Optional<Usuario> usuario = usuarioService.obtenerPorId(jwtUtil.extractUserId(token));
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // 404 si no existe
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
        if (usuario.isPresent()) {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 si no existe
    }

    // Actualizar un usuario por ID
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id,
                                                     @Valid @RequestBody Usuario usuario,
                                                     BindingResult bindingResult) {
        // Verifica si hubo errores de validación
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null); // Responde con un 400 Bad Request si hay errores
        }

        // Buscar el usuario por ID
        Optional<Usuario> usuarioExistente = usuarioService.obtenerPorId(id);
        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no existe
        }

        // Actualizar los campos del usuario
        Usuario usuarioActualizado = usuarioExistente.get();
        usuarioActualizado.setNombre(usuario.getNombre());
        usuarioActualizado.setCorreo(usuario.getCorreo());
        usuarioActualizado.setDatos(usuario.getDatos());
        usuarioActualizado.setContrasenna(usuario.getContrasenna());
        usuarioActualizado.setFecha_N(usuario.getFecha_N());

        // Guardar los cambios en la base de datos
        try {
            Usuario usuarioGuardado = usuarioService.guardarUsuario(usuarioActualizado);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioGuardado); // 200 OK con el usuario actualizado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Manejo de excepciones
        }
    }
}