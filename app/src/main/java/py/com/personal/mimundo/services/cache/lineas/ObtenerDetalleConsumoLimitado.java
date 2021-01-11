package py.com.personal.mimundo.services.cache.lineas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.models.DetallesConsumo;
import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;


public class ObtenerDetalleConsumoLimitado extends BaseRequest {

    LineasInterface service;

    public ObtenerDetalleConsumoLimitado(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<List> obtenerDetalleConsumoLimitado(final String numeroLinea) {
        //String cacheKey = new StringBuilder().append("detalleConsumoLimitado:").append(numeroLinea).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<DetallesConsumo> loadDataFromNetwork() {
                try {
                    ListaPaginada<DetallesConsumo> lista = service.obtenerDetalleConsumoLimitado(numeroLinea);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<DetallesConsumo> lista = service.obtenerDetalleConsumoLimitado(numeroLinea);
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

