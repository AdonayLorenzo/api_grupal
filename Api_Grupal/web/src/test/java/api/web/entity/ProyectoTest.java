package api.web.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProyectoTest {

    @Test
    void testProyectoEntity() {
        // Crear un proyecto
        Proyecto proyecto = new Proyecto();
        proyecto.setId_proyecto(1L);
        proyecto.setNombre("Proyecto de prueba");
        proyecto.setDescripcion("Descripción del proyecto");

        // Verificar los valores
        assertEquals(1L, proyecto.getId_proyecto());
        assertEquals("Proyecto de prueba", proyecto.getNombre());
        assertEquals("Descripción del proyecto", proyecto.getDescripcion());
    }

    @Test
    void testProyectoUsuarioRelationship() {
        // Crear un proyecto y un usuario
        Proyecto proyecto = new Proyecto();
        Usuario usuario = new Usuario();
        usuario.setId_usuario(1L);

        // Establecer la relación
        proyecto.setUsuario(usuario);

        // Verificar la relación
        assertEquals(1L, proyecto.getUsuario().getId_usuario());
    }
}