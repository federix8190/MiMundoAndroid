package py.com.personal.mimundo.services.lineas.models;

import java.util.List;

/**
 * Created by Konecta on 19/09/2014.
 */
public class ListaLineas {

    private List<Linea> lista;

    public ListaLineas() {
    }

    public ListaLineas(List<Linea> lista) {
        this.lista = lista;
    }

    public List<Linea> getLista() {
        return lista;
    }

    public void setLista(List<Linea> lista) {
        this.lista = lista;
    }
}
