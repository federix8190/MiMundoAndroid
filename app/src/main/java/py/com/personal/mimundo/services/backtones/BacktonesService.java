package py.com.personal.mimundo.services.backtones;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.backtones.models.Categoria;
import py.com.personal.mimundo.services.backtones.models.ListaTonos;
import py.com.personal.mimundo.services.backtones.models.Tono;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 11/25/2014.
 */
public class BacktonesService extends BaseRequest {

    BacktonesInterface service;

    public BacktonesService(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(BacktonesInterface.class);
    }

    public CachedSpiceRequest<List> tonosMasEscuchados(final int inicio, final int cantidad) {
        String cacheKey = new StringBuilder().append("tonos-mas-escuchados:").append(inicio).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Tono> loadDataFromNetwork() {
                try {
                    ListaTonos lista = service.obtenerTonosMasEscuchados(inicio, cantidad, "tema");
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaTonos lista = service.obtenerTonosMasEscuchados(inicio, cantidad, "tema");
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<List> obtenerCategorias() {
        String cacheKey = new StringBuilder().append("categorias-agrupadas:").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Categoria> loadDataFromNetwork() {
                try {
                    ListaPaginada<Categoria> lista = service.obtenerCategorias(-1);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Categoria> lista = service.obtenerCategorias(-1);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<List> tonosCategoria(final int idcategoria, final int inicio, final int cantidad) {
        String cacheKey = new StringBuilder().append("categorias-tonos:").append(inicio).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Tono> loadDataFromNetwork() {
                try {
                    ListaTonos lista = service.obtenerTonosCategoria(idcategoria, inicio, cantidad, "tema");
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaTonos lista = service.obtenerTonosCategoria(idcategoria, inicio, cantidad, "tema");
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

//    public CachedSpiceRequest<TonoImagen> obtenerImagenDeTono(final Integer idTono) {
//        String cacheKey = new StringBuilder().append("tono-id:").append(idTono).toString();
//        SpiceRequest<TonoImagen> request = new SpiceRequest<TonoImagen>(TonoImagen.class) {
//            @Override
//            public TonoImagen loadDataFromNetwork() throws Exception {
//                return service.obtenerImagenDeTono(idTono);
//            }
//        };
//        return new CachedSpiceRequest<TonoImagen>(request, cacheKey, CACHE_EXPIRE);
//    }
}
