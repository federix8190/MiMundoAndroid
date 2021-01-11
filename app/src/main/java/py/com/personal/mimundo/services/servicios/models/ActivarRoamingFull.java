package py.com.personal.mimundo.services.servicios.models;

/**
 * Created by Konecta on 13/10/2014.
 */
public class ActivarRoamingFull {

    String modeloTelefono;

    public ActivarRoamingFull() {
    }

    public ActivarRoamingFull(String modeloTelefono) {
        this.modeloTelefono = modeloTelefono;
    }

    public String getModeloTelefono() {
        return modeloTelefono;
    }

    public void setModeloTelefono(String modeloTelefono) {
        this.modeloTelefono = modeloTelefono;
    }
}
