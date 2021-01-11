package py.com.personal.mimundo.fragments.configuracion;

import java.util.List;

import py.com.personal.mimundo.services.destinos.models.Destino;

/**
 * Created by Konecta on 10/10/2014.
 */
public class ListaDestinos {

    private List<Destino> lista;

    public ListaDestinos(List<Destino> lista) {
        this.lista = lista;
    }

    public List<Destino> getLista() {
        return lista;
    }

    public void setLista(List<Destino> lista) {
        this.lista = lista;
    }
}
