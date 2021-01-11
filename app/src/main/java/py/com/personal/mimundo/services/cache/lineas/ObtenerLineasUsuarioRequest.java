package py.com.personal.mimundo.services.cache.lineas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 01/10/2014.
 */
public class ObtenerLineasUsuarioRequest extends BaseRequest {

    LineasInterface service;
    String codigoOperacion;

    public ObtenerLineasUsuarioRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public ObtenerLineasUsuarioRequest(Activity context, Converter converter, String codigoOperacion) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
        this.codigoOperacion = codigoOperacion;
    }

    public SpiceRequest<List> listar() {
        String cacheKey;
        if (codigoOperacion != null) {
            cacheKey = new StringBuilder().append("lineas:").append(codigoOperacion).toString();
        } else {
            cacheKey = new StringBuilder().append("lineas:").toString();
        }
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Linea> loadDataFromNetwork() {
                try {
                    ListaPaginada<Linea> lista;
                    if (codigoOperacion == null) {
                        lista = service.obtenerLineas(-1);
                    } else {
                        lista = service.obtenerLineas(-1, codigoOperacion);
                    }
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Linea> lista;
                        if (codigoOperacion == null) {
                            lista = service.obtenerLineas(-1);
                        } else {
                            lista = service.obtenerLineas(-1, codigoOperacion);
                        }
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        //return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
        return request;
    }

    public SpiceRequest<ListaPaginada> getListaPaginada() {

        SpiceRequest<ListaPaginada> request = new SpiceRequest<ListaPaginada>(ListaPaginada.class) {
            @Override
            public ListaPaginada<Linea> loadDataFromNetwork() {
                try {
                    ListaPaginada<Linea> lista;
                    if (codigoOperacion == null) {
                        lista = service.obtenerLineas(-1);
                    } else {
                        lista = service.obtenerLineas(-1, codigoOperacion);
                    }
                    return lista;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Linea> lista;
                        if (codigoOperacion == null) {
                            lista = service.obtenerLineas(-1);
                        } else {
                            lista = service.obtenerLineas(-1, codigoOperacion);
                        }
                        return lista;
                    }
                    return null;
                }
            }
        };
        return request;
    }

    public SpiceRequest<Linea> datosLinea(final String numeroLinea) {
        SpiceRequest<Linea> request = new SpiceRequest<Linea>(Linea.class) {
            @Override
            public Linea loadDataFromNetwork() {
                try {
                    Linea l = service.obtenerLinea(numeroLinea);
                    return l;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerLinea(numeroLinea);
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<Linea>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<ListaPaginada> obtenerLineas() {
        String cacheKey;
        if (codigoOperacion != null) {
            cacheKey = new StringBuilder().append("lineas:").append(codigoOperacion).toString();
        } else {
            cacheKey = new StringBuilder().append("lineas:").toString();
        }
        SpiceRequest<ListaPaginada> request = new SpiceRequest<ListaPaginada>(ListaPaginada.class) {
            @Override
            public ListaPaginada<Linea> loadDataFromNetwork() {
                try {
                    ListaPaginada<Linea> lista;
                    lista = service.obtenerLineasDelUsuario(-1, "tipoLinea");
                    return lista;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Linea> lista;
                        if (codigoOperacion == null) {
                            lista = service.obtenerLineasDelUsuario(-1, "tipoLinea");
                        } else {
                            lista = service.obtenerLineasDelUsuario(-1, "tipoLinea");
                        }
                        return lista;
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<ListaPaginada>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<List> listarLimiteConsumo() {
        String cacheKey = new StringBuilder().append("lineas:").append(codigoOperacion).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Linea> loadDataFromNetwork() {
                ListaPaginada<Linea> lista;
                try {
                    lista = service.obtenerLineasLimiteConsumo(-1, codigoOperacion, "POSPA");
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        lista = service.obtenerLineasLimiteConsumo(-1, codigoOperacion, "POSPA");
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }
}
