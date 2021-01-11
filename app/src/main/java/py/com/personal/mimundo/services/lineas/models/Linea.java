package py.com.personal.mimundo.services.lineas.models;

/**
 * Created by Konecta on 24/07/2014.
 */
public class Linea {

    private Long codigoCliente;
    private String numeroLinea;
    private String numeroContrato;
    private String claseComercial;
    private String grupoFacturacion;
    private String codigoTecnologia;
    private String descripcionTecnologia;
    private String codigoTipoLinea;
    private String descripcionTipoLinea;
    private String codigoEstadoContrato;
    private String descripcionEstadoContrato;
    private String codigoEstadoLinea;
    private String descripcionEstadoLinea;
    private String organizacionVenta;
    private String perfilConsumo;
    private Integer limiteConsumo;
    private Boolean tieneUsuario;
    private Boolean tieneDebitoAutomatico;
    private Long fechaActivacion;
    private String tipoLinea;

    public Linea() {
    }

    public Linea(String linea) {
        this.numeroLinea = linea;
    }

    public Long getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getClaseComercial() {
        return claseComercial;
    }

    public void setClaseComercial(String claseComercial) {
        this.claseComercial = claseComercial;
    }

    public String getGrupoFacturacion() {
        return grupoFacturacion;
    }

    public void setGrupoFacturacion(String grupoFacturacion) {
        this.grupoFacturacion = grupoFacturacion;
    }

    public String getCodigoTecnologia() {
        return codigoTecnologia;
    }

    public void setCodigoTecnologia(String codigoTecnologia) {
        this.codigoTecnologia = codigoTecnologia;
    }

    public String getDescripcionTecnologia() {
        return descripcionTecnologia;
    }

    public void setDescripcionTecnologia(String descripcionTecnologia) {
        this.descripcionTecnologia = descripcionTecnologia;
    }

    public String getCodigoTipoLinea() {
        return codigoTipoLinea;
    }

    public void setCodigoTipoLinea(String codigoTipoLinea) {
        this.codigoTipoLinea = codigoTipoLinea;
    }

    public String getDescripcionTipoLinea() {
        return descripcionTipoLinea;
    }

    public void setDescripcionTipoLinea(String descripcionTipoLinea) {
        this.descripcionTipoLinea = descripcionTipoLinea;
    }

    public String getCodigoEstadoContrato() {
        return codigoEstadoContrato;
    }

    public void setCodigoEstadoContrato(String codigoEstadoContrato) {
        this.codigoEstadoContrato = codigoEstadoContrato;
    }

    public String getDescripcionEstadoContrato() {
        return descripcionEstadoContrato;
    }

    public void setDescripcionEstadoContrato(String descripcionEstadoContrato) {
        this.descripcionEstadoContrato = descripcionEstadoContrato;
    }

    public String getCodigoEstadoLinea() {
        return codigoEstadoLinea;
    }

    public void setCodigoEstadoLinea(String codigoEstadoLinea) {
        this.codigoEstadoLinea = codigoEstadoLinea;
    }

    public String getDescripcionEstadoLinea() {
        return descripcionEstadoLinea;
    }

    public void setDescripcionEstadoLinea(String descripcionEstadoLinea) {
        this.descripcionEstadoLinea = descripcionEstadoLinea;
    }

    public String getOrganizacionVenta() {
        return organizacionVenta;
    }

    public void setOrganizacionVenta(String organizacionVenta) {
        this.organizacionVenta = organizacionVenta;
    }

    public String getPerfilConsumo() {
        return perfilConsumo;
    }

    public void setPerfilConsumo(String perfilConsumo) {
        this.perfilConsumo = perfilConsumo;
    }

    public Integer getLimiteConsumo() {
        return limiteConsumo;
    }

    public void setLimiteConsumo(Integer limiteConsumo) {
        this.limiteConsumo = limiteConsumo;
    }

    public Boolean getTieneUsuario() {
        return tieneUsuario;
    }

    public void setTieneUsuario(Boolean tieneUsuario) {
        this.tieneUsuario = tieneUsuario;
    }

    public Boolean getTieneDebitoAutomatico() {
        return tieneDebitoAutomatico;
    }

    public void setTieneDebitoAutomatico(Boolean tieneDebitoAutomatico) {
        this.tieneDebitoAutomatico = tieneDebitoAutomatico;
    }

    public Long getFechaActivacion() {
        return fechaActivacion;
    }

    public void setFechaActivacion(Long fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }

    public String getTipoLinea() {
        return tipoLinea;
    }

    public void setTipoLinea(String tipoLinea) {
        this.tipoLinea = tipoLinea;
    }

    @Override
    public String toString() {
        return getNumeroLinea();
    }
}
