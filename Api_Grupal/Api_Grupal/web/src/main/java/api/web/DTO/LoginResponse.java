package api.web.DTO;

import api.web.entity.Usuario;

public class LoginResponse {
    private String token;


    public LoginResponse(String token) {
        this.token = token;
    }

    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

