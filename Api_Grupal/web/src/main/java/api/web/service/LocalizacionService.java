package api.web.service;

import api.web.entity.Localizacion;
import api.web.entity.Proyecto;
import api.web.repo.LocalizacionRepo;
import api.web.repo.ProyectoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LocalizacionService {

    private final LocalizacionRepo localizacionRepository;

    @Autowired
    public LocalizacionService(LocalizacionRepo localizacionRepository) {
        this.localizacionRepository = localizacionRepository;
    }

    // Método para obtener todos los proyectos
    public List<Localizacion> obtenerTodos() {
        return localizacionRepository.findAll(); // Usamos el repositorio directamente
    }

    // Método para guardar un nuevo proyecto
    public Localizacion guardarLocalizacion(Localizacion localizacion) {
        // El usuario se guarda automáticamente usando el repositorio
        return localizacionRepository.save(localizacion);
    }

    // Método para obtener un proyecto por ID
    public Optional<Localizacion> obtenerPorId(Long id) {
        return localizacionRepository.findById(id); // Usamos el repositorio directamente
    }

    // Método para eliminar un proyecto por ID
    public void eliminarLocalizacion(Long id) {
        localizacionRepository.deleteById(id); // Usamos el repositorio directamente
    }

}