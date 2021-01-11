package py.com.personal.mimundo.services.grupos.models;

/**
 * Created by Konecta on 04/08/2014.
 */
public class ResumenGrupo {

    private Double totalUltimaFactura;
    private Integer cantidadLineasInternet;
    private Integer cantidadLineasTelefonia;
    private Integer cantidadLineasTotal;

    public ResumenGrupo() {
    }

    public Double getTotalUltimaFactura() {
        return totalUltimaFactura;
    }

    public void setTotalUltimaFactura(Double totalUltimaFactura) {
        this.totalUltimaFactura = totalUltimaFactura;
    }

    public Integer getCantidadLineasInternet() {
        return cantidadLineasInternet;
    }

    public void setCantidadLineasInternet(Integer cantidadLineasInternet) {
        this.cantidadLineasInternet = cantidadLineasInternet;
    }

    public Integer getCantidadLineasTelefonia() {
        return cantidadLineasTelefonia;
    }

    public void setCantidadLineasTelefonia(Integer cantidadLineasTelefonia) {
        this.cantidadLineasTelefonia = cantidadLineasTelefonia;
    }

    public Integer getCantidadLineasTotal() {
        return cantidadLineasTotal;
    }

    public void setCantidadLineasTotal(Integer cantidadLineasTotal) {
        this.cantidadLineasTotal = cantidadLineasTotal;
    }
}
