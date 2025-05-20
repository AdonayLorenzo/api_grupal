package api.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import javax.validation.constraints.NotBlank;

import java.sql.Blob;
import java.util.List;

@Entity
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_proyecto;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @Size(min = 0, max = 255, message = "La descripcion es muy larga")
    private String descripcion;

    @Lob
    @Column(name = "imagen", columnDefinition = "LONGBLOB")
    private Blob imagen;

    @ManyToOne
    @JoinColumn(name = "id_usuario" , nullable = false)
    @JsonBackReference  // Evita la serialización infinita
    private Usuario usuario;

    @OneToMany(mappedBy = "proyecto")
    @JsonManagedReference // Evita la serialización infinita
    @JsonIgnore
    private List<Localizacion> localizaciones;

    @OneToMany(mappedBy = "proyecto")
    @JsonManagedReference // Evita la serialización infinita
    @JsonIgnore
    private List<Storyboard> storyboards;

    @OneToMany(mappedBy = "proyecto")
    @JsonManagedReference // Evita la serialización infinita
    @JsonIgnore
    public List<Secuencia> secuencias;

    //Getters and Setters

    public Long getId_proyecto() {
        return id_proyecto;
    }

    public void setId_proyecto(Long id_proyecto) {
        this.id_proyecto = id_proyecto;
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

    public Blob getImagen() {
        return imagen;
    }

    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Localizacion> getLocalizaciones() {
        return this.localizaciones;
    }

    public void setLocalizaciones(List<Localizacion> localizaciones) {
        this.localizaciones = localizaciones;
    }

    public List<Storyboard> getStoryboards() {
        return storyboards;
    }
    public List<Secuencia> getSecuencias() {
        return secuencias;
    }

    public void setStoryboards(List<Storyboard> storyboards) {
        this.storyboards = storyboards;
    }

}
