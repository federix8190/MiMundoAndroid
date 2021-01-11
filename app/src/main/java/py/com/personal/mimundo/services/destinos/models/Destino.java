package py.com.personal.mimundo.services.destinos.models;

/**
 * Created by Konecta on 06/10/2014.
 */
public class Destino {

    private String numeroLinea;
    private String codigoServicio;
    private String codigoGrupo;
    private Long fechaInicio;
    private Long fechaFin;

    public Destino(){
    }

    public Destino(String numeroLinea) {
        this.numeroLinea = numeroLinea;
        this.codigoGrupo = "";
        this.codigoServicio = "";
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(String codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public Long getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Long getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Long fechaFin) {
        this.fechaFin = fechaFin;
    }
}
