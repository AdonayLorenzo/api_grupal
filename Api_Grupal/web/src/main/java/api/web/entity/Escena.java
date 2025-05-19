package api.web.entity;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

@Entity
public class Escena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id_escena;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    public String nombre;

    @Min(value = 0, message = "El minuto de inicio no puede ser negativo")
    public int min_inicio;

    @Min(value = 0, message = "El minuto de inicio no puede ser negativo")
    public int min_final;

    @ManyToOne
    @JoinColumn(name = "id_proyecto" , nullable = false)
    @JsonBackReference  // Evita la serialización infinita
    private Proyecto proyecto;

    @OneToMany(mappedBy = "escena")
    @JsonManagedReference // Evita la serialización infinita
    @JsonIgnore
    public List<Secuencia> secuencias;


    public int getId_escena() {
        return id_escena;
    }

    public void setId_escena(int id_escena) {
        this.id_escena = id_escena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

}
