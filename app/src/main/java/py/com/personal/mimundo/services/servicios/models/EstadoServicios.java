package py.com.personal.mimundo.services.servicios.models;

/**
 * Created by Konecta on 13/10/2014.
 */
public class EstadoServicios {

    private EstadoServicio bandaHoraria;
    private EstadoServicio secretariaPersonal;
    private EstadoServicio transferenciaLlamadas;
    private EstadoServicio recargaContraFactura;
    private EstadoServicio servicioBlackBerry;
    private EstadoServicio roamingAutomatico;
    private EstadoServicio roamingFull;

    public EstadoServicios() {
    }

    public EstadoServicio getBandaHoraria() {
        return bandaHoraria;
    }

    public void setBandaHoraria(EstadoServicio bandaHoraria) {
        this.bandaHoraria = bandaHoraria;
    }

    public EstadoServicio getSecretariaPersonal() {
        return secretariaPersonal;
    }

    public void setSecretariaPersonal(EstadoServicio secretariaPersonal) {
        this.secretariaPersonal = secretariaPersonal;
    }

    public EstadoServicio getTransferenciaLlamadas() {
        return transferenciaLlamadas;
    }

    public void setTransferenciaLlamadas(EstadoServicio transferenciaLlamadas) {
        this.transferenciaLlamadas = transferenciaLlamadas;
    }

    public EstadoServicio getRecargaContraFactura() {
        return recargaContraFactura;
    }

    public void setRecargaContraFactura(EstadoServicio recargaContraFactura) {
        this.recargaContraFactura = recargaContraFactura;
    }

    public EstadoServicio getServicioBlackBerry() {
        return servicioBlackBerry;
    }

    public void setServicioBlackBerry(EstadoServicio servicioBlackBerry) {
        this.servicioBlackBerry = servicioBlackBerry;
    }

    public EstadoServicio getRoamingAutomatico() {
        return roamingAutomatico;
    }

    public void setRoamingAutomatico(EstadoServicio roamingAutomatico) {
        this.roamingAutomatico = roamingAutomatico;
    }

    public EstadoServicio getRoamingFull() {
        return roamingFull;
    }

    public void setRoamingFull(EstadoServicio roamingFull) {
        this.roamingFull = roamingFull;
    }
}
