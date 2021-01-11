package py.com.personal.mimundo.services.grupos.models;

/**
 * Created by Konecta on 25/07/2014.
 */
public class GrupoFacturacion {

    private String codigo;
    private String descripcion;
    private String estado;
    private Long debito;
    private Long diaFacturacion;
    private Integer diaAcreditacion;

    public GrupoFacturacion() {
    }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getDebito() {
        return debito;
    }

    public void setDebito(Long debito) {
        this.debito = debito;
    }

    public Long getDiaFacturacion() {
        return diaFacturacion;
    }

    public void setDiaFacturacion(Long diaFacturacion) {
        this.diaFacturacion = diaFacturacion;
    }

    public Integer getDiaAcreditacion() {
        return diaAcreditacion;
    }

    public void setDiaAcreditacion(Integer diaAcreditacion) {
        this.diaAcreditacion = diaAcreditacion;
    }
}
