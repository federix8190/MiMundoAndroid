package py.com.personal.mimundo.services.financiaciones.models;

import java.util.List;

/**
 * Created by Konecta on 23/09/2014.
 */
public class ListaCuotas {

    private List<Cuota> lista;

    public ListaCuotas() {
    }

    public ListaCuotas(List<Cuota> lista) {
        this.lista = lista;
    }

    public List<Cuota> getLista() {
        return lista;
    }

    public void setLista(List<Cuota> lista) {
        this.lista = lista;
    }
}
