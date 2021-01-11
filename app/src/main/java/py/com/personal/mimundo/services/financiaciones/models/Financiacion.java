package py.com.personal.mimundo.services.financiaciones.models;

/**
 * Created by Konecta on 04/08/2014.
 */
public class Financiacion {

    private Long numeroCuenta;
    private String codigoEstado;
    private String descripcionEstado;
    private String codigoGrupo;
    private String numeroLinea;
    private Double monto;
    private Double saldo;
    private Long fechaInicio;
    private Long primerVencimiento;

    public Financiacion() {
    }

    public Financiacion(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Financiacion(Long numeroCuenta, Double monto, String descripcionEstado, Long primerVencimiento) {
        this.numeroCuenta = numeroCuenta;
        this.monto = monto;
        this.descripcionEstado = descripcionEstado;
        this.primerVencimiento = primerVencimiento;
    }

    public Long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
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

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Long getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Long getPrimerVencimiento() {
        return primerVencimiento;
    }

    public void setPrimerVencimiento(Long primerVencimiento) {
        this.primerVencimiento = primerVencimiento;
    }
}
