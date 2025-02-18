package api.web.controller;

import api.web.entity.Localizacion;
import api.web.entity.Proyecto;
import api.web.entity.Storyboard;
import api.web.service.ProyectoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProyectoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProyectoService proyectoService;

    @InjectMocks
    private ProyectoController proyectoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(proyectoController).build();
    }

    @Test
    void obtenerTodos() throws Exception {
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setId_proyecto(1L);
        proyecto1.setNombre("Proyecto 1");

        Proyecto proyecto2 = new Proyecto();
        proyecto2.setId_proyecto(2L);
        proyecto2.setNombre("Proyecto 2");

        List<Proyecto> proyectos = Arrays.asList(proyecto1, proyecto2);

        when(proyectoService.obtenerTodos()).thenReturn(proyectos);

        mockMvc.perform(get("/api/proyectos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id_proyecto").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Proyecto 1"))
                .andExpect(jsonPath("$[1].id_proyecto").value(2))
                .andExpect(jsonPath("$[1].nombre").value("Proyecto 2"));

        verify(proyectoService, times(1)).obtenerTodos();
    }

    @Test
    void obtenerPorId_ProyectoExistente() throws Exception {
        Long id = 1L;

        Proyecto proyecto = new Proyecto();
        proyecto.setId_proyecto(id);
        proyecto.setNombre("Proyecto Existente");

        when(proyectoService.obtenerPorId(id)).thenReturn(Optional.of(proyecto));

        mockMvc.perform(get("/api/proyectos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_proyecto").value(1))
                .andExpect(jsonPath("$.nombre").value("Proyecto Existente"));

        verify(proyectoService, times(1)).obtenerPorId(id);
    }

    @Test
    void obtenerPorId_ProyectoNoExistente() throws Exception {
        Long id = 1L;

        when(proyectoService.obtenerPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/proyectos/{id}", id))
                .andExpect(status().isNotFound());

        verify(proyectoService, times(1)).obtenerPorId(id);
    }

    @Test
    void crearProyecto() throws Exception {
        Proyecto proyecto = new Proyecto();
        proyecto.setId_proyecto(1L);
        proyecto.setNombre("Proyecto Test");

        when(proyectoService.guardarProyecto(any(Proyecto.class))).thenReturn(proyecto);

        mockMvc.perform(post("/api/proyectos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Proyecto Test\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_proyecto").value(1))
                .andExpect(jsonPath("$.nombre").value("Proyecto Test"));

        verify(proyectoService, times(1)).guardarProyecto(any(Proyecto.class));
    }

    @Test
    void eliminarProyecto_ProyectoExistente() throws Exception {
        Long id = 1L;
        Proyecto proyecto = new Proyecto();
        proyecto.setId_proyecto(id);

        when(proyectoService.obtenerPorId(id)).thenReturn(Optional.of(proyecto));

        mockMvc.perform(delete("/api/proyectos/{id}", id))
                .andExpect(status().isNoContent());

        verify(proyectoService, times(1)).obtenerPorId(id);
        verify(proyectoService, times(1)).eliminarProyecto(id);
    }

    @Test
    void eliminarProyecto_ProyectoNoExistente() throws Exception {
        Long id = 1L;

        when(proyectoService.obtenerPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/proyectos/{id}", id))
                .andExpect(status().isNotFound());

        verify(proyectoService, times(1)).obtenerPorId(id);
        verify(proyectoService, times(0)).eliminarProyecto(id);
    }

    @Test
    void actualizarProyecto() throws Exception {
        Long id = 1L;
        Proyecto proyectoExistente = new Proyecto();
        proyectoExistente.setId_proyecto(id);
        proyectoExistente.setNombre("Proyecto Existente");

        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setId_proyecto(id);
        proyectoActualizado.setNombre("Proyecto Actualizado");

        when(proyectoService.obtenerPorId(id)).thenReturn(Optional.of(proyectoExistente));
        when(proyectoService.guardarProyecto(any(Proyecto.class))).thenReturn(proyectoActualizado);

        mockMvc.perform(put("/api/proyectos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Proyecto Actualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_proyecto").value(1))
                .andExpect(jsonPath("$.nombre").value("Proyecto Actualizado"));

        verify(proyectoService, times(1)).obtenerPorId(id);
        verify(proyectoService, times(1)).guardarProyecto(any(Proyecto.class));
    }

    @Test
    void getLocalizaciones() throws Exception {
        Long id = 1L;
        Localizacion localizacion1 = new Localizacion();
        Localizacion localizacion2 = new Localizacion();
        List<Localizacion> localizaciones = Arrays.asList(localizacion1, localizacion2);

        Proyecto proyecto = new Proyecto();
        proyecto.setId_proyecto(id);
        proyecto.setLocalizaciones(localizaciones);

        when(proyectoService.obtenerPorId(id)).thenReturn(Optional.of(proyecto));

        mockMvc.perform(get("/api/proyectos/{id}/localizaciones", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(proyectoService, times(1)).obtenerPorId(id);
    }

    @Test
    void getStoryboards() throws Exception {
        Long id = 1L;
        Storyboard storyboard1 = new Storyboard();
        Storyboard storyboard2 = new Storyboard();
        List<Storyboard> storyboards = Arrays.asList(storyboard1, storyboard2);

        Proyecto proyecto = new Proyecto();
        proyecto.setId_proyecto(id);
        proyecto.setStoryboards(storyboards);

        when(proyectoService.obtenerPorId(id)).thenReturn(Optional.of(proyecto));

        mockMvc.perform(get("/api/proyectos/{id}/storyboards", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(proyectoService, times(1)).obtenerPorId(id);
    }
}