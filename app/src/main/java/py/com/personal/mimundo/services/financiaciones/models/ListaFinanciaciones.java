package py.com.personal.mimundo.services.financiaciones.models;

import java.util.List;

/**
 * Created by Konecta on 22/09/2014.
 */
public class ListaFinanciaciones {

    private List<Financiacion> lista;

    public ListaFinanciaciones() {
    }

    public List<Financiacion> getLista() {
        return lista;
    }

    public void setLista(List<Financiacion> lista) {
        this.lista = lista;
    }
}
