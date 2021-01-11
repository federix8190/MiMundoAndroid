package py.com.personal.mimundo.services;

import java.util.List;

/**
 * Created by Konecta on 24/07/2014.
 */
public class ListaPaginada<T> {

    private List<T> lista;
    private int inicio;
    private int registros;
    private int cantidad;

    public ListaPaginada(){
    }

    public List<T> getLista() {
        return lista;
    }

    public void setLista(List<T> lista) {
        this.lista = lista;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getRegistros() {
        return registros;
    }

    public void setRegistros(int registros) {
        this.registros = registros;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
