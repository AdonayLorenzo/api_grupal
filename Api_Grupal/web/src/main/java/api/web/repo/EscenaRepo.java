package api.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import api.web.entity.Escena;

@Repository
public interface EscenaRepo extends JpaRepository<Escena, Integer> {
}
