package py.com.personal.mimundo.services.atencionpersonal;

/**
 * Created by Konecta on 05/10/2015.
 */
public class Sugerencia {

    private String sugerencia;
    private String datosContacto;
    private String email;
    private String nombres;
    private String numeroLinea;
    private String respuesta;
    private Long peticionId;

    public Sugerencia() {
    }

    public Sugerencia(String datosContacto, String email, String sugerencia) {
        this.datosContacto = datosContacto;
        this.email = email;
        this.sugerencia = sugerencia;
    }

    public String getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(String sugerencia) {
        this.sugerencia = sugerencia;
    }

    public String getDatosContacto() {
        return datosContacto;
    }

    public void setDatosContacto(String datosContacto) {
        this.datosContacto = datosContacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
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

    public void setPeticionId(Long peticionId) {
        this.peticionId = peticionId;
    }
}
