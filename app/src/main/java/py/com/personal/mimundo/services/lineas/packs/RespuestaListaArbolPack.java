package py.com.personal.mimundo.services.lineas.packs;

/**
 * Created by Konecta on 16/09/2014.
 */
public class RespuestaListaArbolPack {

    private String codigo;
    private String mensaje;
    private NodoPack[] items;
    private int cantidad;

    public RespuestaListaArbolPack() {
    }

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

    public NodoPack[] getItems() {
        return items;
    }

    public void setItems(NodoPack[] items) {
        this.items = items;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
