package py.com.personal.mimundo.services.pines.models;

/**
 * Created by mabpg on 14/06/17.
 */

public class ValidarPinSesion {

    private boolean exitoso;
    private String mensaje;
    private int codigo;

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
