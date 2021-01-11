package py.com.personal.mimundo.services.lineas.models;

import java.util.List;

/**
 * Created by Konecta on 15/09/2014.
 */
public class ListaServiciosLinea {

    private List<Servicio> servicios;

    public ListaServiciosLinea(){
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }
}
