package py.com.personal.mimundo.services.cache.financiaciones;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.financiaciones.FinanciacionesInterface;
import py.com.personal.mimundo.services.financiaciones.models.Cuota;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 23/09/2014.
 */
public class CuotasFinanciacionRequest extends BaseRequest {

    FinanciacionesInterface service;

    public CuotasFinanciacionRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(FinanciacionesInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<List> listarCuotas(final Long numeroCuenta) {
        String cacheKey = new StringBuilder().append("cuotas-financiacion:" + numeroCuenta).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Cuota> loadDataFromNetwork() throws Exception {
                ListaPaginada<Cuota> cuotas = service.obtenerCuotas(numeroCuenta, -1, "numeroCuota;desc");
                return cuotas.getLista();
            }
        };
        return request;
        //return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
