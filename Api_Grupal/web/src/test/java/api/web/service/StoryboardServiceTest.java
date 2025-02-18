package api.web.service;

import api.web.entity.Proyecto;
import api.web.entity.Storyboard;
import api.web.repo.StoryboardRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StoryboardServiceTest {

    @Mock
    private StoryboardRepo storyboardRepo; // Mock del repositorio

    @InjectMocks
    private StoryboardService storyboardService; // Inyección del mock al servicio

    private Storyboard storyboard;
    private Proyecto proyecto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear un Proyecto simulado
        proyecto = new Proyecto();
        proyecto.setId_proyecto(1L);
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción del Proyecto Test");

        // Crear un Storyboard simulado
        storyboard = new Storyboard();
        storyboard.setDescripcion("Storyboard Test");
        storyboard.setProyecto(proyecto);
        storyboard.setImagen(new byte[]{1, 2, 3}); // Simulación de datos de imagen
    }

    @Test
    void testObtenerTodos() {
        // Simular datos retornados por el repositorio
        when(storyboardRepo.findAll()).thenReturn(Arrays.asList(storyboard));

        // Llamar al método del servicio
        List<Storyboard> storyboards = storyboardService.obtenerTodos();

        // Verificar resultados
        assertNotNull(storyboards);
        assertEquals(1, storyboards.size());
        assertEquals("Storyboard Test", storyboards.get(0).getDescripcion());

        // Verificar que el método del repositorio fue llamado
        verify(storyboardRepo, times(1)).findAll();
    }

    @Test
    void testGuardarStoryboard() {
        // Simular el comportamiento del repositorio al guardar
        when(storyboardRepo.save(any(Storyboard.class))).thenReturn(storyboard);

        // Llamar al método del servicio
        Storyboard storyboardGuardado = storyboardService.guardarStoryboard(storyboard);

        // Verificar resultados
        assertNotNull(storyboardGuardado);
        assertEquals("Storyboard Test", storyboardGuardado.getDescripcion());
        assertNotNull(storyboardGuardado.getProyecto());
        assertEquals("Proyecto Test", storyboardGuardado.getProyecto().getNombre());

        // Verificar que el método del repositorio fue llamado
        verify(storyboardRepo, times(1)).save(any(Storyboard.class));
    }

    @Test
    void testObtenerPorId_Existente() {
        // Simular el comportamiento del repositorio al buscar por ID
        when(storyboardRepo.findById(storyboard.getId_storyboard())).thenReturn(Optional.of(storyboard));

        // Llamar al método del servicio
        Optional<Storyboard> storyboardEncontrado = storyboardService.obtenerPorId(storyboard.getId_storyboard());

        // Verificar resultados
        assertTrue(storyboardEncontrado.isPresent());
        assertEquals("Storyboard Test", storyboardEncontrado.get().getDescripcion());
        assertNotNull(storyboardEncontrado.get().getProyecto());
        assertEquals("Proyecto Test", storyboardEncontrado.get().getProyecto().getNombre());

        // Verificar que el método del repositorio fue llamado
        verify(storyboardRepo, times(1)).findById(storyboard.getId_storyboard());
    }

    @Test
    void testObtenerPorId_NoExistente() {
        // Simular que el repositorio no encuentra el ID
        when(storyboardRepo.findById(999L)).thenReturn(Optional.empty());

        // Llamar al método del servicio
        Optional<Storyboard> storyboardNoEncontrado = storyboardService.obtenerPorId(999L);

        // Verificar resultados
        assertFalse(storyboardNoEncontrado.isPresent());

        // Verificar que el método del repositorio fue llamado
        verify(storyboardRepo, times(1)).findById(999L);
    }

    @Test
    void testEliminarStoryboard() {
        // Simular el comportamiento del repositorio al eliminar
        doNothing().when(storyboardRepo).deleteById(storyboard.getId_storyboard());

        // Llamar al método del servicio
        storyboardService.eliminarStoryboard(storyboard.getId_storyboard());

        // Verificar que el método del repositorio fue llamado
        verify(storyboardRepo, times(1)).deleteById(storyboard.getId_storyboard());
    }
}