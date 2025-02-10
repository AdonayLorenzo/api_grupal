package api.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @Lob
    @Column(name = "datos", columnDefinition = "LONGBLOB")
    private byte[] datos;


    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasenna;

    private Date Fecha_N;

    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference // Evita la serialización infinita
    private List<Proyecto> proyectos;


    // Constructor por defecto
    public Usuario() {}





    // Getters y setters
    public Long getId_usuario() {
        return id_usuario;
    }
    
    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public byte[] getDatos() {
        return datos;
    }
    public void setDatos(byte[] datos) {
        this.datos = datos;
    }
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getContrasenna() {
        return contrasenna;
    }
    
    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }
    
    public Date getFecha_N() {
        return Fecha_N;
    }
    
    public void setFecha_N(Date Fecha_N) {
        this.Fecha_N = Fecha_N;
    }


}
