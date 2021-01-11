package py.com.personal.mimundo.services.lineas.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.models.ParamPedido;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 01/10/2014.
 */
public class PedirSaldoRequest extends BaseRequest {

    LineasInterface service;

    public PedirSaldoRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public SpiceRequest<Response> ejecutar(final String numeroLinea, final String numeroDestino) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                ParamPedido parametroLinea = new ParamPedido(numeroDestino);
                Response res = service.pedirSaldo(numeroLinea, parametroLinea);
                return res;
            }
        };
        return request;
    }
}
