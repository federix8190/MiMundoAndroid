package py.com.personal.mimundo.services.usuarios.models;

/**
 * Created by Konecta on 11/08/2014.
 */
public class ModificarClave {

    private Long pinId;
    private String respuesta;
    private String clave;

    public ModificarClave() {
    }

    public Long getPinId() {
        return pinId;
    }

    public void setPinId(Long pinId) {
        this.pinId = pinId;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
