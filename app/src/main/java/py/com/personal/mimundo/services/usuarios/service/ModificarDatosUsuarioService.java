package py.com.personal.mimundo.services.usuarios.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.usuarios.models.DatosUsuario;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 01/10/2014.
 */
public class ModificarDatosUsuarioService extends BaseRequest {

    UsuarioInterface service;

    public ModificarDatosUsuarioService(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(UsuarioInterface.class);
    }

    public SpiceRequest<Response> ejecutar(final String user, final DatosUsuario datosUsuario) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                try {
                    Response res = service.modificarDatosUsuario(user, datosUsuario);
                    return res;
                } catch (Exception e) {
                    throw e;
                }
            }
        };
        return request;
    }
}
