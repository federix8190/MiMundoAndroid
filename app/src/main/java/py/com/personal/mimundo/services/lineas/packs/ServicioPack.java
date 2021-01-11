package py.com.personal.mimundo.services.lineas.packs;

/**
 * Created by Konecta on 16/09/2014.
 */
public class ServicioPack {

    private String codigo;
    private float duracion;
    private DuracionUnidad unidad;

    public ServicioPack() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public float getDuracion() {
        return duracion;
    }

    public void setDuracion(float duracion) {
        this.duracion = duracion;
    }

    public DuracionUnidad getUnidad() {
        return unidad;
    }

    public void setUnidad(DuracionUnidad unidad) {
        this.unidad = unidad;
    }
}
