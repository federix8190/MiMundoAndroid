package py.com.personal.mimundo.services.grupos.models;

import java.util.List;

/**
 * Created by Konecta on 19/09/2014.
 */
public class ListaFacturas {

    private List<Factura> lista;

    public ListaFacturas() {
    }

    public List<Factura> getLista() {
        return lista;
    }

    public void setLista(List<Factura> lista) {
        this.lista = lista;
    }
}
