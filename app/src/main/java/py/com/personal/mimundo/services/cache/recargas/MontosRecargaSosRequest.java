package py.com.personal.mimundo.services.cache.recargas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.models.MontosRecargaSos;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 24/09/2014.
 */
public class MontosRecargaSosRequest extends BaseRequest {

    LineasInterface service;

    public MontosRecargaSosRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public CachedSpiceRequest<MontosRecargaSos> consultar(final String numeroLinea) {
        String cacheKey = new StringBuilder().append("montos-recarga-contra-factura:").append(numeroLinea).toString();
        SpiceRequest<MontosRecargaSos> request = new SpiceRequest<MontosRecargaSos>(MontosRecargaSos.class) {
            @Override
            public MontosRecargaSos loadDataFromNetwork() {
                try {
                    return service.obtenerMontosRecargasSos(numeroLinea);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerMontosRecargasSos(numeroLinea);
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }
}
