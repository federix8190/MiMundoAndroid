package py.com.personal.mimundo.services.grupos.models;

import java.util.List;

/**
 * Created by Konecta on 19/09/2014.
 */
public class ListaDetalleFactura {

    private List<DetalleFactura> lista;

    public ListaDetalleFactura() {
    }

    public List<DetalleFactura> getLista() {
        return lista;
    }

    public void setLista(List<DetalleFactura> lista) {
        this.lista = lista;
    }
}
