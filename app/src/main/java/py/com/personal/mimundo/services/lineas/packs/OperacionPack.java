package py.com.personal.mimundo.services.lineas.packs;

/**
 * Created by Konecta on 16/09/2014.
 */
public class OperacionPack {

    /**
     * Identificador de la operacion.
     * ACT: para activacion y regalo.
     * SUS: para suscripcion
     */
    public enum Codigo {
        ACT,
        SUS,
        REG,
        REA
    }

    public static final String ACT = "ACT";
    public static final String SUS = "SUS";
    public static final String REG = "REG";
    public static final String REA = "REA";


    private String codigo;
    private String descripcion;
    private int cardinalidad;

    public OperacionPack(){
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

    public int getCardinalidad() {
        return cardinalidad;
    }

    public void setCardinalidad(int cardinalidad) {
        this.cardinalidad = cardinalidad;
    }
}
