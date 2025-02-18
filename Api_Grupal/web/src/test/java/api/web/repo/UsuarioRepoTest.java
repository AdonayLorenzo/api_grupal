package api.web.repo;

import api.web.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.springframework.test.context.ActiveProfiles;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
public class UsuarioRepoTest {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Test
    public void testGuardarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setCorreo("juan@example.com");
        usuario.setContrasenna("password123");

        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        assertNotNull(usuarioGuardado.getId_usuario());
        assertEquals("Juan", usuarioGuardado.getNombre());
    }

    @Test
    public void testBuscarPorCorreo() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Ana");
        usuario.setApellido("Gómez");
        usuario.setCorreo("ana@example.com");
        usuario.setContrasenna("password123");

        usuarioRepo.save(usuario);

        Optional<Usuario> encontrado = usuarioRepo.findBycorreo("ana@example.com");

        assertTrue(encontrado.isPresent());
        assertEquals("Ana", encontrado.get().getNombre());
    }

    @Test
    public void testExisteCorreo() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Carlos");
        usuario.setApellido("Lopez");
        usuario.setCorreo("carlos@example.com");
        usuario.setContrasenna("password123");

        usuarioRepo.save(usuario);

        assertTrue(usuarioRepo.existsBycorreo("carlos@example.com"));
        assertFalse(usuarioRepo.existsBycorreo("noexiste@example.com"));
    }
}
