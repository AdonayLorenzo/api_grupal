package api.web.repo;

import api.web.entity.Proyecto;
import api.web.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Usa MySQL directamente
@ActiveProfiles("test") // Usa el perfil configuración de prueba
public class ProyectoRepoTest {

    @Autowired
    private ProyectoRepo proyectoRepo;

    @Autowired
    private UsuarioRepo usuarioRepo; // Asegúrate de tener acceso al repositorio de Usuario

    @Test
    @Rollback
    void testGuardarProyecto() {
        // Crear y guardar un usuario (proyecto requiere uno)
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");
        usuario.setCorreo("test@correo.com");
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear un proyecto asociado al usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto de prueba");
        proyecto.setDescripcion("Descripción del proyecto");
        proyecto.setUsuario(usuarioGuardado); // Relacionar usuario

        // Guardar el proyecto
        Proyecto proyectoGuardado = proyectoRepo.save(proyecto);

        // Validar que se guardó correctamente
        assertNotNull(proyectoGuardado.getId_proyecto());
        assertEquals("Proyecto de prueba", proyectoGuardado.getNombre());
        assertNotNull(proyectoGuardado.getUsuario());
        assertEquals(usuarioGuardado.getId_usuario(), proyectoGuardado.getUsuario().getId_usuario());
    }

    @Test
    void testBuscarProyectoPorId() {
        // Crear y guardar un usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");
        usuario.setCorreo("test@correo.com");
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar un proyecto relacionado al usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto de prueba");
        proyecto.setDescripcion("Descripción");
        proyecto.setUsuario(usuarioGuardado);
        Proyecto proyectoGuardado = proyectoRepo.save(proyecto);

        // Buscar el proyecto por ID
        Optional<Proyecto> proyectoEncontrado = proyectoRepo.findById(proyectoGuardado.getId_proyecto());

        // Validar que se encontró correctamente
        assertTrue(proyectoEncontrado.isPresent());
        assertEquals("Proyecto de prueba", proyectoEncontrado.get().getNombre());
        assertEquals(usuarioGuardado.getId_usuario(), proyectoEncontrado.get().getUsuario().getId_usuario());
    }

    @Test
    void testEliminarProyecto() {
        // Crear y guardar un usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario de prueba");
        usuario.setCorreo("test@correo.com");
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar un proyecto relacionado al usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto a eliminar");
        proyecto.setDescripcion("Descripción");
        proyecto.setUsuario(usuarioGuardado);
        Proyecto proyectoGuardado = proyectoRepo.save(proyecto);

        // Eliminar el proyecto
        proyectoRepo.deleteById(proyectoGuardado.getId_proyecto());

        // Verificar que el proyecto ya no existe
        Optional<Proyecto> proyectoEliminado = proyectoRepo.findById(proyectoGuardado.getId_proyecto());
        assertFalse(proyectoEliminado.isPresent());
    }
}