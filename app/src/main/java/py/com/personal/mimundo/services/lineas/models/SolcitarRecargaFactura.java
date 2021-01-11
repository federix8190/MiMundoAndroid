package py.com.personal.mimundo.services.lineas.models;

public class SolcitarRecargaFactura {

    private Double monto;
    private Integer porcentaje;

    public SolcitarRecargaFactura(Double monto, Integer porcentaje) {
        this.monto = monto;
        this.porcentaje = porcentaje;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }


    public Integer getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Integer porcentaje) {
        this.porcentaje = porcentaje;
    }
}
