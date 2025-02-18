package api.web.repo;

import api.web.entity.Localizacion;
import api.web.entity.Proyecto;
import api.web.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Usa base de datos real
@ActiveProfiles("test") // Perfil de configuración de prueba
public class LocalizacionRepoTest {

    @Autowired
    private LocalizacionRepo localizacionRepo;

    @Autowired
    private ProyectoRepo proyectoRepo;

    @Autowired
    private UsuarioRepo usuarioRepo; // Repositorio para gestionar la relación con Usuario

    @Test
    @Rollback
    void testGuardarLocalizacion() {
        // Crear y guardar un Usuario requerido para el Proyecto
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");
        usuario.setCorreo("test@correo.com");
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar un Proyecto relacionado con el Usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto de prueba");
        proyecto.setDescripcion("Descripción del proyecto de prueba");
        proyecto.setUsuario(usuarioGuardado); // Relacionar Usuario
        Proyecto proyectoGuardado = proyectoRepo.save(proyecto);

        // Crear Localización asociada al Proyecto
        Localizacion localizacion = new Localizacion();
        localizacion.setNombre("Localización Prueba");
        localizacion.setDescripcion("Descripción breve de la localización");
        localizacion.setLink_map("https://maps.google.com/localizacion");
        localizacion.setProyecto(proyectoGuardado); // Relacionar proyecto

        // Guardar la Localización
        Localizacion localizacionGuardada = localizacionRepo.save(localizacion);

        // Verificar que se guardó correctamente
        assertNotNull(localizacionGuardada.getId_localizacion());
        assertEquals("Localización Prueba", localizacionGuardada.getNombre());
        assertNotNull(localizacionGuardada.getProyecto());
        assertEquals(proyectoGuardado.getId_proyecto(), localizacionGuardada.getProyecto().getId_proyecto());
    }

    @Test
    void testBuscarLocalizacionPorId() {
        // Crear y guardar un Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Test");
        usuario.setCorreo("usuario@test.com");
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar un Proyecto relacionado al Usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción Test");
        proyecto.setUsuario(usuarioGuardado);
        Proyecto proyectoGuardado = proyectoRepo.save(proyecto);

        // Crear y guardar una Localización relacionada al Proyecto
        Localizacion localizacion = new Localizacion();
        localizacion.setNombre("Localización Test");
        localizacion.setDescripcion("Detalles de la localización");
        localizacion.setProyecto(proyectoGuardado);
        Localizacion localizacionGuardada = localizacionRepo.save(localizacion);

        // Buscar la Localización por su ID
        Optional<Localizacion> localizacionEncontrada = localizacionRepo.findById(localizacionGuardada.getId_localizacion());

        // Verificar que se encontró correctamente
        assertTrue(localizacionEncontrada.isPresent());
        assertEquals("Localización Test", localizacionEncontrada.get().getNombre());
        assertEquals(proyectoGuardado.getId_proyecto(), localizacionEncontrada.get().getProyecto().getId_proyecto());
    }

    @Test
    void testEliminarLocalizacion() {
        // Crear y guardar un Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Test");
        usuario.setCorreo("usuario@test.com");
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar un Proyecto relacionado al Usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción de prueba");
        proyecto.setUsuario(usuarioGuardado);
        Proyecto proyectoGuardado = proyectoRepo.save(proyecto);

        // Crear y guardar una Localización relacionada al Proyecto
        Localizacion localizacion = new Localizacion();
        localizacion.setNombre("Localización a eliminar");
        localizacion.setDescripcion("Detalles de la localización");
        localizacion.setProyecto(proyectoGuardado);
        Localizacion localizacionGuardada = localizacionRepo.save(localizacion);

        // Eliminar la Localización
        localizacionRepo.deleteById(localizacionGuardada.getId_localizacion());

        // Verificar que la Localización fue eliminada
        Optional<Localizacion> localizacionEliminada = localizacionRepo.findById(localizacionGuardada.getId_localizacion());
        assertFalse(localizacionEliminada.isPresent());
    }
}