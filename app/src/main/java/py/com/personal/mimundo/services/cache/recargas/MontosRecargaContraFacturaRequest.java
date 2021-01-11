package py.com.personal.mimundo.services.cache.recargas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.lineas.models.ItemOpcionesRecargaContraFactura;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 24/09/2014.
 */
public class MontosRecargaContraFacturaRequest extends BaseRequest {

    LineasInterface service;

    public MontosRecargaContraFacturaRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public CachedSpiceRequest<List> obtenerLista(final String numeroLinea) {
        String cacheKey = new StringBuilder().append("montos-recarga-contra-factura:").append(numeroLinea).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<ItemOpcionesRecargaContraFactura> loadDataFromNetwork() throws Exception {
                ListaPaginada<ItemOpcionesRecargaContraFactura> montos = service.montosRecargasContraFactura(numeroLinea);
                return montos.getLista();
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
