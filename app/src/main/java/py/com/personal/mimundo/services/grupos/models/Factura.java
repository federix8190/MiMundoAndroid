package py.com.personal.mimundo.services.grupos.models;

/**
 * Created by Konecta on 04/08/2014.
 */
public class Factura {

    private Long numeroFactura;
    private String codigoDocumento;
    private String tipoDocumento;
    private String grupoFacturacion;
    private Long numeroCuenta;
    private String nombre;
    private String apellido;
    private String ciudad;
    private String barrio;
    private String direccion;
    private String periodo;
    private String estado;
    private Double montoNeto;
    private Double montoIva;
    private Double montoTotal;
    private Double montoFacturaAnterior;
    private Double montoCuota;
    private Double saldo;
    private Long fechaEmision;
    private Long fechaVencimiento;
    private Long tipoCambio;

    public Factura() {
    }

    public Long getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Long numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getGrupoFacturacion() {
        return grupoFacturacion;
    }

    public void setGrupoFacturacion(String grupoFacturacion) {
        this.grupoFacturacion = grupoFacturacion;
    }

    public Long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getMontoNeto() {
        return montoNeto;
    }

    public void setMontoNeto(Double montoNeto) {
        this.montoNeto = montoNeto;
    }

    public Double getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(Double montoIva) {
        this.montoIva = montoIva;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Double getMontoFacturaAnterior() {
        return montoFacturaAnterior;
    }

    public void setMontoFacturaAnterior(Double montoFacturaAnterior) {
        this.montoFacturaAnterior = montoFacturaAnterior;
    }

    public Double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(Double montoCuota) {
        this.montoCuota = montoCuota;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Long getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Long fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Long getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Long fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Long getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(Long tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
}
