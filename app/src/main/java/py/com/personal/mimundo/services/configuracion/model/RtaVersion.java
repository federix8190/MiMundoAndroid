package py.com.personal.mimundo.services.configuracion.model;

/**
 * Created by Konecta on 11/08/2014.
 */
public class RtaVersion {

    private String clave;
    private String valor;
    private String esExterna;
    private String descripcion;

    public String getClave() {
        return clave;
    }

    public String getValor() {
        return valor;
    }

    public String getEsExterna() {
        return esExterna;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setEsExterna(String esExterna) {
        this.esExterna = esExterna;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
