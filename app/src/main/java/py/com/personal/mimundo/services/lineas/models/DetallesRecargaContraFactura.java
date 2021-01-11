package py.com.personal.mimundo.services.lineas.models;

import java.util.List;

public class DetallesRecargaContraFactura {

//            "numeroLineaOrigen": "",
//            "numeroLineaDestino": "971110031",
//            "fecha": "2014/03/05",
//            "hora": "16:09:09",
//            "cantidad": null,
//            "unidad": "",
//            "costo": 30000,
//            "tipo": "REC_FACT",
//            "descripcion": "ACREDITACION PUNTUAL X RECARGA CONTRA FACTURA"



    String numeroLineaOrigen;
    String numeroLineaDestino;
    String fecha;
    String hora;
    String cantidad;
    String costo;
    String tipo;
    String descripcion;


//    public DetallesRecargaContraFactura(String numeroLineaOrigen, String numeroLineaDestino, String fecha, String hora,
//                      List<DetalleRecargaSos> detalles, String deudaRestante, String totalCobrado,
//                      String totalPrestado) {
//        this.codigo = codigo;
//        this.numeroLinea = numeroLinea;
//        this.mensaje = mensaje;
//        this.cantidadPrestamos = cantidadPrestamos;
//        this.detalles = detalles;
//        this.deudaRestante = deudaRestante;
//        this.totalCobrado = totalCobrado;
//        this.totalPrestado = totalPrestado;
//    }

    public String getNumeroLineaOrigen() {
        return numeroLineaOrigen;
    }

    public void setNumeroLineaOrigen(String numeroLineaOrigen) {
        this.numeroLineaOrigen = numeroLineaOrigen;
    }

    public String getNumeroLineaDestino() {
        return numeroLineaDestino;
    }

    public void setNumeroLineaDestino(String numeroLineaDestino) {
        this.numeroLineaDestino = numeroLineaDestino;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
