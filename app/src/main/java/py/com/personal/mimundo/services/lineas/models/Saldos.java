package py.com.personal.mimundo.services.lineas.models;

import java.util.List;

/**
 * Created by Konecta on 30/07/2014.
 */
public class Saldos {

    /*
	 * Tipos de saldo
	 */
    public static final String SALDO_NORMAL = "Saldo";
    public static final String SALDO_PROMOCIONAL = "Saldo Promocional";
    public static final String SALDO_EXCEDENTE = "Excedente";
    public static final String SALDO_DISPONIBLE = "Disponible";

    private List<DetalleSaldo> mensajes;
    private List<DetalleSaldo> minutos;
    private List<DetalleSaldo> datos;
    private List<DetalleSaldo> moneda;
    private List<DetalleSaldo> pospago;
    private List<SaldoPack> packs;

    public Saldos() {
    }

    public List<DetalleSaldo> getPospago() {
        return pospago;
    }

    public List<DetalleSaldo> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<DetalleSaldo> mensajes) {
        this.mensajes = mensajes;
    }

    public List<DetalleSaldo> getMinutos() {
        return minutos;
    }

    public void setMinutos(List<DetalleSaldo> minutos) {
        this.minutos = minutos;
    }

    public List<DetalleSaldo> getDatos() {
        return datos;
    }

    public void setDatos(List<DetalleSaldo> datos) {
        this.datos = datos;
    }

    public List<DetalleSaldo> getMoneda() {
        return moneda;
    }

    public void setMoneda(List<DetalleSaldo> moneda) {
        this.moneda = moneda;
    }

    public List<SaldoPack> getPacks() {
        return packs;
    }

    public void setPacks(List<SaldoPack> packs) {
        this.packs = packs;
    }
}
