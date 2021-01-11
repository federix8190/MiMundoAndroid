package py.com.personal.mimundo.services.financiaciones.models;

/**
 * Created by Konecta on 04/08/2014.
 */
public class Factura {

    private Long numeroFactura;
    private String codigoDocumento;
    private String tipoDocumento;
    private String grupoFacturacion;
    private Long numeroCuenta;
    private Double saldo;

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

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
}
