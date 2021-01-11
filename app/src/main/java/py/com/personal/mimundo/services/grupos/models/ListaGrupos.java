package py.com.personal.mimundo.services.grupos.models;

import java.util.List;

/**
 * Created by Konecta on 19/09/2014.
 */
public class ListaGrupos {

    private List<GrupoFacturacion> lista;

    public ListaGrupos() {
    }

    public List<GrupoFacturacion> getLista() {
        return lista;
    }

    public void setLista(List<GrupoFacturacion> lista) {
        this.lista = lista;
    }
}
