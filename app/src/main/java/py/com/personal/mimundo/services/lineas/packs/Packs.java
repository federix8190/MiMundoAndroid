package py.com.personal.mimundo.services.lineas.packs;

/**
 * Created by Konecta on 17/09/2014.
 */
public class Packs {

    private Pack[] lista;

    public Packs() {
    }

    public Packs(Pack[] lista) {
        this.lista = lista;
    }

    public Pack[] getLista() {
        return lista;
    }

    public void setLista(Pack[] lista) {
        this.lista = lista;
    }
}
