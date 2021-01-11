package py.com.personal.mimundo.services.backtones;

import android.app.Activity;

import com.google.gson.Gson;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ErrorResponse;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.backtones.models.GrupoLineas;
import py.com.personal.mimundo.services.backtones.models.LineaGrupo;
import py.com.personal.mimundo.services.backtones.models.ListaGruposLinea;
import py.com.personal.mimundo.services.backtones.models.ListaLineaGrupo;
import py.com.personal.mimundo.services.backtones.models.ListaTonos;
import py.com.personal.mimundo.services.backtones.models.Tono;
import py.com.personal.mimundo.services.backtones.models.TonoConfiguracion;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.mime.TypedByteArray;

/**
 * Created by Konecta on 11/25/2014.
 */
public class LineaBacktonesService extends BaseRequest {

    LineaBacktonesInterface service;

    public LineaBacktonesService(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineaBacktonesInterface.class);
    }

    public CachedSpiceRequest<List> obtenerTonosLinea(final String linea, final int inicio, final int cantidad) {
        String cacheKey = new StringBuilder().append("tonos-linea:").append(inicio).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Tono> loadDataFromNetwork() {
                try {
                    ListaTonos lista = service.obtenerTonosLinea(linea, inicio, cantidad, "tema");
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaTonos lista = service.obtenerTonosLinea(linea, inicio, cantidad, "tema");
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public SpiceRequest<Resultado> comprarBacktone(final String numeroLinea, final Integer idTono) {
        SpiceRequest<Resultado> request = new SpiceRequest<Resultado>(Resultado.class) {
            @Override
            public Resultado loadDataFromNetwork() throws Exception {
                try {
                    Response res = service.comprarBacktone(numeroLinea, idTono);
                    if (res != null && res.getStatus() == 200) {
                        return new Resultado(true, null);
                    } else {
                        return new Resultado(false, MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
                    }
                } catch (RetrofitError e) {
                    return procesarError(e);
                }
            }
        };
        return request;
    }

    public SpiceRequest<Response> borrarBacktone(final String numeroLinea, final Integer idTono) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.borrarBacktone(numeroLinea, idTono);
            }
        };
        return request;
    }

    public SpiceRequest<Response> obtenerDatosCompra(final String numeroLinea, final Integer idTono) {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() {
                return service.obtenerDatosCompra(numeroLinea, idTono);
            }
        };
        return request;
    }

    public SpiceRequest<TonoConfiguracion> obtenerConfiguracionTono(final String numeroLinea, final Integer idTono) {
        SpiceRequest<TonoConfiguracion> request = new SpiceRequest<TonoConfiguracion>(TonoConfiguracion.class) {
            @Override
            public TonoConfiguracion loadDataFromNetwork() {
                    return service.obtenerConfiguracionTono(numeroLinea, idTono);
            }
        };
        return request;
    }

    public SpiceRequest<Response> modificarConfiguracionTono(final String numeroLinea, final Integer idTono,
                                                             final TonoConfiguracion tonoConfiguracion) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.modificarConfiguracionTono(numeroLinea, idTono, tonoConfiguracion);
            }
        };
        return request;
    }

    public CachedSpiceRequest<List> obtenerLineasAsignadas(final String linea, final int idTono,
                                                           final int inicio, final int cantidad) {
        String cacheKey = new StringBuilder().append("lineas-asignadas:").append(inicio).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<LineaGrupo> loadDataFromNetwork() {
                try {
                    ListaLineaGrupo lista = service.obtenerLineasAsignadas(linea, idTono, inicio, cantidad);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaLineaGrupo lista = service.obtenerLineasAsignadas(linea, idTono, inicio, cantidad);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public SpiceRequest<Response> asignarLineaTono(final String numeroLinea, final Integer idTono,
                                                   final String numeroLineaAsignada) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.asignarLineaTono(numeroLinea, idTono, numeroLineaAsignada);
            }
        };
        return request;
    }

    public SpiceRequest<Response> sacarLineaAsignada(final String numeroLinea, final Integer idTono,
                                                     final String numeroLineaAsignada) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.sacarLineaAsignada(numeroLinea, idTono, numeroLineaAsignada);
            }
        };
        return request;
    }

    public CachedSpiceRequest<List> obtenerGruposAsignados(final String linea, final int idTono,
                                                           final int inicio, final int cantidad) {
        String cacheKey = new StringBuilder().append("grupos-asignados:").append(inicio).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<GrupoLineas> loadDataFromNetwork() {
                try {
                    ListaGruposLinea lista = service.obtenerGruposAsignados(linea, idTono, inicio, cantidad);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaGruposLinea lista = service.obtenerGruposAsignados(linea, idTono, inicio, cantidad);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public SpiceRequest<Response> asignarGrupoTono(final String numeroLinea, final Integer idTono,
                                                   final Integer idGrupo) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.asignarGrupoTono(numeroLinea, idTono, idGrupo);
            }
        };
        return request;
    }

    public SpiceRequest<Response> desasignarGrupoTono(final String numeroLinea, final Integer idTono,
                                                      final Integer idGrupo) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.desasignarGrupoTono(numeroLinea, idTono, idGrupo);
            }
        };
        return request;
    }

    public CachedSpiceRequest<List> obtenerGruposLinea(final String numeroLinea, final int inicio, final int cantidad) {
        String cacheKey = new StringBuilder().append("grupos-lineas:").append(inicio).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<GrupoLineas> loadDataFromNetwork() {
                try {
                    ListaGruposLinea lista = service.obtenerGruposLinea(numeroLinea, inicio, cantidad, "descripcion");
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaGruposLinea lista = service.obtenerGruposLinea(numeroLinea, inicio, cantidad, "descripcion");
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<Response> crearGrupoLinea(final String numeroLinea, final String descripcion) {
        String cacheKey = new StringBuilder().append("crear-grupos-lineas").toString();
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() {
                GrupoLineas grupoNuevo = new GrupoLineas();
                grupoNuevo.setDescripcion(descripcion);
                try {
                    Response response = service.crearGrupoLinea(numeroLinea, grupoNuevo);
                    return response;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        Response response = service.crearGrupoLinea(numeroLinea, grupoNuevo);
                        return response;
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<List> obtenerLineasGrupo(final String numeroLinea, final int grupoId,
                                                       final int inicio, final int cantidad) {
        String cacheKey = new StringBuilder().append("linea-grupo:").append(inicio).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<LineaGrupo> loadDataFromNetwork() {
                try {
                    ListaLineaGrupo lista = service.obtenerLineasGrupo(numeroLinea, grupoId, inicio, cantidad);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaLineaGrupo lista = service.obtenerLineasGrupo(numeroLinea, grupoId, inicio, cantidad);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<Response> asignarLineaGrupo(final String numeroLinea, final int idGrupo,
                                                          final String lineaAsignada) {
        String cacheKey = new StringBuilder().append("asiganar-linea-grupo").toString();
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() {
                try {
                    Response response = service.asignarLineaGrupo(numeroLinea, idGrupo, lineaAsignada);
                    return response;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        Response response = service.asignarLineaGrupo(numeroLinea, idGrupo, lineaAsignada);
                        return response;
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<Response> desasignarLineaGrupo(final String numeroLinea, final int idGrupo,
                                                             final String lineaDesasignada) {
        String cacheKey = new StringBuilder().append("desasiganar-linea-grupo").toString();
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() {
                try {
                    Response response = service.desasignarLineaGrupo(numeroLinea, idGrupo, lineaDesasignada);
                    return response;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        Response response = service.desasignarLineaGrupo(numeroLinea, idGrupo, lineaDesasignada);
                        return response;
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<>(request, cacheKey, CACHE_EXPIRE);
    }

}
