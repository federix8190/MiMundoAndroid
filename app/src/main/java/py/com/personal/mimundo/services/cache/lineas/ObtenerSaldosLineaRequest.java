package py.com.personal.mimundo.services.cache.lineas;

import android.app.Activity;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.models.SaldoPcrf;
import retrofit.converter.Converter;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.lineas.models.Saldos;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;

/**
 * Created by Konecta on 10/09/2014.
 */
public class ObtenerSaldosLineaRequest extends BaseRequest {

    LineasInterface service;

    public ObtenerSaldosLineaRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<Saldos> saldosLinea(final String numeroLinea) {
        //String cacheKey = new StringBuilder().append("saldos:").append(numeroLinea).toString();
        SpiceRequest<Saldos> request = new SpiceRequest<Saldos>(Saldos.class) {
            @Override
            public Saldos loadDataFromNetwork() {
                try {
                    System.err.println("saldosLinea : " + numeroLinea);
                    Saldos s = service.obtenerSaldos(numeroLinea);
                    System.err.println("saldosLinea : " + s);
                    return s;
                } catch (Exception e) {
                    System.err.println("saldosLinea : " + e.getMessage());
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerSaldos(numeroLinea);
                    }
                    return null;
                }
            }
        };
        //return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
        return request;
    }

    public CachedSpiceRequest<SaldoPcrf> saldoPcrf(final String numeroLinea) {
        String cacheKey = new StringBuilder().append("saldos-pcrf:").append(numeroLinea).toString();
        SpiceRequest<SaldoPcrf> request = new SpiceRequest<SaldoPcrf>(SaldoPcrf.class) {
            @Override
            public SaldoPcrf loadDataFromNetwork() {
                try {
                    return service.obtenerSaldoPcrf(numeroLinea);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerSaldoPcrf(numeroLinea);
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

}
