package py.com.personal.mimundo.services;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;
import com.squareup.okhttp.Response;

import java.util.List;

import py.com.personal.mimundo.services.clubpersonal.models.RespuestaDatosUsuarioClub;
import py.com.personal.mimundo.services.clubpersonal.models.RespuestaTotalPuntosClub;
import py.com.personal.mimundo.services.clubpersonal.service.ClubPersonalInterface;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaCorp;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaInd;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoCorp;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoInd;
import py.com.personal.mimundo.services.transferencias.service.TransferenciaInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 24/09/2014.
 */
public class ClubPersonalRequest extends BaseRequest {

    ClubPersonalInterface service;

    public ClubPersonalRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(ClubPersonalInterface.class);
    }

    public SpiceRequest<RespuestaDatosUsuarioClub> solicitarDatosUsuarioClub() throws Exception {
        SpiceRequest<RespuestaDatosUsuarioClub> request = new SpiceRequest<RespuestaDatosUsuarioClub>(RespuestaDatosUsuarioClub.class) {
            @Override
            public RespuestaDatosUsuarioClub loadDataFromNetwork() throws Exception {
                return service.solicitarDatosUsuario();
            }
        };
        return request;
    }

    public SpiceRequest<RespuestaTotalPuntosClub> solicitarTotalPuntosClub() throws Exception {
        SpiceRequest<RespuestaTotalPuntosClub> request = new SpiceRequest<RespuestaTotalPuntosClub>(RespuestaTotalPuntosClub.class) {
            @Override
            public RespuestaTotalPuntosClub loadDataFromNetwork() throws Exception {
                return service.solicitarTotalPuntos();
            }
        };
        return request;
    }


    public SpiceRequest<Response> asociarUsuario() throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.asociarUsuario();
            }
        };
        return request;
    }

}
