package api.web.controller;

import api.web.entity.Secuencia;
import api.web.service.SecuenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Secuencia createSecuencia(@Valid @RequestBody Secuencia secuencia) {
        return secuenciaService.saveSecuencia(secuencia);
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
