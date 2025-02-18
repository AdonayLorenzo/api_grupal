package api.web.service;

import api.web.entity.Proyecto;
import api.web.repo.ProyectoRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectoServiceTest {

    @Mock
    private ProyectoRepo proyectoRepo;

    @InjectMocks
    private ProyectoService proyectoService;

    @Test
    void testObtenerTodos() {
        // Configurar el mock
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setNombre("Proyecto 1");
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto 2");
        when(proyectoRepo.findAll()).thenReturn(Arrays.asList(proyecto1, proyecto2));

        // Llamar al método del servicio
        List<Proyecto> proyectos = proyectoService.obtenerTodos();

        // Verificar el resultado
        assertEquals(2, proyectos.size());
        verify(proyectoRepo, times(1)).findAll();
    }

    @Test
    void testGuardarProyecto() {
        // Configurar el mock
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto de prueba");
        when(proyectoRepo.save(proyecto)).thenReturn(proyecto);

        // Llamar al método del servicio
        Proyecto proyectoGuardado = proyectoService.guardarProyecto(proyecto);

        // Verificar el resultado
        assertNotNull(proyectoGuardado);
        assertEquals("Proyecto de prueba", proyectoGuardado.getNombre());
        verify(proyectoRepo, times(1)).save(proyecto);
    }

    @Test
    void testObtenerPorId() {
        // Configurar el mock
        Proyecto proyecto = new Proyecto();
        proyecto.setId_proyecto(1L);
        proyecto.setNombre("Proyecto de prueba");
        when(proyectoRepo.findById(1L)).thenReturn(Optional.of(proyecto));

        // Llamar al método del servicio
        Optional<Proyecto> proyectoEncontrado = proyectoService.obtenerPorId(1L);

        // Verificar el resultado
        assertTrue(proyectoEncontrado.isPresent());
        assertEquals("Proyecto de prueba", proyectoEncontrado.get().getNombre());
        verify(proyectoRepo, times(1)).findById(1L);
    }

    @Test
    void testEliminarProyecto() {
        // Llamar al método del servicio
        proyectoService.eliminarProyecto(1L);

        // Verificar que se llamó al método del repositorio
        verify(proyectoRepo, times(1)).deleteById(1L);
    }
}