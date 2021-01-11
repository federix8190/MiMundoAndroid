package py.com.personal.mimundo.services.lineas.models;

import java.util.List;

public class OpcionesRecargaContraFactura {

//            {
//    "lista":[{
//        "porcentaje": 30,
//                "monto": 25900.0
//    }],
//            "inicio":0,
//            "registros":1,
//            "cantidad":1




    List<ItemOpcionesRecargaContraFactura> lista;



    public List<ItemOpcionesRecargaContraFactura> getLista() {
        return lista;
    }

    public void setLista(List<ItemOpcionesRecargaContraFactura> lista) {
        this.lista = lista;
    }



}
