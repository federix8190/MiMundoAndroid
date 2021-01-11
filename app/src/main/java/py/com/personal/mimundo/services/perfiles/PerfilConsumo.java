package py.com.personal.mimundo.services.perfiles;

/**
 * Created by Konecta on 05/11/2014.
 */
public class PerfilConsumo {

    private String categoria;
    private String codigo;
    private String descripcion;
    private Long limite;

    public PerfilConsumo() {
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }
}
