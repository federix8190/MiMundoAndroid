package py.com.personal.mimundo.services.cache.facturacion;

import android.app.Activity;

import py.com.personal.mimundo.services.BaseRequest;
import retrofit.converter.Converter;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.grupos.models.GrupoFacturacion;
import py.com.personal.mimundo.services.grupos.service.GruposFacturacionInterface;

/**
 * Created by Konecta on 10/09/2014.
 */
public class ObtenerGruposFacturacionRequest extends BaseRequest {

    GruposFacturacionInterface service;

    public ObtenerGruposFacturacionRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(GruposFacturacionInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<List> gruposFacturacion() {
        String cacheKey = new StringBuilder().append("grupos-facturacion").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<GrupoFacturacion> loadDataFromNetwork() throws Exception {
                try {
                    ListaPaginada<GrupoFacturacion> grupos = service.obtenerGrupos(-1);
                    return grupos.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<GrupoFacturacion> grupos = service.obtenerGrupos(-1);
                        return grupos.getLista();
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public SpiceRequest<ListaPaginada> listar(final int inicio, final int registros) {
        String cacheKey = new StringBuilder().append("grupos-facturacion").toString();
        SpiceRequest<ListaPaginada> request = new SpiceRequest<ListaPaginada>(ListaPaginada.class) {
            @Override
            public ListaPaginada<GrupoFacturacion> loadDataFromNetwork() throws Exception {
                try {
                    ListaPaginada<GrupoFacturacion> grupos = service.getLista(registros, inicio);
                    return grupos;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<GrupoFacturacion> grupos = service.getLista(registros, inicio);
                        return grupos;
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public SpiceRequest<List> gruposFacturacion(final String codigo) {
        String cacheKey = new StringBuilder().append("grupos-facturacion").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<GrupoFacturacion> loadDataFromNetwork() throws Exception {
                try {
                    ListaPaginada<GrupoFacturacion> grupos = service.obtenerGruposByCodigo(codigo);
                    return grupos.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<GrupoFacturacion> grupos = service.obtenerGruposByCodigo(codigo);
                        return grupos.getLista();
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

}
