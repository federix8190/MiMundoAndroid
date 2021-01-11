package py.com.personal.mimundo.services.lineas.packs;

/**
 * Created by Konecta on 16/09/2014.
 */
public class NodoPack {

    private String descripcion;
    private NodoPack[] hijos;
    private Pack[] packs;
    private boolean principal = false;

    public NodoPack() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public NodoPack[] getHijos() {
        return hijos;
    }

    public void setHijos(NodoPack[] hijos) {
        this.hijos = hijos;
    }

    public Pack[] getPacks() {
        return packs;
    }

    public void setPacks(Pack[] packs) {
        this.packs = packs;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }
}
