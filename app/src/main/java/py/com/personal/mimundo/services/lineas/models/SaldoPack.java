package py.com.personal.mimundo.services.lineas.models;

import java.util.List;

/**
 * Created by Konecta on 30/07/2014.
 */
public class SaldoPack {

    private String nombre;
    private List<DetalleSaldoPack> detalle;

    public SaldoPack() {
    }

    public SaldoPack(String nombre, List<DetalleSaldoPack> detalle) {
        this.nombre = nombre;
        this.detalle = detalle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DetalleSaldoPack> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleSaldoPack> detalle) {
        this.detalle = detalle;
    }
}
