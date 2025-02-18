package api.web.repo;

import api.web.entity.Proyecto;
import api.web.entity.Storyboard;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Para usar una base de datos real
@ActiveProfiles("test") // Perfil de pruebas
public class StoryboardRepoTest {

    @Autowired
    private StoryboardRepo storyboardRepo;

    @Autowired
    private ProyectoRepo proyectoRepo;

    @Autowired
    private UsuarioRepo usuarioRepo; // Repositorio para Usuario

    @Test
    @Rollback
    void testGuardarStoryboard() {
        // Crear y guardar un Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Test");
        usuario.setCorreo("usuario@test.com");
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar un Proyecto asociado al Usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción del Proyecto Test");
        proyecto.setUsuario(usuarioGuardado);
        Proyecto proyectoGuardado = proyectoRepo.save(proyecto);

        // Crear y guardar un Storyboard asociado al Proyecto
        Storyboard storyboard = new Storyboard();
        storyboard.setDescripcion("Storyboard Test");
        storyboard.setProyecto(proyectoGuardado); // Relación con Proyecto
        storyboard.setImagen(new byte[]{1, 2, 3}); // Simulación de datos de imagen

        Storyboard storyboardGuardado = storyboardRepo.save(storyboard);

        // Verificar que se haya guardado correctamente
        assertNotNull(storyboardGuardado.getId_storyboard());
        assertEquals("Storyboard Test", storyboardGuardado.getDescripcion());
        assertNotNull(storyboardGuardado.getProyecto());
        assertEquals("Proyecto Test", storyboardGuardado.getProyecto().getNombre());
    }

    @Test
    void testBuscarStoryboardPorId() {
        // Crear y guardar un Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Test");
        usuario.setCorreo("usuario@test.com");
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar un Proyecto asociado al Usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción del Proyecto Test");
        proyecto.setUsuario(usuarioGuardado);
        Proyecto proyectoGuardado = proyectoRepo.save(proyecto);

        // Crear y guardar un Storyboard asociado al Proyecto
        Storyboard storyboard = new Storyboard();
        storyboard.setDescripcion("Storyboard Test");
        storyboard.setProyecto(proyectoGuardado); // Relación con Proyecto
        storyboard.setImagen(new byte[]{1, 2, 3}); // Simulación de datos de imagen

        Storyboard storyboardGuardado = storyboardRepo.save(storyboard);

        // Buscar el Storyboard por su ID
        Optional<Storyboard> storyboardEncontrado = storyboardRepo.findById(storyboardGuardado.getId_storyboard());

        // Verificar los datos encontrados
        assertTrue(storyboardEncontrado.isPresent());
        assertEquals("Storyboard Test", storyboardEncontrado.get().getDescripcion());
        assertNotNull(storyboardEncontrado.get().getProyecto());
        assertEquals("Proyecto Test", storyboardEncontrado.get().getProyecto().getNombre());
    }

    @Test
    @Rollback
    void testEliminarStoryboard() {
        // Crear y guardar un Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Test");
        usuario.setCorreo("usuario@test.com");
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar un Proyecto asociado al Usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción del Proyecto Test");
        proyecto.setUsuario(usuarioGuardado);
        Proyecto proyectoGuardado = proyectoRepo.save(proyecto);

        // Crear y guardar un Storyboard asociado al Proyecto
        Storyboard storyboard = new Storyboard();
        storyboard.setDescripcion("Storyboard Test");
        storyboard.setProyecto(proyectoGuardado); // Relación con Proyecto
        storyboard.setImagen(new byte[]{1, 2, 3}); // Simulación de datos de imagen

        Storyboard storyboardGuardado = storyboardRepo.save(storyboard);

        // Eliminar el Storyboard
        storyboardRepo.deleteById(storyboardGuardado.getId_storyboard());

        // Verificar que el Storyboard ha sido eliminado
        Optional<Storyboard> storyboardEliminado = storyboardRepo.findById(storyboardGuardado.getId_storyboard());
        assertFalse(storyboardEliminado.isPresent());
    }
}