package py.com.personal.mimundo.services.financiaciones.models;

/**
 * Created by Konecta on 04/08/2014.
 */
public class Cuota {

    private Long numeroFactura;
    private String numeroCuota;
    private String codigoEstado;
    private String descripcionEstado;
    private Double montoCuota;
    private Long fechaVencimiento;

    public Cuota() {
    }

    public Long getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Long numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(String numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public String getCodigoEstado() {
        return codigoEstado;
    }

    public void setCodigoEstado(String codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public Double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(Double montoCuota) {
        this.montoCuota = montoCuota;
    }

    public Long getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Long fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
