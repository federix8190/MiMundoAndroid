package py.com.personal.mimundo.services.clientes;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.clientes.models.Cliente;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 28/10/2014.
 */
public class ClientesService extends BaseRequest {

    ClientesInterface service;

    public ClientesService(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(ClientesInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public CachedSpiceRequest<List> obtenerClientes() {
        String cacheKey = new StringBuilder().append("lista-clientes").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Cliente> loadDataFromNetwork() throws Exception {
                try {
                    ListaPaginada<Cliente> lineas = service.obtenerClientes(1);
                    return lineas.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Cliente> lineas = service.obtenerClientes(1);
                        return lineas.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

}
