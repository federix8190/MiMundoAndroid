package py.com.personal.mimundo.services.cache.facturacion;

import android.app.Activity;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import retrofit.converter.Converter;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.grupos.models.Factura;
import py.com.personal.mimundo.services.grupos.service.GruposFacturacionInterface;

/**
 * Created by Konecta on 10/09/2014.
 */
public class FacturasGrupoRequest extends BaseRequest {

    GruposFacturacionInterface service;

    public FacturasGrupoRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(GruposFacturacionInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public CachedSpiceRequest<List> facturasGrupo(final String codigoGrupo) {
        String cacheKey = new StringBuilder().append("facturas-grupo:" + codigoGrupo).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Factura> loadDataFromNetwork() throws Exception {
                System.err.println("Listar Facturas : " + codigoGrupo);
                ListaPaginada<Factura> facturas = service.obtenerFacturas(codigoGrupo, 10);
                System.err.println("Listar Facturas : " + facturas.getLista().size());
                return facturas.getLista();
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

    public SpiceRequest<ListaPaginada> listar(final String codigoGrupo, final int inicio, final int registros) {
        String cacheKey = new StringBuilder().append("facturas-grupo:" + codigoGrupo).toString();
        SpiceRequest<ListaPaginada> request = new SpiceRequest<ListaPaginada>(ListaPaginada.class) {
            @Override
            public ListaPaginada<Factura> loadDataFromNetwork() throws Exception {
                System.err.println("Listar Facturas : " + codigoGrupo);
                ListaPaginada<Factura> facturas = service.listar(codigoGrupo, registros, inicio);
                System.err.println("Listar Facturas : " + facturas.getLista().size());
                return facturas;
            }
        };
        return request;
        //return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

}
