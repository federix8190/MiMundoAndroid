package py.com.personal.mimundo.services.cache.facturacion;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.grupos.models.GrupoFacturacion;
import py.com.personal.mimundo.services.grupos.service.GruposFacturacionInterface;
import retrofit.converter.Converter;

public class ObtenerDatosGrupoRequest extends BaseRequest {

    GruposFacturacionInterface service;

    public ObtenerDatosGrupoRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(GruposFacturacionInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<GrupoFacturacion> get(final String codigo) {
        String cacheKey = new StringBuilder().append("grupos-facturacion").toString();
        SpiceRequest<GrupoFacturacion> request = new SpiceRequest<GrupoFacturacion>(GrupoFacturacion.class) {
            @Override
            public GrupoFacturacion loadDataFromNetwork() throws Exception {
                try {
                    GrupoFacturacion grupo = service.obtenerGrupo(codigo);
                    return grupo;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        GrupoFacturacion grupo = service.obtenerGrupo(codigo);
                        return grupo;
                    }
                    return null;
                }
            }
        };
        return request;
    }

}
