package api.web.controller;

import api.web.entity.Escena;
import api.web.service.EscenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/escenas")
@CrossOrigin(origins = "http://localhost:3000")
public class EscenaController {

    @Autowired
    private EscenaService escenaService;

    @GetMapping
    public List<Escena> getAll() {
        return escenaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Escena> getById(@PathVariable int id) {
        return escenaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Escena create(@Valid @RequestBody Escena escena) {
        if (escena.color == null) {
            escena.color = "FFFFFF";
        }
        return escenaService.save(escena);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Escena> update(@PathVariable int id, @Valid @RequestBody Escena escena) {
        try {
            if (escena.color == null) {
                escena.color = "FFFFFF";
            }
            return ResponseEntity.ok(escenaService.update(id, escena));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (escenaService.findById(id).isPresent()) {
            escenaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
