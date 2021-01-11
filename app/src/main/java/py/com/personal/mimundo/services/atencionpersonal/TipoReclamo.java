package py.com.personal.mimundo.services.atencionpersonal;

/**
 * Created by Konecta on 01/10/2015.
 */
public class TipoReclamo {

    private Integer idTipoReclamo;
    private String tecnologia;
    private String codigoProceso;
    private String codigoAccion;
    private String descripcion;

    public TipoReclamo() {
    }

    public Integer getIdTipoReclamo() {
        return idTipoReclamo;
    }

    public void setIdTipoReclamo(Integer idTipoReclamo) {
        this.idTipoReclamo = idTipoReclamo;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public String getCodigoProceso() {
        return codigoProceso;
    }

    public void setCodigoProceso(String codigoProceso) {
        this.codigoProceso = codigoProceso;
    }

    public String getCodigoAccion() {
        return codigoAccion;
    }

    public void setCodigoAccion(String codigoAccion) {
        this.codigoAccion = codigoAccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
