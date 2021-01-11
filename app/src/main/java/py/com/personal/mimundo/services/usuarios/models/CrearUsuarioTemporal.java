package py.com.personal.mimundo.services.usuarios.models;

/**
 * Created by Konecta on 25/07/2014.
 */
public class CrearUsuarioTemporal {

    private String numeroLinea;
    private String email;
    private String clave;
    private String respuesta;
    private Long peticionId;
    private String facebookToken;

    public CrearUsuarioTemporal() {
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Long getPeticionId() {
        return peticionId;
    }

    public void setPeticionId(Long peticionCaptchaId) {
        this.peticionId = peticionCaptchaId;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }
}
