package py.com.personal.mimundo.services.lineas.models;

import java.util.Date;

/**
 * Created by Konecta on 13/11/2015.
 */
public class SaldoPcrf {

    private int diaAcreditacion;
    private long saldoNormalCantidad;
    private long saldoPromocionalCantidad;
    private long saldoTotalCantidad;
    private String saldoNormalVencimiento;
    private String saldoPromocionalVencimiento;
    private Date fechaActualizacion;
    private SaldoPlan plan;
    private SaldoPack pack;

    public SaldoPcrf(){
    }

    public int getDiaAcreditacion() {
        return diaAcreditacion;
    }

    public void setDiaAcreditacion(int diaAcreditacion) {
        this.diaAcreditacion = diaAcreditacion;
    }

    public long getSaldoNormalCantidad() {
        return saldoNormalCantidad;
    }

    public void setSaldoNormalCantidad(long saldoNormalCantidad) {
        this.saldoNormalCantidad = saldoNormalCantidad;
    }

    public long getSaldoPromocionalCantidad() {
        return saldoPromocionalCantidad;
    }

    public void setSaldoPromocionalCantidad(long saldoPromocionalCantidad) {
        this.saldoPromocionalCantidad = saldoPromocionalCantidad;
    }

    public long getSaldoTotalCantidad() {
        return saldoTotalCantidad;
    }

    public void setSaldoTotalCantidad(long saldoTotalCantidad) {
        this.saldoTotalCantidad = saldoTotalCantidad;
    }

    public String getSaldoNormalVencimiento() {
        return saldoNormalVencimiento;
    }

    public void setSaldoNormalVencimiento(String saldoNormalVencimiento) {
        this.saldoNormalVencimiento = saldoNormalVencimiento;
    }

    public String getSaldoPromocionalVencimiento() {
        return saldoPromocionalVencimiento;
    }

    public void setSaldoPromocionalVencimiento(String saldoPromocionalVencimiento) {
        this.saldoPromocionalVencimiento = saldoPromocionalVencimiento;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public SaldoPlan getPlan() {
        return plan;
    }

    public void setPlan(SaldoPlan plan) {
        this.plan = plan;
    }

    public SaldoPack getPack() {
        return pack;
    }

    public void setPack(SaldoPack pack) {
        this.pack = pack;
    }
}
