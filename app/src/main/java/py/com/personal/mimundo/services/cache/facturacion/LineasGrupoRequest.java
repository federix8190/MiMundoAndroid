package py.com.personal.mimundo.services.cache.facturacion;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.grupos.service.GruposFacturacionInterface;
import py.com.personal.mimundo.services.lineas.models.Linea;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 11/09/2014.
 */
public class LineasGrupoRequest extends BaseRequest {

    GruposFacturacionInterface service;

    public LineasGrupoRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(GruposFacturacionInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<List> lineasGrupo(final String codigoGrupo) {
        //String cacheKey = new StringBuilder().append("lineas-grupo").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Linea> loadDataFromNetwork() throws Exception {
                ListaPaginada<Linea> lineas = service.obtenerLineas(codigoGrupo);
                return lineas.getLista();
            }
        };
        return request;
        //return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
