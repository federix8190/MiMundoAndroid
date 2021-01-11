package py.com.personal.mimundo.services.pines.models;

/**
 * Created by Konecta on 25/07/2014.
 */
public class Pin {

    private long id;
    private String respuesta;
    private long fechaCreacion;
    private long validez;
    private String numeroLinea;
    private String email;
    private String canalEnvio;
    private String nombreUsuario;

    public Pin() {
    }

    public Pin(Long id, String respuesta) {
        this.id = id;
        this.respuesta = respuesta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public long getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public long getValidez() {
        return validez;
    }

    public void setValidez(long validez) {
        this.validez = validez;
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

    public String getCanalEnvio() {
        return canalEnvio;
    }

    public void setCanalEnvio(String canalEnvio) {
        this.canalEnvio = canalEnvio;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
