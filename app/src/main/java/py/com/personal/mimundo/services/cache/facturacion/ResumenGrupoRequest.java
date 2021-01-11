package py.com.personal.mimundo.services.cache.facturacion;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.grupos.models.ResumenGrupo;
import py.com.personal.mimundo.services.grupos.service.GruposFacturacionInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 11/09/2014.
 */
public class ResumenGrupoRequest extends BaseRequest {

    GruposFacturacionInterface service;

    public ResumenGrupoRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(GruposFacturacionInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<ResumenGrupo> resumenGrupo(final String codigoGrupo) {
        String cacheKey = new StringBuilder().append("resumen-grupo").toString();
        SpiceRequest<ResumenGrupo> request = new SpiceRequest<ResumenGrupo>(ResumenGrupo.class) {
            @Override
            public ResumenGrupo loadDataFromNetwork() throws Exception {
                return service.obtenerResumen(codigoGrupo);
            }
        };
        //return new CachedSpiceRequest<ResumenGrupo>(request, cacheKey, CACHE_EXPIRE);
        return request;
    }
}
