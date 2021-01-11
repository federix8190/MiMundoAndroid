package py.com.personal.mimundo.services.lineas.models;

/**
 * Created by Konecta on 30/07/2014.
 */
public class Plan {

    private String codigo;
    private String descripcion;
    private String tasador;
    private String segmento;
    private String tipo;
    private String moneda;
    private Double costo;
    private Double porcentajeIva;
    private Double costoMasIva;

    public Plan() {
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

    public String getTasador() {
        return tasador;
    }

    public void setTasador(String tasador) {
        this.tasador = tasador;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Double getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(Double porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public Double getCostoMasIva() {
        return costoMasIva;
    }

    public void setCostoMasIva(Double costoMasIva) {
        this.costoMasIva = costoMasIva;
    }
}
