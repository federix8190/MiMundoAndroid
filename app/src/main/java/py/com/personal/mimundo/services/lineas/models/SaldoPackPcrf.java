package py.com.personal.mimundo.services.lineas.models;

import java.util.Date;

/**
 * Created by Konecta on 13/11/2015.
 */
public class SaldoPackPcrf {

    private String codigo;
    private String descripcion;
    private long cuotaAsignadaBytes;
    private long cantidadUsadaBytes;
    private long cantidadDisponibleBytes;
    private boolean tieneBeneficio;
    private Date fechaVencimiento;

    public SaldoPackPcrf() {}

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getCuotaAsignadaBytes() {
        return cuotaAsignadaBytes;
    }

    public void setCuotaAsignadaBytes(long cuotaAsignadaBytes) {
        this.cuotaAsignadaBytes = cuotaAsignadaBytes;
    }

    public long getCantidadUsadaBytes() {
        return cantidadUsadaBytes;
    }

    public void setCantidadUsadaBytes(long cantidadUsadaBytes) {
        this.cantidadUsadaBytes = cantidadUsadaBytes;
    }

    public long getCantidadDisponibleBytes() {
        return cantidadDisponibleBytes;
    }

    public void setCantidadDisponibleBytes(long cantidadDisponibleBytes) {
        this.cantidadDisponibleBytes = cantidadDisponibleBytes;
    }

    public boolean isTieneBeneficio() {
        return tieneBeneficio;
    }

    public void setTieneBeneficio(boolean tieneBeneficio) {
        this.tieneBeneficio = tieneBeneficio;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
