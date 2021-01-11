package py.com.personal.mimundo.services.cache.facturacion;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.grupos.models.DetalleFactura;
import py.com.personal.mimundo.services.grupos.service.GruposFacturacionInterface;
import retrofit.converter.Converter;

/**
 * Created by Usuario on 9/17/2014.
 */
public class DetallesFactura extends BaseRequest {

    GruposFacturacionInterface service;

    public DetallesFactura(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(GruposFacturacionInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<List> obtenerDetallesDeFactura(final String codigoGrupo,
                                                  final String codigoDocumento,
                                                  final Long numeroFactura) {
        String clave = "detalles-factura:" + codigoGrupo + codigoDocumento + numeroFactura;
        String cacheKey = new StringBuilder().append(clave).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<DetalleFactura> loadDataFromNetwork() throws Exception {
                ListaPaginada<DetalleFactura> detalles = service.obtenerDetalleFactura(codigoGrupo,
                        codigoDocumento, numeroFactura, "numeroLinea", -1);
                return detalles.getLista();
            }
        };
        return request;
        //return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
