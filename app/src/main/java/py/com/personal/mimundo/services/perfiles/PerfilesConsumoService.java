package py.com.personal.mimundo.services.perfiles;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.lineas.models.Linea;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 05/11/2014.
 */
public class PerfilesConsumoService extends BaseRequest {

    PerfilesConsumoInterface service;

    public PerfilesConsumoService(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(PerfilesConsumoInterface.class);
    }

    public CachedSpiceRequest<List> consultar(final String numeroLinea) {
        String cacheKey = new StringBuilder().append("perfiles-consumo").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<PerfilConsumo> loadDataFromNetwork() throws Exception {
                try {
                    ListaPaginada<PerfilConsumo> lineas = service.obtenerPerfilesConsumo(numeroLinea, -1, "limite");
                    return lineas.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<PerfilConsumo> lineas = service.obtenerPerfilesConsumo(numeroLinea, -1, "limite");
                        return lineas.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public SpiceRequest<Response> aumentarLimiteCredito(final String numeroLinea, final String perfilConsumo) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                Linea linea = new Linea();
                linea.setPerfilConsumo(perfilConsumo);
                return service.aumentarLimiteCredito(numeroLinea, linea);
            }
        };
        return request;
    }
}
