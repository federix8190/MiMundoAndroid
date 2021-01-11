package py.com.personal.mimundo.services.cache.lineas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.packs.RespuestaListaArbolPack;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 16/09/2014.
 */
public class ObtenerPacksLineaRequest extends BaseRequest {

    LineasInterface service;

    public ObtenerPacksLineaRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<RespuestaListaArbolPack> packsLinea(final String numeroLinea, final String canal) {
        //String cacheKey = new StringBuilder().append("packs-linea:").append(numeroLinea).toString();
        SpiceRequest<RespuestaListaArbolPack> request = new SpiceRequest<RespuestaListaArbolPack>(RespuestaListaArbolPack.class) {
            @Override
            public RespuestaListaArbolPack loadDataFromNetwork() throws Exception {
                try {
                    return service.obtenerPacks(numeroLinea, canal);
                } catch (Exception e) {
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<RespuestaListaArbolPack>(request, cacheKey, CACHE_EXPIRE);
    }
}
