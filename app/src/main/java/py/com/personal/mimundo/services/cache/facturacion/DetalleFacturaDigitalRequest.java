package py.com.personal.mimundo.services.cache.facturacion;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.grupos.models.DetalleFactura;
import py.com.personal.mimundo.services.grupos.service.GruposFacturacionInterface;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 01/10/2014.
 */
public class DetalleFacturaDigitalRequest extends BaseRequest {

    GruposFacturacionInterface service;

    public DetalleFacturaDigitalRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(GruposFacturacionInterface.class);
    }

    public SpiceRequest<Response> consultar(final String codigoGrupo,
                                                    final String codigoDocumento,
                                                    final Long numeroFactura) {
        String clave = "detalles-factura-digital:" + codigoGrupo + codigoDocumento + numeroFactura;
        String cacheKey = new StringBuilder().append(clave).toString();
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                System.err.println("exportar factura");
                Response res = service.obtenerDetalleFacturaDigital(codigoGrupo, codigoDocumento, numeroFactura);
                System.err.println("exportar factura : " + res.getStatus());
                System.err.println("exportar factura : " + res.getBody().length());
                return res;
            }
        };
        //return new CachedSpiceRequest<Response>(request, cacheKey, CACHE_EXPIRE);
        return request;
    }
}
