package py.com.personal.mimundo.services.servicios.models;

import java.util.List;

import py.com.personal.mimundo.services.lineas.models.Servicio;

/**
 * Created by Konecta on 13/10/2014.
 */
public class EstadoServicio {

    private boolean habilitado;
    private boolean activado;
    private List<Servicio> servicios;

    private EstadoServicio() {
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }
}
