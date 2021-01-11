package py.com.personal.mimundo.services;

/**
 * Created by Konecta on 23/07/2014.
 */
public class Resultado {

    private boolean exitoso;
    private String mensaje;
    private int codigo;

    public Resultado() {
    }

    public Resultado(boolean exitoso, String mensaje) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
    }

    public Resultado(boolean exitoso, String mensaje, int codigo) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.codigo = codigo;
    }

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
