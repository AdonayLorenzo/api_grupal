package api.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import javax.validation.constraints.NotBlank;

@Entity
public class Localizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_localizacion;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @Size(min = 0, max = 255, message = "La descripcion es muy larga")
    private String descripcion;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;


    @Size(min = 0, max = 255, message = "La descripcion es muy larga")
    private String link_map;

    @ManyToOne
    @JoinColumn(name = "id_proyecto" , nullable = false)
    @JsonBackReference  // Evita la serialización infinita
    private Proyecto proyecto;

    //Getters and Setters

    public Long getId_localizacion() {
        return id_localizacion;
    }

    public void setId_localizacion(Long id_localizacion) {
        this.id_localizacion = id_localizacion;
    }

    public @NotBlank(message = "El nombre no puede estar vacío") @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre no puede estar vacío") @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres") String nombre) {
        this.nombre = nombre;
    }

    public @Size(min = 0, max = 255, message = "La descripcion es muy larga") String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@Size(min = 0, max = 255, message = "La descripcion es muy larga") String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }
    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getLink_map() {
        return link_map;
    }
    public void setLink_map(String link_map) {
        this.link_map = link_map;
    }

}