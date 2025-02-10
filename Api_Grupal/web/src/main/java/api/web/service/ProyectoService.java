package api.web.service;

import api.web.entity.Proyecto;
import api.web.repo.ProyectoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProyectoService {

    private final ProyectoRepo proyectoRepository;

    @Autowired
    public ProyectoService(ProyectoRepo proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    // Método para obtener todos los proyectos
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll(); // Usamos el repositorio directamente
    }

    // Método para guardar un nuevo proyecto
    public Proyecto guardarProyecto(Proyecto proyecto) {
        // El usuario se guarda automáticamente usando el repositorio
        return proyectoRepository.save(proyecto);
    }

    // Método para obtener un proyecto por ID
    public Optional<Proyecto> obtenerPorId(Long id) {
        return proyectoRepository.findById(id); // Usamos el repositorio directamente
    }

    // Método para eliminar un proyecto por ID
    public void eliminarProyecto(Long id) {
        proyectoRepository.deleteById(id); // Usamos el repositorio directamente
    }

}