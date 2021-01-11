package py.com.personal.mimundo.services.lineas.models;

public class RespuestaRecargaFactura {

    private String exitoso;
    private String mensaje;

    public RespuestaRecargaFactura() {
    }

    public String getExitoso() {
        return exitoso;
    }

    public void setExitoso(String exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
