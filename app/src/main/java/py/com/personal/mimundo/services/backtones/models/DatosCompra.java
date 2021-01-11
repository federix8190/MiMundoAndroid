package py.com.personal.mimundo.services.backtones.models;

import py.com.personal.mimundo.services.Resultado;

/**
 * Created by Konecta on 03/12/2015.
 */
public class DatosCompra {

    private Resultado resultado;
    private Boolean esTrial;
    private String unidad;
    private Double precio;
    private Integer dias;

    public DatosCompra() {
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public Boolean getEsTrial() {
        return esTrial;
    }

    public void setEsTrial(Boolean esTrial) {
        this.esTrial = esTrial;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

}
