package api.web.controller;
import api.web.entity.Localizacion;

import api.web.service.LocalizacionService;
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
@RequestMapping("/api/localizacion")
@Validated // Habilitar validaciones a nivel de clase
@CrossOrigin(origins = "http://localhost:+6")
public class LocalizacionController {

    private final LocalizacionService localizacionService;

    @Autowired
    public LocalizacionController(LocalizacionService localizacionService) {
        this.localizacionService = localizacionService;
    }

    // Obtener todos los Localizacion
    @GetMapping
    public ResponseEntity<List<Localizacion>> obtenerTodos() {
        List<Localizacion> localizaciones = localizacionService.obtenerTodos();
        return ResponseEntity.ok(localizaciones);
    }

    // Crear un nuevo localizacion con validación de los datos entrantes
    @PostMapping
    public ResponseEntity<Object> crearLocalizacion(@Valid @RequestBody Localizacion localizacion, BindingResult bindingResult) {
        // Verifica si hubo errores de validación
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }

        try {
            // Guardar la Localizacion en la base de datos
            Localizacion localizacionGuardado = localizacionService.guardarLocalizacion(localizacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(localizacionGuardado);
        } catch (Exception e) {
            // Imprimir el error detallado en la consola
            System.out.println("Error al crear el proyecto: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }

    // Obtener una Localizacion por ID
    @GetMapping("/{id}")
    public ResponseEntity<Localizacion> obtenerPorId(@PathVariable Long id) {
        Optional<Localizacion> localizacion = localizacionService.obtenerPorId(id);
        return localizacion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // 404 si no existe
    }

    // Eliminar una Localizacion por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLocalizacion(@PathVariable Long id) {
        Optional<Localizacion> localizacion = localizacionService.obtenerPorId(id);
        if (localizacion.isPresent()) {
            localizacionService.eliminarLocalizacion(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 si no existe
    }

    // Actualizar una Localizacion por ID
    @PutMapping("/{id}")
    public ResponseEntity<Localizacion> actualizarLocalizacion(@PathVariable Long id,
                                                       @Valid @RequestBody Localizacion localizacion,
                                                       BindingResult bindingResult) {
        // Verifica si hubo errores de validación
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null); // Responde con un 400 Bad Request si hay errores
        }

        // Buscar la Localizacion por ID
        Optional<Localizacion> localizacionExistente = localizacionService.obtenerPorId(id);
        if (localizacionExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no existe
        }

        // Actualizar los campos de la Localizacion
        Localizacion localizacionActualizado = localizacionExistente.get();
        localizacionActualizado.setNombre(localizacion.getNombre());
        localizacionActualizado.setDescripcion(localizacion.getDescripcion());
        localizacionActualizado.setImagen(localizacion.getImagen());
        localizacionActualizado.setLink_map(localizacion.getLink_map());

        // Guardar los cambios en la base de datos
        try {
            Localizacion localizacionGuardado = localizacionService.guardarLocalizacion(localizacionActualizado);
            return ResponseEntity.status(HttpStatus.OK).body(localizacionGuardado); // 200 OK con la localizacion actualizado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Manejo de excepciones
        }
    }
}