package py.com.personal.mimundo.services.lineas.consumo;

/**
 * Created by Konecta on 26/08/2015.
 */
public class HistorialesConsumo {

    private HistorialConsumo llamadas;
    private HistorialConsumo datos;
    private HistorialConsumo minutos;
    private HistorialConsumo mensajes;

    public HistorialesConsumo() {
    }

    public HistorialConsumo getLlamadas() {
        return llamadas;
    }

    public void setLlamadas(HistorialConsumo llamadas) {
        this.llamadas = llamadas;
    }

    public HistorialConsumo getDatos() {
        return datos;
    }

    public void setDatos(HistorialConsumo datos) {
        this.datos = datos;
    }

    public HistorialConsumo getMinutos() {
        return minutos;
    }

    public void setMinutos(HistorialConsumo minutos) {
        this.minutos = minutos;
    }

    public HistorialConsumo getMensajes() {
        return mensajes;
    }

    public void setMensajes(HistorialConsumo mensajes) {
        this.mensajes = mensajes;
    }
}
