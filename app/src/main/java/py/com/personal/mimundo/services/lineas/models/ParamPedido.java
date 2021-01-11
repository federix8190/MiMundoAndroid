package py.com.personal.mimundo.services.lineas.models;

public class ParamPedido {

    String numeroLinea;

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public ParamPedido(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

}
