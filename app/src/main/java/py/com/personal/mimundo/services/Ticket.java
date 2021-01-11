package py.com.personal.mimundo.services;

/**
 * Created by Konecta on 13/10/2014.
 */
public class Ticket {

    private String numeroLinea;
    private Integer numeroPedido;
    private Long fechaOperacion;
    private String descripcionOperacion;
    private String observaciones;

    public Ticket() {
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Long getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(Long fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getDescripcionOperacion() {
        return descripcionOperacion;
    }

    public void setDescripcionOperacion(String descripcionOperacion) {
        this.descripcionOperacion = descripcionOperacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
