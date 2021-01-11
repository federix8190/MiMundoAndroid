package py.com.personal.mimundo.models;

import java.io.Serializable;
import java.math.BigDecimal;


public class TerminalSerializable implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigoProducto;
    private String codigoPlanLlamadas;
    private String codigoPlanDatos;
    private String codigoPlanMensajes;
    private String codigoCargo;
    private BigDecimal cargoMonto;
    private BigDecimal porcentajeDescuento;
    private String descripcion;

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getCodigoPlanLlamadas() {
        return codigoPlanLlamadas;
    }

    public void setCodigoPlanLlamadas(String codigoPlanLlamadas) {
        this.codigoPlanLlamadas = codigoPlanLlamadas;
    }

    public String getCodigoPlanDatos() {
        return codigoPlanDatos;
    }

    public void setCodigoPlanDatos(String codigoPlanDatos) {
        this.codigoPlanDatos = codigoPlanDatos;
    }

    public String getCodigoPlanMensajes() {
        return codigoPlanMensajes;
    }

    public void setCodigoPlanMensajes(String codigoPlanMensajes) {
        this.codigoPlanMensajes = codigoPlanMensajes;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public BigDecimal getCargoMonto() {
        return cargoMonto;
    }

    public void setCargoMonto(BigDecimal cargoMonto) {
        this.cargoMonto = cargoMonto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

