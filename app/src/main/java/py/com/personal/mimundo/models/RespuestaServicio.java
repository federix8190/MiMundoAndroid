package py.com.personal.mimundo.models;

/**
 * Created by Konecta on 04/09/2014.
 */
public class RespuestaServicio<T> {

    private T datos;
    private String mensaje;

    public RespuestaServicio(T datos, String mensaje) {
        this.datos = datos;
        this.mensaje = mensaje;
    }

    public T getDatos() {
        return datos;
    }

    public void setDatos(T datos) {
        this.datos = datos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
