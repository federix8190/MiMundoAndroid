package py.com.personal.mimundo.services.lineas.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.models.SolcitarRecarga;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 24/09/2014.
 */
public class SolicitarRecargaSosRequest extends BaseRequest {

    LineasInterface service;

    public SolicitarRecargaSosRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public SpiceRequest<Response> solicitar(final String numeroLinea, final String monto) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                Response res = service.solicitarRecargaSos(numeroLinea, new SolcitarRecarga(monto));
                return res;
            }
        };
        return request;
    }
}
