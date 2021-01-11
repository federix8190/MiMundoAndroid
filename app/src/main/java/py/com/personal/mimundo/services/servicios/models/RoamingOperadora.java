package py.com.personal.mimundo.services.servicios.models;

/**
 * Created by Konecta on 16/10/2014.
 */
public class RoamingOperadora {

    private Integer idOperadora;
    private RoamingPais pais;
    private String nombre;
    private String frecuencia;
    private Boolean tieneRoamingVozSms;
    private Boolean tieneRoamingDatosGprs;
    private Boolean tieneRoaming3g;
    private Boolean tieneRoamingAutomatico;
    private Long fechaDesde;

    public RoamingOperadora() {
    }

    public Integer getIdOperadora() {
        return idOperadora;
    }

    public void setIdOperadora(Integer idOperadora) {
        this.idOperadora = idOperadora;
    }

    public RoamingPais getPais() {
        return pais;
    }

    public void setPais(RoamingPais pais) {
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Boolean getTieneRoamingVozSms() {
        return tieneRoamingVozSms;
    }

    public void setTieneRoamingVozSms(Boolean tieneRoamingVozSms) {
        this.tieneRoamingVozSms = tieneRoamingVozSms;
    }

    public Boolean getTieneRoamingDatosGprs() {
        return tieneRoamingDatosGprs;
    }

    public void setTieneRoamingDatosGprs(Boolean tieneRoamingDatosGprs) {
        this.tieneRoamingDatosGprs = tieneRoamingDatosGprs;
    }

    public Boolean getTieneRoaming3g() {
        return tieneRoaming3g;
    }

    public void setTieneRoaming3g(Boolean tieneRoaming3g) {
        this.tieneRoaming3g = tieneRoaming3g;
    }

    public Boolean getTieneRoamingAutomatico() {
        return tieneRoamingAutomatico;
    }

    public void setTieneRoamingAutomatico(Boolean tieneRoamingAutomatico) {
        this.tieneRoamingAutomatico = tieneRoamingAutomatico;
    }

    public Long getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Long fechaDesde) {
        this.fechaDesde = fechaDesde;
    }
}
