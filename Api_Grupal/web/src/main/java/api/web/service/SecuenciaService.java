package api.web.service;

import api.web.entity.Secuencia;
import api.web.repo.SecuenciaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecuenciaService {

    @Autowired
    private SecuenciaRepo secuenciaRepository;

    public List<Secuencia> getAllSecuencias() {
        return secuenciaRepository.findAll();
    }

    public Optional<Secuencia> getSecuenciaById(int id) {
        return secuenciaRepository.findById(id);
    }

    public Secuencia saveSecuencia(Secuencia secuencia) {
        return secuenciaRepository.save(secuencia);
    }

    public void deleteSecuencia(Integer id) {
        secuenciaRepository.deleteById(id);
    }

    public Secuencia updateSecuencia(int id, Secuencia updatedSecuencia) {
        return secuenciaRepository.findById(id).map(secuencia -> {
            secuencia.setNombre(updatedSecuencia.getNombre());
            secuencia.setColor(updatedSecuencia.getColor());
            secuencia.setMin_inicio(updatedSecuencia.getMin_inicio());
            secuencia.setMin_final(updatedSecuencia.getMin_final());
            secuencia.setEscenas(updatedSecuencia.getEscenas());
            return secuenciaRepository.save(secuencia);
        }).orElseGet(() -> {
            updatedSecuencia.setId_secuencia(Math.toIntExact(id));
            return secuenciaRepository.save(updatedSecuencia);
        });
    }

}
