package py.com.personal.mimundo.services.perfiles;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.lineas.models.Linea;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Konecta on 05/11/2014.
 */
public interface PerfilesConsumoInterface {

    /**
     * Obtiene la lista de perfiles de consumo del usuario.
     *
     * @param numeroLinea Numero de linea
     * @param registros Cantidad de registros
     *
     * @return Lista de perfiles de consumo
     */
    @GET("/perfiles-consumo")
    public ListaPaginada<PerfilConsumo> obtenerPerfilesConsumo(@Query("numeroLinea") String numeroLinea,
                                                               @Query("registros") int registros,
                                                               @Query("ordenadoPor") String orden);

    /**
     * Aumenta el limite de consumo de la linea. Si el aumento no se pudo
     * realizar automaticamente, se crea un ticket de aumento de limite de
     * consumo.
     *
     * @param numeroLinea Numero de linea
     * @param datosLinea Datos de la linea
     * @return
     */
    @PUT("/lineas/{numeroLinea}/limite-consumo")
    public Response aumentarLimiteCredito(@Path("numeroLinea") String numeroLinea,
                                          @Body Linea datosLinea);

}
