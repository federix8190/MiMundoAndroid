package py.com.personal.mimundo.services.grupos.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.grupos.models.Direccion;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 01/10/2014.
 */
public class ModificarDireccionGrupo extends BaseRequest {

    GruposFacturacionInterface service;

    public ModificarDireccionGrupo(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(GruposFacturacionInterface.class);
    }

    public SpiceRequest<Response> ejecutar(final String codigoGrupo, final Direccion direccionActual) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                Response res = service.actualizarDireccion(codigoGrupo, direccionActual.getId(), direccionActual);
                return res;
            }
        };
        return request;
    }
}
