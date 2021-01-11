package py.com.personal.mimundo.services.lineas.consumo;

import java.util.Date;

/**
 * Created by Konecta on 26/08/2015.
 */
public class DatosHistorialConsumo {

    private Date fecha;
    private Double cantidad;

    public DatosHistorialConsumo(Date fecha, Double cantidad) {
        this.fecha = fecha;
        this.cantidad = cantidad;
    }

    public DatosHistorialConsumo() {
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
}
