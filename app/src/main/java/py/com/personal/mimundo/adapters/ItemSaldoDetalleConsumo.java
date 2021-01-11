package py.com.personal.mimundo.adapters;


import java.util.List;

import py.com.personal.mimundo.services.lineas.models.DetalleSaldo;

public class ItemSaldoDetalleConsumo {

    private String titulo;
    private String metaTitulo;
    private List<DetalleSaldo> saldos;
    private int icono;

    public ItemSaldoDetalleConsumo(String titulo, int icono, List<DetalleSaldo> saldos, String metaTitulo) {
        this.titulo = titulo;
        this.saldos = saldos;
        this.icono = icono;
        this.metaTitulo = metaTitulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<DetalleSaldo> getSaldos() {
        return saldos;
    }

    public void setSaldos(List<DetalleSaldo> saldos) {
        this.saldos = saldos;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public String getMetaTitulo() {
        return metaTitulo;
    }

    public void setMetaTitulo(String metaTitulo) {
        this.metaTitulo = metaTitulo;
    }
}
