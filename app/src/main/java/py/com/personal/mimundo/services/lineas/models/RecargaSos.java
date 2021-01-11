package py.com.personal.mimundo.services.lineas.models;

import java.util.List;


public class RecargaSos {

    String codigo;
    String mensaje;
    String numeroLinea;
    Integer cantidadPrestamos;
    List<DetalleRecargaSos> detalles;
    String deudaRestante;
    String totalCobrado;
    String totalPrestado;

    public RecargaSos() {
    }

    public RecargaSos(String codigo, String numeroLinea, String mensaje, Integer cantidadPrestamos,
                      List<DetalleRecargaSos> detalles, String deudaRestante, String totalCobrado,
                      String totalPrestado) {
        this.codigo = codigo;
        this.numeroLinea = numeroLinea;
        this.mensaje = mensaje;
        this.cantidadPrestamos = cantidadPrestamos;
        this.detalles = detalles;
        this.deudaRestante = deudaRestante;
        this.totalCobrado = totalCobrado;
        this.totalPrestado = totalPrestado;
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCantidadPrestamos() {
        return cantidadPrestamos;
    }

    public void setCantidadPrestamos(Integer cantidadPrestamos) {
        this.cantidadPrestamos = cantidadPrestamos;
    }

    public List<DetalleRecargaSos> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleRecargaSos> detalles) {
        this.detalles = detalles;
    }

    public String getDeudaRestante() {
        return deudaRestante;
    }

    public void setDeudaRestante(String deudaRestante) {
        this.deudaRestante = deudaRestante;
    }

    public String getTotalPrestado() {
        return totalPrestado;
    }

    public void setTotalPrestado(String totalPrestado) {
        this.totalPrestado = totalPrestado;
    }

    public String getTotalCobrado() {
        return totalCobrado;
    }

    public void setTotalCobrado(String totalCobrado) {
        this.totalCobrado = totalCobrado;
    }
}
