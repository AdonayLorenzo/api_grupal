package api.web.controller;

import api.web.DTO.LoginRequest;
import api.web.DTO.LoginResponse;
import api.web.JwtUtil;
import api.web.entity.Usuario;
import api.web.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private String jwtToken = "fake-jwt-token";

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
    public void testObtenerUsuarios() throws Exception {
        Mockito.when(usuarioService.obtenerTodos()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    public void testCheckIfNicknameOrEmailExists() throws Exception {
        Mockito.when(usuarioService.checkIfNicknameOrEmailExists("Juan", "juan@example.com"))
                .thenReturn(Map.of("nicknameExists", true, "emailExists", false));

        mockMvc.perform(get("/api/usuarios/exists?nickname=Juan&email=juan@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nicknameExists").value(true))
                .andExpect(jsonPath("$.emailExists").value(false));
    }

    @Test
    public void testCrearUsuario() throws Exception {
        Mockito.when(usuarioService.guardarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    public void testObtenerUsuarioPorId() throws Exception {
        Mockito.when(jwtUtil.extractUserId(jwtToken)).thenReturn(1L);
        Mockito.when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/" + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        Mockito.when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuario));
        Mockito.doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(usuarioService, Mockito.times(1)).eliminarUsuario(1L);
    }

    @Test
    public void testActualizarUsuario() throws Exception {
        Mockito.when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioService.guardarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }
}
