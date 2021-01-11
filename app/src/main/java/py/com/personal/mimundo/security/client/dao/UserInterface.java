package py.com.personal.mimundo.security.client.dao;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Konecta on 21/08/2014.
 */
public interface UserInterface {

    /**
     * Obtiene datos del ultimo acceso del usuario a la aplicacion.
     * @param clientId
     * @param token
     * @return
     */
    @GET("info")
    public Response getUserInfo(
            @Query("client_id") String clientId,
            @Query("token") String token);
}
