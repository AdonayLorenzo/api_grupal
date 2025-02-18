package api.web.service;

import api.web.entity.Localizacion;
import api.web.entity.Proyecto;
import api.web.entity.Usuario;
import api.web.repo.LocalizacionRepo;
import api.web.repo.ProyectoRepo;
import api.web.repo.UsuarioRepo;
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

public class LocalizacionServiceTest {

    @Mock
    private LocalizacionRepo localizacionRepo;

    @Mock
    private ProyectoRepo proyectoRepo;

    @Mock
    private UsuarioRepo usuarioRepo;

    @InjectMocks
    private LocalizacionService localizacionService;

    private Proyecto proyecto;
    private Localizacion localizacion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simular datos para Proyecto
        Usuario usuario = new Usuario();
        usuario.setId_usuario(1L);
        usuario.setNombre("Usuario Test");
        usuario.setCorreo("usuario@test.com");

        proyecto = new Proyecto();
        proyecto.setId_proyecto(1L);
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción del Proyecto Test");
        proyecto.setUsuario(usuario);

        // Simular datos para Localizacion
        localizacion = new Localizacion();
        localizacion.setId_localizacion(1L);
        localizacion.setNombre("Localización Test");
        localizacion.setDescripcion("Descripción de la Localización");
        localizacion.setLink_map("https://maps.google.com/localizacion");
        localizacion.setProyecto(proyecto);
    }

    @Test
    void testObtenerTodos() {
        // Simular comportamiento del repositorio
        when(localizacionRepo.findAll()).thenReturn(Arrays.asList(localizacion));

        // Llamar al método del servicio
        List<Localizacion> localizaciones = localizacionService.obtenerTodos();

        // Verificar resultados
        assertNotNull(localizaciones);
        assertEquals(1, localizaciones.size());
        assertEquals("Localización Test", localizaciones.get(0).getNombre());

        // Verificar que el método del repositorio fue llamado una vez
        verify(localizacionRepo, times(1)).findAll();
    }

    @Test
    void testGuardarLocalizacion() {
        // Simular comportamiento del repositorio
        when(localizacionRepo.save(any(Localizacion.class))).thenReturn(localizacion);

        // Llamar al método del servicio
        Localizacion localizacionGuardada = localizacionService.guardarLocalizacion(localizacion);

        // Verificar resultados
        assertNotNull(localizacionGuardada);
        assertEquals("Localización Test", localizacionGuardada.getNombre());
        assertEquals("https://maps.google.com/localizacion", localizacionGuardada.getLink_map());
        assertNotNull(localizacionGuardada.getProyecto());
        assertEquals("Proyecto Test", localizacionGuardada.getProyecto().getNombre());

        // Verificar que el método del repositorio fue llamado una vez
        verify(localizacionRepo, times(1)).save(any(Localizacion.class));
    }

    @Test
    void testObtenerPorId() {
        // Simular comportamiento del repositorio
        when(localizacionRepo.findById(1L)).thenReturn(Optional.of(localizacion));

        // Llamar al método del servicio
        Optional<Localizacion> localizacionEncontrada = localizacionService.obtenerPorId(1L);

        // Verificar resultados
        assertTrue(localizacionEncontrada.isPresent());
        assertEquals("Localización Test", localizacionEncontrada.get().getNombre());
        assertNotNull(localizacionEncontrada.get().getProyecto());
        assertEquals("Proyecto Test", localizacionEncontrada.get().getProyecto().getNombre());

        // Verificar que el método del repositorio fue llamado una vez
        verify(localizacionRepo, times(1)).findById(1L);
    }

    @Test
    void testEliminarLocalizacion() {
        // Simular comportamiento del repositorio
        doNothing().when(localizacionRepo).deleteById(1L);

        // Llamar al método del servicio
        localizacionService.eliminarLocalizacion(1L);

        // Verificar que el método del repositorio fue llamado una vez
        verify(localizacionRepo, times(1)).deleteById(1L);
    }
}