package py.com.personal.mimundo.services.lineas.models;

/**
 * Created by Konecta on 30/07/2014.
 */
public class DetalleSaldo {

    private String tipo;
    private String monto;
    private String unidad;
    private Long vencimiento;

    public DetalleSaldo() {
    }

    public DetalleSaldo(String tipo) {
        this.tipo = tipo;
    }

    public DetalleSaldo(String tipo, String unidad, Long vencimiento) {
        this.tipo = tipo;
        this.unidad = unidad;
        this.vencimiento = vencimiento;
    }

    public DetalleSaldo(String tipo, String monto, String unidad) {
        this.tipo = tipo;
        this.monto = monto;
        this.unidad = unidad;
    }

    public DetalleSaldo(String tipo, String monto, String unidad, Long vencimiento) {
        this(tipo, unidad, vencimiento);
        this.monto = monto;
    }

    public DetalleSaldo(String tipo, Long monto, String unidad, Long vencimiento) {
        this(tipo, unidad, vencimiento);
        this.monto = monto == null ? "0" : String.valueOf(monto);
    }

    public DetalleSaldo(String tipo, Double monto, String unidad, Long vencimiento) {
        this(tipo, unidad, vencimiento);
        this.monto = monto == null || monto == 0.0 ? "0" : String.valueOf(monto);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Long getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Long vencimiento) {
        this.vencimiento = vencimiento;
    }
}
