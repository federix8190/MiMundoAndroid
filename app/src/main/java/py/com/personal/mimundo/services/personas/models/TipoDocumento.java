package py.com.personal.mimundo.services.personas.models;

/**
 * Created by Konecta on 29/07/2014.
 */
public class TipoDocumento {

    private String tipo;
    private String descripcion;

    public TipoDocumento() {
    }

    public TipoDocumento(String tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
