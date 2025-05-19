package api.web.service;

import api.web.entity.Escena;
import api.web.repo.EscenaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EscenaService {

    @Autowired
    private EscenaRepo escenaRepository;

    public List<Escena> findAll() {
        return escenaRepository.findAll();
    }

    public Optional<Escena> findById(int id) {
        return escenaRepository.findById(id);
    }


    public Escena save(Escena escena) {
        return escenaRepository.save(escena);
    }

    public void deleteById(int id) {
        escenaRepository.deleteById(id);
    }

    public Escena update(int id, Escena escenaDetails) {
        return escenaRepository.findById(id).map(escena -> {
            escena.nombre = escenaDetails.nombre;
            escena.min_inicio = escenaDetails.min_inicio;
            escena.min_final = escenaDetails.min_final;
            return escenaRepository.save(escena);
        }).orElseThrow(() -> new RuntimeException("Escena no encontrada con ID: " + id));
    }
}
