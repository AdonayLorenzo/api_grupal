package api.web.controller;


import api.web.entity.Storyboard;
import api.web.service.StoryboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/storyboard")
@Validated // Habilitar validaciones a nivel de clase
@CrossOrigin(origins = "http://localhost:+6")
public class StoryboardController {

    private final StoryboardService storyboardService;

    @Autowired
    public StoryboardController(StoryboardService storyboardService) {
        this.storyboardService = storyboardService;
    }

    // Obtener todos los Storyboard
    @GetMapping
    public ResponseEntity<List<Storyboard>> obtenerTodos() {
        List<Storyboard> storyboard = storyboardService.obtenerTodos();
        return ResponseEntity.ok(storyboard);
    }

    // Crear un nuevo storyboard con validación de los datos entrantes
    @PostMapping
    public ResponseEntity<Object> crearLocalizacion(@Valid @RequestBody Storyboard storyboard, BindingResult bindingResult) {
        // Verifica si hubo errores de validación
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }

        try {
            // Guardar el Storyboard en la base de datos
            Storyboard storyboardGuardado = storyboardService.guardarStoryboard(storyboard);
            return ResponseEntity.status(HttpStatus.CREATED).body(storyboardGuardado);
        } catch (Exception e) {
            // Imprimir el error detallado en la consola
            System.out.println("Error al crear el Storyboard: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }

    // Obtener un Storyboard por ID
    @GetMapping("/{id}")
    public ResponseEntity<Storyboard> obtenerPorId(@PathVariable Long id) {
        Optional<Storyboard> localizacion = storyboardService.obtenerPorId(id);
        return localizacion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // 404 si no existe
    }

    // Eliminar un Storyboard por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLocalizacion(@PathVariable Long id) {
        Optional<Storyboard> storyboard = storyboardService.obtenerPorId(id);
        if (storyboard.isPresent()) {
            storyboardService.eliminarStoryboard(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 si no existe
    }

    // Actualizar un usuario por ID
    @PutMapping("/{id}")
    public ResponseEntity<Storyboard> actualizarStoryboard(@PathVariable Long id,
                                                               @Valid @RequestBody Storyboard storyboard,
                                                               BindingResult bindingResult) {
        // Verifica si hubo errores de validación
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null); // Responde con un 400 Bad Request si hay errores
        }

        // Buscar el Storyboard por ID
        Optional<Storyboard> storyboardExistente = storyboardService.obtenerPorId(id);
        if (storyboardExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no existe
        }

        // Actualizar los campos del usuario
        Storyboard storyboardActualizado = storyboardExistente.get();
        storyboardActualizado.setDescripcion(storyboard.getDescripcion());
        storyboardActualizado.setImagen(storyboard.getImagen());

        // Guardar los cambios en la base de datos
        try {
            Storyboard storyboardGuardado = storyboardService.guardarStoryboard(storyboardActualizado);
            return ResponseEntity.status(HttpStatus.OK).body(storyboardGuardado); // 200 OK con la storyboard actualizado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Manejo de excepciones
        }
    }
}