package py.com.personal.mimundo.services.cache.lineas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.consumo.HistorialesConsumo;
import py.com.personal.mimundo.services.lineas.models.Saldos;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 26/08/2015.
 */
public class HistorialConsumoLinea extends BaseRequest {

    LineasInterface service;

    public HistorialConsumoLinea(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<HistorialesConsumo> obtenerHistorial(final String numeroLinea) {
        //String cacheKey = new StringBuilder().append("historial-consumo:").append(numeroLinea).toString();
        SpiceRequest<HistorialesConsumo> request = new SpiceRequest<HistorialesConsumo>(HistorialesConsumo.class) {
            @Override
            public HistorialesConsumo loadDataFromNetwork() {
                try {
                    return service.obtenerHistorialConsumo(numeroLinea);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerHistorialConsumo(numeroLinea);
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<HistorialesConsumo>(request, cacheKey, CACHE_EXPIRE);
    }
}
