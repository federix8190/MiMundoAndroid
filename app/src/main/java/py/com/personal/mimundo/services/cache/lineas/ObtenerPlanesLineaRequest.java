package py.com.personal.mimundo.services.cache.lineas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.lineas.models.Plan;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 09/09/2014.
 */
public class ObtenerPlanesLineaRequest extends BaseRequest {

    LineasInterface service;

    public ObtenerPlanesLineaRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public SpiceRequest<List> planesLinea(final String numeroLinea) {
        //String cacheKey = new StringBuilder().append("planes:").append(numeroLinea).toString();
        System.err.println("planesLinea");
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Plan> loadDataFromNetwork() {
                try {
                    ListaPaginada<Plan> lista = service.obtenerPlanes(numeroLinea, -1);
                    System.err.println("planesLinea : " + lista.getLista().size());
                    return lista.getLista();
                } catch (Exception e) {
                    System.err.println("planesLinea : " + e.getMessage());
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Plan> lista = service.obtenerPlanes(numeroLinea, -1);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

}
