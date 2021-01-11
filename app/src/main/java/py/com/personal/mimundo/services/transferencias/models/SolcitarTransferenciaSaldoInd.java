package py.com.personal.mimundo.services.transferencias.models;

public class SolcitarTransferenciaSaldoInd {

    String origen;
    String destino;
    String monto;
    String pin;

    public SolcitarTransferenciaSaldoInd(String origen, String destino, String monto, String pin) {
        this.monto = monto;
        this.origen = origen;
        this.destino = destino;
        this.pin = pin;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

}
