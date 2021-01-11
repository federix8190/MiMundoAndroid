package py.com.personal.mimundo.fragments.gestion.financiacion;

import py.com.personal.mimundo.disenhos.Formateador;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;

/**
 * Created by Usuario on 9/18/2014.
 */
public class ItemDeListaCuota {

    public static final String CODIGO_SIN_DATOS = "sinDatos";
    public static final String CODIGO_LOADING = "loading";
    public static final String CODIGO_SIN_SERVICIO = "sinServicio";
    public static final String CODIGO_CUOTA_CANCELADA = "CAN";
    private String estado = "200Ok";
    private String numeroFactura;
    private String fechaVencimiento;
    private String numeroCuota;
    private String codigoEstado;
    private String descripcionEstado;
    private String montoCuota;
    private boolean cancelada;

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Long numeroFactura) {
        if (numeroFactura != null) {
            this.numeroFactura = String.valueOf(numeroFactura);
        } else {
            this.numeroFactura = "Sin número de factura";
        }
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Long fechaVencimiento) {
        if (fechaVencimiento != null) {
            this.fechaVencimiento = Formateador.formatearFecha(fechaVencimiento);
        } else {
            this.fechaVencimiento = MensajesDeUsuario.MENSAJE_NULL;
        }
    }

    public String getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(String numeroCuota) {
        if (numeroCuota != null){
            this.numeroCuota = numeroCuota;
        } else {
            this.numeroCuota = MensajesDeUsuario.MENSAJE_NULL;
        }
    }

    public String getCodigoEstado() {
        return codigoEstado;
    }

    public void setCodigoEstado(String codigoEstado) {
        if (codigoEstado != null) {
            if (codigoEstado.equals(CODIGO_CUOTA_CANCELADA)) {
                this.codigoEstado = "CANCELADA";
                setCancelada(true);
            } else {
                this.codigoEstado = "ACTIVA";
                setCancelada(false);
            }
        } else {
            this.codigoEstado = MensajesDeUsuario.MENSAJE_NULL;
            setCancelada(false);
        }
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        if (descripcionEstado != null) {
            this.descripcionEstado = descripcionEstado;
        } else {
            this.descripcionEstado = MensajesDeUsuario.MENSAJE_NULL;
        }
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado != null) {
            this.estado = estado;
        } else {
            this.estado = MensajesDeUsuario.MENSAJE_NULL;
        }
    }

    public String getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(Double montoCuota) {
        if (montoCuota != null) {
            this.montoCuota = String.valueOf(montoCuota.intValue());
        } else {
            this.montoCuota =  MensajesDeUsuario.MENSAJE_NULL;
        }
    }
}
