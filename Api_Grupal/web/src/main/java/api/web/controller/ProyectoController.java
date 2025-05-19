package api.web.controller;
import api.web.entity.Localizacion;
import api.web.entity.Proyecto;
import api.web.entity.Secuencia;
import api.web.entity.Storyboard;
import api.web.service.ProyectoService;
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
@RequestMapping("/api/proyectos")
@Validated // Habilitar validaciones a nivel de clase
@CrossOrigin(origins = "http://localhost:3000")
public class ProyectoController {

    private final ProyectoService proyectoService;

    @Autowired
    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Proyecto>> obtenerTodos() {
        List<Proyecto> usuarios = proyectoService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}/localizaciones")
    public List<Localizacion> getLocalizaciones(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.obtenerPorId(id);
        if (proyecto != null) {
            return proyecto.get().getLocalizaciones(); // Retorna la lista de localizaciones
        }
        return null; // O maneja el caso de que el proyecto no exista
    }

    @GetMapping("/{id}/storyboards")
    public List<Storyboard> getStoryboards(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.obtenerPorId(id);
        if (proyecto != null) {
            return proyecto.get().getStoryboards(); // Retorna la lista de localizaciones
        }
        return null; // O maneja el caso de que el proyecto no exista
    }

    @GetMapping("/{id}/secuencias")
    public List<Secuencia> getSecuencias(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.obtenerPorId(id);
        if (proyecto != null) {
            return proyecto.get().getSecuencias(); // Retorna la lista de localizaciones
        }
        return null; // O maneja el caso de que el proyecto no exista
    }

    // Crear un nuevo proyecto con validación de los datos entrantes
    @PostMapping
    public ResponseEntity<Object> crearProyecto(@Valid @RequestBody Proyecto proyecto, BindingResult bindingResult) {
        // Verifica si hubo errores de validación
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }

        try {
            // Guardar el proyecto en la base de datos
            Proyecto proyectoGuardado = proyectoService.guardarProyecto(proyecto);
            return ResponseEntity.status(HttpStatus.CREATED).body(proyectoGuardado);
        } catch (Exception e) {
            // Imprimir el error detallado en la consola
            System.out.println("Error al crear el proyecto: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }

    // Obtener un proyecto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerPorId(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.obtenerPorId(id);
        return proyecto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // 404 si no existe
    }

    // Eliminar un proyecto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.obtenerPorId(id);
        if (proyecto.isPresent()) {
            proyectoService.eliminarProyecto(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 si no existe
    }

    // Actualizar un proyecto por ID
    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> actualizarProyecto(@PathVariable Long id,
                                                     @Valid @RequestBody Proyecto proyecto,
                                                     BindingResult bindingResult) {
        // Verifica si hubo errores de validación
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null); // Responde con un 400 Bad Request si hay errores
        }

        // Buscar el proyecto por ID
        Optional<Proyecto> proyectoExistente = proyectoService.obtenerPorId(id);
        if (proyectoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no existe
        }

        // Actualizar los campos del proyecto
        Proyecto proyectoActualizado = proyectoExistente.get();
        proyectoActualizado.setNombre(proyecto.getNombre());
        proyectoActualizado.setDescripcion(proyecto.getDescripcion());
        proyectoActualizado.setImagen(proyecto.getImagen());

        // Guardar los cambios en la base de datos
        try {
            Proyecto proyectoGuardado = proyectoService.guardarProyecto(proyectoActualizado);
            return ResponseEntity.status(HttpStatus.OK).body(proyectoGuardado); // 200 OK con el usuario actualizado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Manejo de excepciones
        }
    }
}