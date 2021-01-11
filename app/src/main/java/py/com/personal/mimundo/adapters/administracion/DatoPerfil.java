package py.com.personal.mimundo.adapters.administracion;

/**
 * Created by Usuario on 9/5/2014.
 */
public class DatoPerfil {

    private String titulo;
    private String valor;
    private int icono;

    public DatoPerfil(String titulo, String valor, int icono) {
        this.titulo = titulo;
        this.valor = valor;
        this.icono = icono;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getValor() {
        return valor;
    }

    public int getIcono() {
        return icono;
    }
}
