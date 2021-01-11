package py.com.personal.mimundo.services.lineas.consumo;

import java.util.List;

/**
 * Created by Konecta on 26/08/2015.
 */
public class HistorialConsumo {

    private List<DatosHistorialConsumo> entrantes;
    private List<DatosHistorialConsumo> salientes;

    public HistorialConsumo() {
    }

    public List<DatosHistorialConsumo> getEntrantes() {
        return entrantes;
    }

    public void setEntrantes(List<DatosHistorialConsumo> entrantes) {
        this.entrantes = entrantes;
    }

    public List<DatosHistorialConsumo> getSalientes() {
        return salientes;
    }

    public void setSalientes(List<DatosHistorialConsumo> salientes) {
        this.salientes = salientes;
    }
}
