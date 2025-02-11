package api.web.repo;

import api.web.entity.Localizacion;
import api.web.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalizacionRepo extends JpaRepository<Localizacion, Long> {

}