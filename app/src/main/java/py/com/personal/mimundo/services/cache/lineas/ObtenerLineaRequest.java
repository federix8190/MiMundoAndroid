package py.com.personal.mimundo.services.cache.lineas;

import android.app.Activity;

import py.com.personal.mimundo.services.BaseRequest;
import retrofit.converter.Converter;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;

/**
 * Created by Konecta on 09/09/2014.
 */
public class ObtenerLineaRequest extends BaseRequest {

    private LineasInterface service;

    public ObtenerLineaRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<Linea> datosLinea(final String numeroLinea) {
        String cacheKey = new StringBuilder().append("linea:").append(numeroLinea).toString();
        SpiceRequest<Linea> request = new SpiceRequest<Linea>(Linea.class) {
            @Override
            public Linea loadDataFromNetwork() {
                try {
                    Linea l = service.obtenerLinea(numeroLinea);
                    return l;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerLinea(numeroLinea);
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<Linea>(request, cacheKey, CACHE_EXPIRE);
    }

}
