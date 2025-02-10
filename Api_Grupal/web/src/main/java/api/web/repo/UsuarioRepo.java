package api.web.repo;

import api.web.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
    // Puedes añadir métodos personalizados aquí si los necesitas
    boolean existsByNombre(String nickname);
    boolean existsBycorreo(String email);
    Optional<Usuario> findBycorreo(String email);
}