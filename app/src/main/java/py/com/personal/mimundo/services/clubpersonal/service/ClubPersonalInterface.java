package py.com.personal.mimundo.services.clubpersonal.service;

import com.squareup.okhttp.Response;

import java.util.List;

import py.com.personal.mimundo.services.clubpersonal.models.RespuestaDatosUsuarioClub;
import py.com.personal.mimundo.services.clubpersonal.models.RespuestaTotalPuntosClub;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaCorp;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaInd;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoCorp;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoInd;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by Konecta on 24/07/2014.
 */
public interface ClubPersonalInterface {


    /**
     * Solicita datos del usuario de Club Personal.
     * @return
     */
    @GET("/clubpersonal/datos-usuario")
    public RespuestaDatosUsuarioClub solicitarDatosUsuario();

    /**
     * Solicita total de puntos de un socio de Club Personal.

     * @return
     */
    @GET("/clubpersonal/total-puntos")
    public RespuestaTotalPuntosClub solicitarTotalPuntos();



    /**
     * Solicita total de puntos de un socio de Club Personal.

     * @return
     */
    @POST("/clubpersonal/usuarios")
    public Response asociarUsuario();
}
