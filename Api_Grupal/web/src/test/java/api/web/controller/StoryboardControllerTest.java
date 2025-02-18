package api.web.controller;

import api.web.entity.Storyboard;
import api.web.service.StoryboardService;
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

class StoryboardControllerTest {

    @Mock
    private StoryboardService storyboardService;

    @InjectMocks
    private StoryboardController storyboardController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Storyboard dummyStoryboard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(storyboardController).build();
        objectMapper = new ObjectMapper();

        // Create a sample Storyboard for testing
        dummyStoryboard = new Storyboard();
        dummyStoryboard.setDescripcion("Test storyboard");
        dummyStoryboard.setImagen(new byte[]{1, 2, 3});
    }

    // Test GET /api/storyboard (Retrieve all storyboards)
    @Test
    void testObtenerTodos() throws Exception {
        when(storyboardService.obtenerTodos()).thenReturn(Arrays.asList(dummyStoryboard));

        mockMvc.perform(get("/api/storyboard"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].descripcion").value(dummyStoryboard.getDescripcion()));

        verify(storyboardService, times(1)).obtenerTodos();
    }

    // Test POST /api/storyboard (Create a storyboard)
    @Test
    void testCrearStoryboard() throws Exception {
        when(storyboardService.guardarStoryboard(any(Storyboard.class))).thenReturn(dummyStoryboard);

        mockMvc.perform(post("/api/storyboard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyStoryboard)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.descripcion").value(dummyStoryboard.getDescripcion()));

        verify(storyboardService, times(1)).guardarStoryboard(any(Storyboard.class));
    }

    

    // Test GET /api/storyboard/{id} (Get by ID - Found)
    @Test
    void testObtenerPorId_Found() throws Exception {
        when(storyboardService.obtenerPorId(eq(1L))).thenReturn(Optional.of(dummyStoryboard));

        mockMvc.perform(get("/api/storyboard/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.descripcion").value(dummyStoryboard.getDescripcion()));

        verify(storyboardService, times(1)).obtenerPorId(1L);
    }

    // Test GET /api/storyboard/{id} (Get by ID - Not Found)
    @Test
    void testObtenerPorId_NotFound() throws Exception {
        when(storyboardService.obtenerPorId(eq(1L))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/storyboard/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(storyboardService, times(1)).obtenerPorId(1L);
    }

    // Test DELETE /api/storyboard/{id} (Delete storyboard - Found)
    @Test
    void testEliminarStoryboard_Found() throws Exception {
        when(storyboardService.obtenerPorId(eq(1L))).thenReturn(Optional.of(dummyStoryboard));
        doNothing().when(storyboardService).eliminarStoryboard(eq(1L));

        mockMvc.perform(delete("/api/storyboard/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(storyboardService, times(1)).obtenerPorId(1L);
        verify(storyboardService, times(1)).eliminarStoryboard(1L);
    }

    // Test DELETE /api/storyboard/{id} (Delete storyboard - Not Found)
    @Test
    void testEliminarStoryboard_NotFound() throws Exception {
        when(storyboardService.obtenerPorId(eq(1L))).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/storyboard/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(storyboardService, times(1)).obtenerPorId(1L);
        verify(storyboardService, never()).eliminarStoryboard(anyLong());
    }

    // Test PUT /api/storyboard/{id} (Update storyboard - Found)
    @Test
    void testActualizarStoryboard_Found() throws Exception {
        when(storyboardService.obtenerPorId(eq(1L))).thenReturn(Optional.of(dummyStoryboard));
        when(storyboardService.guardarStoryboard(any(Storyboard.class))).thenReturn(dummyStoryboard);

        // Prepare updated storyboard data
        Storyboard updatedStoryboard = new Storyboard();
        updatedStoryboard.setDescripcion("Updated description");
        updatedStoryboard.setImagen(new byte[]{4, 5, 6});

        mockMvc.perform(put("/api/storyboard/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStoryboard)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.descripcion").value(updatedStoryboard.getDescripcion()));

        verify(storyboardService, times(1)).obtenerPorId(1L);
        verify(storyboardService, times(1)).guardarStoryboard(any(Storyboard.class));
    }

    // Test PUT /api/storyboard/{id} (Update storyboard - Not Found)
    @Test
    void testActualizarStoryboard_NotFound() throws Exception {
        when(storyboardService.obtenerPorId(eq(1L))).thenReturn(Optional.empty());

        // Prepare updated storyboard data
        Storyboard updatedStoryboard = new Storyboard();
        updatedStoryboard.setDescripcion("Updated description");

        mockMvc.perform(put("/api/storyboard/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStoryboard)))
                .andExpect(status().isNotFound());

        verify(storyboardService, times(1)).obtenerPorId(1L);
        verify(storyboardService, never()).guardarStoryboard(any(Storyboard.class));
    }
}