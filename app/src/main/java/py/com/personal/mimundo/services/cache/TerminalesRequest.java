package py.com.personal.mimundo.services.cache;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.terminales.TerminalesInterface;
import py.com.personal.mimundo.services.terminales.models.Marca;
import py.com.personal.mimundo.services.terminales.models.Modelo;
import py.com.personal.mimundo.services.terminales.models.Terminal;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 14/10/2014.
 */
public class TerminalesRequest extends BaseRequest {

    TerminalesInterface service;

    public TerminalesRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(TerminalesInterface.class);
    }

    public CachedSpiceRequest<List> consultar(final String marca, final String modelo, final int registros) {
        String cacheKey = new StringBuilder().append("terminales:").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Terminal> loadDataFromNetwork() {
                try {
                    ListaPaginada<Terminal> lista = service.obtenerTerminales(registros, marca, modelo);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Terminal> lista = service.obtenerTerminales(registros, marca, modelo);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<List> marcas() {
        String cacheKey = new StringBuilder().append("marcas:").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Marca> loadDataFromNetwork() {
                try {
                    ListaPaginada<Marca> lista = service.obtenerMarcas(-1);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Marca> lista = service.obtenerMarcas(-1);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<List> modelos(final String marca) {
        String cacheKey = new StringBuilder().append("modelos:").append(marca).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Modelo> loadDataFromNetwork() {
                try {
                    ListaPaginada<Modelo> lista = service.obtenerModelos(marca, -1);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Modelo> lista = service.obtenerModelos(marca, -1);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
