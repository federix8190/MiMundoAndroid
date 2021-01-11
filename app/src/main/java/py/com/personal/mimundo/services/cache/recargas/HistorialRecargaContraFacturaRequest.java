package py.com.personal.mimundo.services.cache.recargas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.models.DetallesRecargaContraFactura;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 24/09/2014.
 */
public class HistorialRecargaContraFacturaRequest extends BaseRequest {

    LineasInterface service;

    public HistorialRecargaContraFacturaRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public CachedSpiceRequest<List> obtenerLista(final String numeroLinea) {
        String cacheKey = new StringBuilder().append("historial-recarga-contra-factura:").append(numeroLinea).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<DetallesRecargaContraFactura> loadDataFromNetwork() throws Exception {
                return service.obtenerRecargasContraFactura(numeroLinea);
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
