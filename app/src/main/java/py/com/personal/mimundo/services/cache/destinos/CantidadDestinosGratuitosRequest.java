package py.com.personal.mimundo.services.cache.destinos;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.destinos.service.DestinosGratuitosInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 07/10/2014.
 */
public class CantidadDestinosGratuitosRequest extends BaseRequest {

    DestinosGratuitosInterface service;

    public CantidadDestinosGratuitosRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(DestinosGratuitosInterface.class);
    }

    public SpiceRequest<Integer> consultar(final String numeroLinea, final String codigoServicio, final String codigoGrupo) {
        String cacheKey = new StringBuilder().append("cantidad-destinos-gratuitos:")
                                             .append(numeroLinea)
                                             .append(codigoServicio)
                                             .append(codigoGrupo).toString();
        SpiceRequest<Integer> request = new SpiceRequest<Integer>(Integer.class) {
            @Override
            public Integer loadDataFromNetwork() {
                try {
                    return service.obtenerCantidad(numeroLinea, codigoServicio, codigoGrupo);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerCantidad(numeroLinea, codigoServicio, codigoGrupo);
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<Integer>(request, cacheKey, CACHE_EXPIRE);
    }
}
