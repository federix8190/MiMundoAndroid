package py.com.personal.mimundo.services.pines.models;

/**
 * Created by Konecta on 11/08/2014.
 */
public class ValidarPin {

    private String respuesta;

    public ValidarPin(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
