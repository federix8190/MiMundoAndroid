package py.com.personal.mimundo.services.lineas.models;


import java.util.List;

public class DetalleRecargaSos {

    String fechaPrestamo;
    Double montoPrestamo;
    Double montoRecargo;
    List<DetallePago> pagos;
    String tiempoGracia;

    public DetalleRecargaSos() {
    }

    public DetalleRecargaSos(String fechaPrestamo, Double montoPrestamo, Double montoRecargo,
                             List<DetallePago> pagos, String tiempoGracia) {
        this.fechaPrestamo = fechaPrestamo;
        this.montoPrestamo = montoPrestamo;
        this.montoRecargo = montoRecargo;
        this.pagos = pagos;
        this.tiempoGracia = tiempoGracia;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Double getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(Double montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public List<DetallePago> getPagos() {
        return pagos;
    }

    public void setPagos(List<DetallePago> pagos) {
        this.pagos = pagos;
    }

    public Double getMontoRecargo() {
        return montoRecargo;
    }

    public void setMontoRecargo(Double montoRecargo) {
        this.montoRecargo = montoRecargo;
    }

    public String getTiempoGracia() {
        return tiempoGracia;
    }

    public void setTiempoGracia(String tiempoGracia) {
        this.tiempoGracia = tiempoGracia;
    }
}

