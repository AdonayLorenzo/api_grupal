package api.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


@Entity
public class Storyboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_storyboard;

    @Size(min = 0, max = 255, message = "La descripcion es muy larga")
    private String descripcion;

    @Lob
    @Column(name = "imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;

    @ManyToOne
    @JoinColumn(name = "id_proyecto" , nullable = false)
    @JsonBackReference  // Evita la serialización infinita
    private Proyecto proyecto;



    //Getters and Setters

    public Long getId_storyboard() {
        return id_storyboard;
    }

    public void setId_localizacion(Long id_storyboard) {
        this.id_storyboard = id_storyboard;
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



}