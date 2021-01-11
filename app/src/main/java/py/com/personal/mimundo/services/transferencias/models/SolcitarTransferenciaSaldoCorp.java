package py.com.personal.mimundo.services.transferencias.models;

public class SolcitarTransferenciaSaldoCorp {

//    [
//    {
//        "origen": "971977138"
//        "destino": "976385890",
//            "tipo": "GUA", // Tipo de saldo (GUA, SMS, MIN)
//            "unidadTiempo": "", // // Unidad de tiempo de la duracion (DIA, MES, ANO)
//            "duracion": 1000, // Duracion del saldo
//            "monto": 2000,
//            "pin" :
//    }, ...
//            ]

    String origen;
    String destino;
    String monto;
    String tipo;
    String unidadTiempo;
    String duracion;
    String pin;

    public SolcitarTransferenciaSaldoCorp(String origen, String destino, String monto,String tipo, String pin) {
        this.monto = monto;
        this.origen = origen;
        this.destino = destino;
        this.tipo = tipo;
        this.pin = pin;
        this.unidadTiempo = "DIA";
        this.duracion = "1";
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getUnidadTiempo() {
        return unidadTiempo;
    }

    public void setUnidadTiempo(String unidadTiempo) {
        this.unidadTiempo = unidadTiempo;
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
