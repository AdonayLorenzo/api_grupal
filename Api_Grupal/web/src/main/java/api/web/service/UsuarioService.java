package api.web.service;

import api.web.entity.Usuario;
import api.web.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepo usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepo usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método para obtener todos los usuarios
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll(); // Usamos el repositorio directamente
    }

    // Método para guardar un nuevo usuario
    public Usuario guardarUsuario(Usuario usuario) {
        // El usuario se guarda automáticamente usando el repositorio
        return usuarioRepository.save(usuario);
    }

    // Método para obtener un usuario por ID
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id); // Usamos el repositorio directamente
    }

    // Método para eliminar un usuario por ID
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id); // Usamos el repositorio directamente
    }

    public Map<String, Boolean> checkIfNicknameOrEmailExists(String nickname, String email) {
        boolean nicknameExists = usuarioRepository.existsByNombre(nickname);
        boolean emailExists = usuarioRepository.existsBycorreo(email);

        Map<String, Boolean> result = new HashMap<>();
        result.put("nicknameExists", nicknameExists);
        result.put("emailExists", emailExists);

        return result;
    }

    public Usuario findByEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findBycorreo(email);
        return usuario.orElse(null);
    }
}
