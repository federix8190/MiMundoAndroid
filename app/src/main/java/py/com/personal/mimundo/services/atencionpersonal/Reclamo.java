package py.com.personal.mimundo.services.atencionpersonal;

/**
 * Created by Konecta on 30/09/2015.
 */
public class Reclamo {

    private String numeroLinea;
    private String descripcion;
    private String codigoProceso;
    private String observaciones;

    public Reclamo() {
    }

    public Reclamo(String numeroLinea, String codigoProceso, String descripcion, String observaciones) {
        this.numeroLinea = numeroLinea;
        this.descripcion = descripcion;
        this.codigoProceso = codigoProceso;
        this.observaciones = observaciones;
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigoProceso() {
        return codigoProceso;
    }

    public void setCodigoProceso(String codigoProceso) {
        this.codigoProceso = codigoProceso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
