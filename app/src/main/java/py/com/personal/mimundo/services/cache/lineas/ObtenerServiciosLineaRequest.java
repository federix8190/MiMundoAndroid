package py.com.personal.mimundo.services.cache.lineas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.lineas.models.Servicio;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 09/09/2014.
 */
public class ObtenerServiciosLineaRequest extends BaseRequest {

    LineasInterface service;

    public ObtenerServiciosLineaRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public SpiceRequest<List> serviciosLinea(final String numeroLinea) {
        //String cacheKey = new StringBuilder().append("servicios:").append(numeroLinea).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Servicio> loadDataFromNetwork() {
                try {
                    ListaPaginada<Servicio> lista = service.obtenerServicios(numeroLinea, -1);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Servicio> lista = service.obtenerServicios(numeroLinea, -1);
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
