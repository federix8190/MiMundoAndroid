package py.com.personal.mimundo.services.lineas.models;

import java.util.List;

/**
 * Created by Konecta on 15/09/2014.
 */
public class ListaPlanesLinea {

    private List<Plan> planes;

    public ListaPlanesLinea(){
    }

    public List<Plan> getPlanes() {
        return planes;
    }

    public void setPlanes(List<Plan> planes) {
        this.planes = planes;
    }
}
