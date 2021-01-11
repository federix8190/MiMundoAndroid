package py.com.personal.mimundo.activities;

import java.io.Serializable;
import java.util.List;

import py.com.personal.mimundo.services.lineas.models.DetalleSaldo;

/**
 * Created by carlos on 10/12/14.
 */
public class SaldosParceable implements Serializable {

    private List<py.com.personal.mimundo.services.lineas.models.DetalleSaldo> mensajes;
    private List<py.com.personal.mimundo.services.lineas.models.DetalleSaldo> minutos;
    private List<py.com.personal.mimundo.services.lineas.models.DetalleSaldo> datos;
    private List<py.com.personal.mimundo.services.lineas.models.DetalleSaldo> moneda;

    public SaldosParceable(List<DetalleSaldo> mensajes, List<DetalleSaldo> minutos,
                           List<DetalleSaldo> datos, List<DetalleSaldo> moneda) {
        this.mensajes = mensajes;
        this.minutos = minutos;
        this.datos = datos;
        this.moneda = moneda;
    }

    public List<DetalleSaldo> getMensajes() {
        return mensajes;
    }

    public List<DetalleSaldo> getMinutos() {
        return minutos;
    }

    public List<DetalleSaldo> getDatos() {
        return datos;
    }

    public List<DetalleSaldo> getMoneda() {
        return moneda;
    }


}
