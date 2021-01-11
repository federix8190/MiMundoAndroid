package py.com.personal.mimundo.services.cache.recargas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.models.RecargaSos;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 24/09/2014.
 */
public class HistorialRecargaSosRequest extends BaseRequest {

    LineasInterface service;

    public HistorialRecargaSosRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public CachedSpiceRequest<RecargaSos> consultar(final String numeroLinea) {
        String cacheKey = new StringBuilder().append("historial-recarga-sos:").append(numeroLinea).toString();
        SpiceRequest<RecargaSos> request = new SpiceRequest<RecargaSos>(RecargaSos.class) {
            @Override
            public RecargaSos loadDataFromNetwork() throws Exception {
                try {
                    return service.obtenerRecargasSos(numeroLinea);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerRecargasSos(numeroLinea);
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }
}
