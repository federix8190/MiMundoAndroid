package py.com.personal.mimundo.models;

import java.util.ArrayList;
import java.util.List;

public class NavDrawerItem {

    public static List<String> itemsFragments;
    public static Integer padreSeleccionado = -1;
    public static Integer hijoSeleccionado = -1;
    private String titulo;
    private String iconoIzquierdo;
    private int iconoDerecho;
    private List<NavDrawerItem> hijos;
    private boolean fragment;
    private String nombreCLase;

    public NavDrawerItem() {
        this.titulo = null;
        this.iconoIzquierdo = null;
        this.nombreCLase = null;
        this.hijos = new ArrayList<NavDrawerItem>();
        this.fragment = false;
    }

    public NavDrawerItem(String titulo, String iconoIzquierdo, String nombreClase) {
        this.titulo = titulo;
        this.iconoIzquierdo = iconoIzquierdo;
        this.nombreCLase =  nombreClase;
        this.hijos = new ArrayList<NavDrawerItem>();
        this.fragment = itemsFragments.contains(nombreClase);
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getIconoIzquierdo() {
        return this.iconoIzquierdo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setIconoIzquierdo(String iconoIzquierdo) {
        this.iconoIzquierdo = iconoIzquierdo;
    }

    public boolean isColapsable() {
        return this.hijos != null && this.hijos.size() > 0;
    }

    public void setHijos(List<NavDrawerItem> hijos) {
        this.hijos = hijos;
    }

    public List<NavDrawerItem> getHijos() {
        return this.hijos;
    }

    public int getIconoDerecho() {
        return iconoDerecho;
    }

    public void setIconoDerecho(int iconoDerecho) {
        this.iconoDerecho = iconoDerecho;
    }

    public boolean isFragment() {
        return fragment;
    }

    public void setFragment(boolean fragment) {
        this.fragment = fragment;
    }

    public String getNombreCLase() {
        return this.nombreCLase;
    }

    public void setNombreCLase(String nombreCLase) {
        this.nombreCLase = nombreCLase;
    }
}
