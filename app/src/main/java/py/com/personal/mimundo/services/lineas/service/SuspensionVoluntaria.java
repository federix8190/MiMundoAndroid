package py.com.personal.mimundo.services.lineas.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 13/10/2014.
 */
public class SuspensionVoluntaria extends BaseRequest {

    LineasInterface service;

    public SuspensionVoluntaria(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public SpiceRequest<Response> suspender(final String numeroLinea) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.suspensionVoluntaria(numeroLinea);
            }
        };
        return request;
    }

    public SpiceRequest<Response> restituir(final String numeroLinea) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.restitucionVoluntaria(numeroLinea);
            }
        };
        return request;
    }
}
