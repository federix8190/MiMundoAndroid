package py.com.personal.mimundo.fragments.saldo.transferencias;

import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoCorp;

public class ResultadoTransferencia {

    private boolean exitoso;
    private String mensaje;
    private String origen;
    private String destino;
    private String monto;
    private String tipo;

    public ResultadoTransferencia() {
    }

    public ResultadoTransferencia(Resultado res, SolcitarTransferenciaSaldoCorp datos) {
        this.exitoso = res.isExitoso();
        this.mensaje = res.getMensaje();
        this.origen = datos.getOrigen();
        this.destino = datos.getDestino();
        this.monto = datos.getMonto();
        this.tipo = datos.getTipo();
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
