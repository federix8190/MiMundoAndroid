package py.com.personal.mimundo.services.cache.facturacion;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.grupos.models.Direccion;
import py.com.personal.mimundo.services.grupos.service.GruposFacturacionInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 01/10/2014.
 */
public class DireccionesGrupoFacturacionRequest extends BaseRequest {

    GruposFacturacionInterface service;

    public DireccionesGrupoFacturacionRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(GruposFacturacionInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public CachedSpiceRequest<List> consultar(final String codigoGrupo) {
        String cacheKey = new StringBuilder().append("direcciones-grupo:" + codigoGrupo).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Direccion> loadDataFromNetwork() throws Exception {
                ListaPaginada<Direccion> lista = service.obtenerDirecciones(codigoGrupo, -1);
                return lista.getLista();
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
