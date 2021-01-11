package py.com.personal.mimundo.services.cache.financiaciones;

import android.app.Activity;

import java.util.List;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import retrofit.converter.Converter;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.financiaciones.FinanciacionesInterface;
import py.com.personal.mimundo.services.financiaciones.models.Financiacion;

/**
 * Created by Konecta on 10/09/2014.
 */
public class FinanciacionesRequest extends BaseRequest {

    FinanciacionesInterface service;

    public FinanciacionesRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(FinanciacionesInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<List> financiaciones() {
        String cacheKey = new StringBuilder().append("financiaciones").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Financiacion> loadDataFromNetwork() throws Exception {
                ListaPaginada<Financiacion> financiaciones = service.obtenerFinanciaciones(10);
                return financiaciones.getLista();
            }
        };
        return request;
        //return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

}
