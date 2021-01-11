package py.com.personal.mimundo.services.lineas.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.packs.AcreditarPack;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 18/09/2014.
 */
public class AcreditarPacksRequest extends BaseRequest {

    LineasInterface service;

    public AcreditarPacksRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public SpiceRequest<Response> acreditarPacks(final String numeroLinea, final AcreditarPack datos) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                Response res = service.acreditarPacks(numeroLinea, datos);
                return res;
            }
        };
        return request;
    }
}
