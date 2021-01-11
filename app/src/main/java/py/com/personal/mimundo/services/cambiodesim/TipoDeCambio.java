package py.com.personal.mimundo.services.cambiodesim;

/**
 * Created by Konecta on 23/06/2016.
 */
public class TipoDeCambio {

    private String codigo;
    private String descripcion;

    public TipoDeCambio() {
    }

    public TipoDeCambio(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
