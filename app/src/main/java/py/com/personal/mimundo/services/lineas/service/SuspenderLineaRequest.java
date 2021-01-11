package py.com.personal.mimundo.services.lineas.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.disenhos.Constantes;
import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.models.OpcionSuspension;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 13/10/2014.
 */
public class SuspenderLineaRequest extends BaseRequest {

    LineasInterface service;

    public SuspenderLineaRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public SpiceRequest<Response> ejecutar(final String numeroLinea, final String tipo, final OpcionSuspension opcion) {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                try {
                    return suspenderLinea(numeroLinea, tipo, opcion);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return suspenderLinea(numeroLinea, tipo, opcion);
                    }
                    return null;
                }
            }
        };
        return request;
    }

    private Response suspenderLinea(String numeroLinea, String tipo, OpcionSuspension opcion) {
        if (tipo.equals(Constantes.SUSPENDER_LINEA_SINIESTRO)) {
            return service.suspensionPorSiniestro(numeroLinea, opcion);
        } else if (tipo.equals(Constantes.SUSPENDER_LINEA_ROBO)) {
            return service.suspensionPorRobo(numeroLinea, opcion);
        } else {
            return null;
        }
    }
}
