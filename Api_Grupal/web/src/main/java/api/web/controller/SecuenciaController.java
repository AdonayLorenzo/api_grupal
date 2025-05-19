package api.web.controller;

import api.web.entity.Escena;
import api.web.entity.Localizacion;
import api.web.entity.Proyecto;
import api.web.entity.Secuencia;
import api.web.service.SecuenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secuencias")
@CrossOrigin(origins = "http://localhost:3000")
public class SecuenciaController {

    @Autowired
    private SecuenciaService secuenciaService;

    @GetMapping
    public List<Secuencia> getAllSecuencias() {
        return secuenciaService.getAllSecuencias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Secuencia> getSecuenciaById(@PathVariable int id) {
        Optional<Secuencia> secuencia = secuenciaService.getSecuenciaById(id);
        return secuencia.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // 404 si no existe
    }

    @GetMapping("/{id}/escenas")
    public ResponseEntity<List<Escena>> getEscenasBySecuenciaId(@PathVariable int id) {
        Optional<Secuencia> optionalSecuencia = secuenciaService.getSecuenciaById(id);

        if (optionalSecuencia.isPresent()) {
            List<Escena> escenas = optionalSecuencia.get().getEscenas();
            return ResponseEntity.ok(escenas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object>  createSecuencia(@Valid @RequestBody Secuencia secuencia, BindingResult bindingResult) {
        // Verifica si hubo errores de validación

        try {
            // Guardar la Localizacion en la base de datos
            Secuencia secuenciaGuardado = secuenciaService.saveSecuencia(secuencia);
            return ResponseEntity.status(HttpStatus.CREATED).body(secuenciaGuardado);
        } catch (Exception e) {
            // Imprimir el error detallado en la consola
            System.out.println("Error al crear el proyecto: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Secuencia> updateSecuencia(@PathVariable int id, @Valid @RequestBody Secuencia secuencia) {
        Secuencia updated = secuenciaService.updateSecuencia(id, secuencia);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSecuencia(@PathVariable int id) {
        if (secuenciaService.getSecuenciaById(id).isPresent()) {
            secuenciaService.deleteSecuencia(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
