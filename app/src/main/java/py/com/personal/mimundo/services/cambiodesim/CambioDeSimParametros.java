package py.com.personal.mimundo.services.cambiodesim;

/**
 * Created by Konecta on 18/05/2016.
 */
public class CambioDeSimParametros {

    private String lineaOrigen;
    private String simDestino;
    private String codigoTipoCambio;

    public CambioDeSimParametros() {
    }

    public String getLineaOrigen() {
        return lineaOrigen;
    }

    public void setLineaOrigen(String lineaOrigen) {
        this.lineaOrigen = lineaOrigen;
    }

    public String getSimDestino() {
        return simDestino;
    }

    public void setSimDestino(String simDestino) {
        this.simDestino = simDestino;
    }

    public String getCodigoTipoCambio() {
        return codigoTipoCambio;
    }

    public void setCodigoTipoCambio(String codigoTipoCambio) {
        this.codigoTipoCambio = codigoTipoCambio;
    }
}
