package api.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import api.web.entity.Secuencia;

@Repository
public interface SecuenciaRepo extends JpaRepository<Secuencia, Long> {
}
