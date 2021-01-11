package py.com.personal.mimundo.services.lineas.models;

public class DetallePago {
    String fecha;
    String monto;

    public DetallePago() {
    }

    DetallePago(String fecha, String monto) {
        this.fecha = fecha;
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
}