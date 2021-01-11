package py.com.personal.mimundo.services.lineas.models;

/**
 * Created by Konecta on 30/07/2014.
 */
public class DetalleSaldoPack extends DetalleSaldo {

    private String renovacion;

    public DetalleSaldoPack() {
    }

    public DetalleSaldoPack(String tipo, String monto, String unidad, String renovacion) {
        super(tipo, monto, unidad);
        this.renovacion = renovacion;
    }

    public DetalleSaldoPack(String tipo, Long monto, String unidad, Long vencimiento,
                            String renovacion) {
        super(tipo, monto, unidad, vencimiento);
        this.renovacion = renovacion;
    }

    public String getRenovacion() {
        return renovacion;
    }

    public void setRenovacion(String renovacion) {
        this.renovacion = renovacion;
    }
}
