package py.com.personal.mimundo.models;

import java.util.List;

/**
 * Created by Konecta on 06/02/2015.
 */
public class ListaDetallesConsumo {

    private List<DetallesConsumo> lista;

    public ListaDetallesConsumo(List<DetallesConsumo> lista) {
        this.lista = lista;
    }

    public List<DetallesConsumo> getLista() {
        return lista;
    }

    public void setLista(List<DetallesConsumo> lista) {
        this.lista = lista;
    }
}
