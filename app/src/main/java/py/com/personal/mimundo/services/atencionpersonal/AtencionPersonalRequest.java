package py.com.personal.mimundo.services.atencionpersonal;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 30/09/2015.
 */
public class AtencionPersonalRequest extends BaseRequest {

    AtencionPersonalInterface service;

    public AtencionPersonalRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(AtencionPersonalInterface.class);
    }

    public CachedSpiceRequest<List> obtenerTiposReclamo() {
        String cacheKey = new StringBuilder().append("tipos-reclamos:").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<TipoReclamo> loadDataFromNetwork() {
                try {
                    ListaPaginada<TipoReclamo> lista = service.obtenerTiposReclamo(-1);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<TipoReclamo> lista = service.obtenerTiposReclamo(-1);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public SpiceRequest<Response> enviarReclamo(final Reclamo datos) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                Response res = service.enviarReclamo(datos);
                return res;
            }
        };
        return request;
    }

    public SpiceRequest<Response> enviarSugerencia(final Sugerencia datos) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                Response res = service.enviarSugerencia(datos);
                return res;
            }
        };
        return request;
    }
}
