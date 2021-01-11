package py.com.personal.mimundo.services.cambiodesim;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.Resultado;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Konecta on 18/05/2016.
 */
public interface CambioSimInterface {

    @POST("/cambio-de-sim/{msisdn}")
    public Resultado procesar(@Path("msisdn")String msisdn, @Body CambioDeSimParametros parametros);


    @GET("/cambio-de-sim/{msisdn}/tipos-de-cambio")
    public ListaPaginada<TipoDeCambio> obtenerTiposDeCambio(@Path("msisdn")String msisdn);
}
