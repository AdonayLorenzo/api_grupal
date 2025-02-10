package api.web.service;


import api.web.entity.Storyboard;
import api.web.repo.StoryboardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StoryboardService {

    private final StoryboardRepo storyboardRepository;

    @Autowired
    public StoryboardService(StoryboardRepo storyboardRepository) {
        this.storyboardRepository = storyboardRepository;
    }

    // Método para obtener todos los proyectos
    public List<Storyboard> obtenerTodos() {
        return storyboardRepository.findAll(); // Usamos el repositorio directamente
    }

    // Método para guardar un nuevo proyecto
    public Storyboard guardarStoryboard(Storyboard storyboard) {
        // El usuario se guarda automáticamente usando el repositorio
        return storyboardRepository.save(storyboard);
    }


    // Método para obtener un proyecto por ID
    public Optional<Storyboard> obtenerPorId(Long id) {
        return storyboardRepository.findById(id); // Usamos el repositorio directamente
    }

    // Método para eliminar un proyecto por ID
    public void eliminarStoryboard(Long id) {
        storyboardRepository.deleteById(id); // Usamos el repositorio directamente
    }

}