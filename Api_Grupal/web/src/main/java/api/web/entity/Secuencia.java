package api.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Secuencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id_secuencia;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    public String nombre;

    @Size(min = 6, max = 6, message = "El color debe tener 6 caracteres")
    public String color;

    @Min(value = 0, message = "El minuto de inicio no puede ser negativo")
    public int min_inicio;

    @Min(value = 0, message = "El minuto de inicio no puede ser negativo")
    public int min_final;

    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    @JsonBackReference // Evita la serialización infinita
    private Proyecto proyecto;

    @OneToMany(mappedBy = "secuencia")
    @JsonManagedReference // Evita la serialización infinita
    @JsonIgnore
    public List<Escena> escenas;

    // Getters y Setters
    public int getId_secuencia() {
        return id_secuencia;
    }

    public void setId_secuencia(int id_secuencia) {
        this.id_secuencia = id_secuencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getMin_inicio() {
        return min_inicio;
    }

    public void setMin_inicio(int min_inicio) {
        this.min_inicio = min_inicio;
    }

    public int getMin_final() {
        return min_final;
    }

    public void setMin_final(int min_final) {
        this.min_final = min_final;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<Escena> getEscenas() {
        return escenas;
    }

    public void setEscenas(List<Escena> escenas) {
        this.escenas = escenas;
    }

}
