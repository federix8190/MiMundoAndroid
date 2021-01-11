package py.com.personal.mimundo.services.terminales.models;

/**
 * Created by Konecta on 16/10/2014.
 */
public class Marca {

    private String nombre;

    public Marca() {
    }

    public Marca(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
