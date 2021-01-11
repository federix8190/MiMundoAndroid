package py.com.personal.mimundo.security.client;

import py.com.personal.mimundo.security.client.dao.AccessTokenResponse;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface AuthorizationInterface {

	/**
	 * 
	 * @param grantType
	 * @param user
	 * @param password
	 * @param responseType
	 * @param clientId
	 * @param redirectUri
	 * @param state
	 * @return {@link py.com.personal.mimundo.security.client.dao.AccessTokenResponse} si es correcto.
	 */
    @FormUrlEncoded
	@POST("/auth/token")
	public AccessTokenResponse authenticate(
            @Field("grant_type") String grantType,
            @Field("user") String user,
            @Field("password") String password,
            @Field("response_type") String responseType,
            @Field("client_id") String clientId,
            @Field("redirect_uri") String redirectUri,
            @Field("state") String state,
            @Field("token") String token) throws RetrofitError;

}

