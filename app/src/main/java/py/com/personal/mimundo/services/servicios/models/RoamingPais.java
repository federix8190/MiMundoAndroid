package py.com.personal.mimundo.services.servicios.models;

/**
 * Created by Konecta on 16/10/2014.
 */
public class RoamingPais {

    private Integer idPais;
    private String nombre;
    private String codigoPaisKml;
    private String continente;

    public RoamingPais() {
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoPaisKml() {
        return codigoPaisKml;
    }

    public void setCodigoPaisKml(String codigoPaisKml) {
        this.codigoPaisKml = codigoPaisKml;
    }

    public String getContinente() {
        return continente;
    }

    public void setContinente(String continente) {
        this.continente = continente;
    }
}
