package py.com.personal.mimundo.services.lineas.packs;

/**
 * Created by Konecta on 16/09/2014.
 */
public class Pack {

    private String codigo;
    private String descripcion;
    private float monto;
    private float duracion;
    private DuracionUnidad duracionUnidad;
    private OperacionPack[] operaciones;
    private ServicioPack[] servicios;

    public Pack() {
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

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public DuracionUnidad getDuracionUnidad() {
        return duracionUnidad;
    }

    public void setDuracionUnidad(DuracionUnidad duracionUnidad) {
        this.duracionUnidad = duracionUnidad;
    }

    public float getDuracion() {
        return duracion;
    }

    public void setDuracion(float duracion) {
        this.duracion = duracion;
    }

    public OperacionPack[] getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(OperacionPack[] operaciones) {
        this.operaciones = operaciones;
    }

    public ServicioPack[] getServicios() {
        return servicios;
    }

    public void setServicios(ServicioPack[] servicios) {
        this.servicios = servicios;
    }
}
