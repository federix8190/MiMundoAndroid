package py.com.personal.mimundo.adapters;

/**
 * Created by Usuario on 9/9/2014.
 */
public class CaracteristicasEquipo {

    String caracteristica;
    String descripcion;
    int icono;

    public CaracteristicasEquipo(String caracteristica, String descripcion, int icono) {
        this.caracteristica = caracteristica;
        this.descripcion = descripcion;
        this.icono = icono;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getIcono() {
        return icono;
    }
}
