package py.com.personal.mimundo.services.lineas.models;

import java.util.List;

public class MontosRecargaSos {

    String codigo;
    String mensaje;
    String numeroLinea;
    String fecha;
    List<String> costos;
    List<String> items;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<String> getCostos() {
        return costos;
    }

    public void setCostos(List<String> costos) {
        this.costos = costos;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
