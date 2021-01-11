package py.com.personal.mimundo.services.atencionpersonal;

import py.com.personal.mimundo.services.ListaPaginada;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Konecta on 30/09/2015.
 */
public interface AtencionPersonalInterface {

    @GET("/atencion-al-cliente/tipos-de-reclamo")
    @Headers("Content-Type: application/json")
    public ListaPaginada<TipoReclamo> obtenerTiposReclamo(@Query("registros") int registros);

    @POST("/atencion-al-cliente/reclamos")
    public Response enviarReclamo(@Body Reclamo datos);

    @POST("/atencion-al-cliente/sugerencias")
    public Response enviarSugerencia(@Body Sugerencia datos);
}
