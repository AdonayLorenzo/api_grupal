package api.web.service;

import api.web.entity.Usuario;
import api.web.repo.UsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepo usuarioRepo;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId_usuario(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setCorreo("juan@example.com");
        usuario.setContrasenna("password123");
    }

    @Test
    public void testObtenerTodos() {
        Mockito.when(usuarioRepo.findAll()).thenReturn(List.of(usuario));

        List<Usuario> usuarios = usuarioService.obtenerTodos();

        assertFalse(usuarios.isEmpty());
        assertEquals(1, usuarios.size());
        assertEquals("Juan", usuarios.get(0).getNombre());
    }

    @Test
    public void testGuardarUsuario() {
        Mockito.when(usuarioRepo.save(usuario)).thenReturn(usuario);

        Usuario usuarioGuardado = usuarioService.guardarUsuario(usuario);

        assertNotNull(usuarioGuardado);
        assertEquals("Juan", usuarioGuardado.getNombre());
    }

    @Test
    public void testObtenerPorId() {
        Mockito.when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> usuarioEncontrado = usuarioService.obtenerPorId(1L);

        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Juan", usuarioEncontrado.get().getNombre());
    }

    @Test
    public void testEliminarUsuario() {
        Mockito.doNothing().when(usuarioRepo).deleteById(1L);

        usuarioService.eliminarUsuario(1L);

        Mockito.verify(usuarioRepo, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testCheckIfNicknameOrEmailExists() {
        Mockito.when(usuarioRepo.existsByNombre("Juan")).thenReturn(true);
        Mockito.when(usuarioRepo.existsBycorreo("juan@example.com")).thenReturn(false);

        var result = usuarioService.checkIfNicknameOrEmailExists("Juan", "juan@example.com");

        assertTrue(result.get("nicknameExists"));
        assertFalse(result.get("emailExists"));
    }
}
