package api.web.controller;

import api.web.entity.Localizacion;
import api.web.entity.Proyecto;
import api.web.entity.Usuario;
import api.web.service.LocalizacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LocalizacionControllerTest {

    @Mock
    private LocalizacionService localizacionService;

    @InjectMocks
    private LocalizacionController localizacionController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Localizacion localizacion;
    private Proyecto proyecto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(localizacionController).build();
        objectMapper = new ObjectMapper();

        // Crear Proyecto y Localizacion simulados
        Usuario usuario = new Usuario();
        usuario.setId_usuario(1L);
        usuario.setNombre("Usuario Test");
        usuario.setCorreo("usuario@test.com");

        proyecto = new Proyecto();
        proyecto.setId_proyecto(1L);
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción Proyecto");
        proyecto.setUsuario(usuario);

        localizacion = new Localizacion();
        localizacion.setId_localizacion(1L);
        localizacion.setNombre("Localización Test");
        localizacion.setDescripcion("Descripción Localización");
        localizacion.setLink_map("https://maps.google.com/localizacion");
        localizacion.setProyecto(proyecto);
    }

    @Test
    void testObtenerTodos() throws Exception {
        // Simula el retorno del servicio
        when(localizacionService.obtenerTodos()).thenReturn(Arrays.asList(localizacion));

        // Realiza la petición GET /api/localizacion
        mockMvc.perform(get("/api/localizacion"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el Content-Type
                .andExpect(jsonPath("$[0].id_localizacion").value(localizacion.getId_localizacion()))
                .andExpect(jsonPath("$[0].nombre").value(localizacion.getNombre())); // Valida el nombre

        // Verifica que el servicio fue llamado
        verify(localizacionService, times(1)).obtenerTodos();
    }

    @Test
    void testCrearLocalizacion() throws Exception {
        // Simula el comportamiento del servicio
        when(localizacionService.guardarLocalizacion(any(Localizacion.class))).thenReturn(localizacion);

        // Realiza la petición POST /api/localizacion enviando una Localizacion como JSON
        mockMvc.perform(post("/api/localizacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localizacion)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el Content-Type
                .andExpect(jsonPath("$.id_localizacion").value(localizacion.getId_localizacion()))
                .andExpect(jsonPath("$.nombre").value(localizacion.getNombre())); // Valida el nombre

        // Verifica que el servicio fue llamado
        verify(localizacionService, times(1)).guardarLocalizacion(any(Localizacion.class));
    }

    @Test
    void testObtenerPorId_Encontrado() throws Exception {
        // Simula el retorno del servicio
        when(localizacionService.obtenerPorId(localizacion.getId_localizacion())).thenReturn(Optional.of(localizacion));

        // Realiza la petición GET /api/localizacion/{id}
        mockMvc.perform(get("/api/localizacion/{id}", localizacion.getId_localizacion()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el Content-Type
                .andExpect(jsonPath("$.id_localizacion").value(localizacion.getId_localizacion()))
                .andExpect(jsonPath("$.nombre").value(localizacion.getNombre())); // Valida el nombre

        // Verifica que el servicio fue llamado
        verify(localizacionService, times(1)).obtenerPorId(localizacion.getId_localizacion());
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        // Simula el retorno del servicio
        when(localizacionService.obtenerPorId(anyLong())).thenReturn(Optional.empty());

        // Realiza la petición GET /api/localizacion/{id} para un ID inexistente
        mockMvc.perform(get("/api/localizacion/{id}", 999L))
                .andExpect(status().isNotFound()); // Verifica que devuelva un 404 Not Found

        // Verifica que el servicio fue llamado
        verify(localizacionService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testEliminarLocalizacion_Existente() throws Exception {
        // Simula el retorno del servicio
        when(localizacionService.obtenerPorId(localizacion.getId_localizacion())).thenReturn(Optional.of(localizacion));
        doNothing().when(localizacionService).eliminarLocalizacion(localizacion.getId_localizacion());

        // Realiza la petición DELETE /api/localizacion/{id}
        mockMvc.perform(delete("/api/localizacion/{id}", localizacion.getId_localizacion()))
                .andExpect(status().isNoContent()); // Verifica que devuelva un 204 No Content

        // Verifica que se llamaron los métodos adecuados
        verify(localizacionService, times(1)).obtenerPorId(localizacion.getId_localizacion());
        verify(localizacionService, times(1)).eliminarLocalizacion(localizacion.getId_localizacion());
    }

    @Test
    void testEliminarLocalizacion_NoExistente() throws Exception {
        // Simula el retorno del servicio
        when(localizacionService.obtenerPorId(anyLong())).thenReturn(Optional.empty());

        // Realiza la petición DELETE /api/localizacion/{id} para un ID inexistente
        mockMvc.perform(delete("/api/localizacion/{id}", 999L))
                .andExpect(status().isNotFound()); // Verifica que devuelva un 404 Not Found

        // Verifica que el servicio fue llamado
        verify(localizacionService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testActualizarLocalizacion() throws Exception {
        // Simula el retorno del servicio
        when(localizacionService.obtenerPorId(localizacion.getId_localizacion())).thenReturn(Optional.of(localizacion));
        when(localizacionService.guardarLocalizacion(any(Localizacion.class))).thenReturn(localizacion);

        // Realiza la petición PUT /api/localizacion/{id}
        mockMvc.perform(put("/api/localizacion/{id}", localizacion.getId_localizacion())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localizacion)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el Content-Type
                .andExpect(jsonPath("$.id_localizacion").value(localizacion.getId_localizacion()))
                .andExpect(jsonPath("$.nombre").value(localizacion.getNombre())); // Valida el nombre

        // Verifica que se llamaron los métodos adecuados
        verify(localizacionService, times(1)).guardarLocalizacion(any(Localizacion.class));
    }
}