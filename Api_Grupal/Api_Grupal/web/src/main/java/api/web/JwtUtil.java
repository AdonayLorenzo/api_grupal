package api.web;

import api.web.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 86400000; // 24 horas en milisegundos

    // Generar un token JWT para un usuario
    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getCorreo()) // Usar el correo como subject
                .claim("id", usuario.getId_usuario()) // Incluir el ID del usuario como una claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    // Extraer todas las claims (reclamaciones) del token
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("El token ha expirado", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Token inválido", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el token", e);
        }
    }

    // Extraer el nombre de usuario (correo) del token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Verificar si el token ha expirado
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Validar el token
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Extraer el ID del usuario desde el token
    public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("id", Long.class); // Extraer el ID del usuario desde la claim
        } catch (Exception e) {
            throw new RuntimeException("No se pudo extraer el ID del usuario desde el token", e);
        }
    }
}