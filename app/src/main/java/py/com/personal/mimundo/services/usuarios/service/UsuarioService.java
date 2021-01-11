package py.com.personal.mimundo.services.usuarios.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.usuarios.models.DatosUsuario;
import py.com.personal.mimundo.services.usuarios.models.DatosUsuarioLogueado;
import py.com.personal.mimundo.services.usuarios.models.ModificarClave;
import py.com.personal.mimundo.services.usuarios.models.Rol;
import py.com.personal.mimundo.services.usuarios.models.Usuario;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 10/09/2014.
 */
public class UsuarioService extends BaseRequest {

    UsuarioInterface service;

    public UsuarioService(Activity context, Converter converter, boolean autenticar) {
        super(context, converter, autenticar);
        service = adapter.create(UsuarioInterface.class);
    }

    public SpiceRequest<Usuario> consultar(final String nombreUsuario) {
        SpiceRequest<Usuario> request = new SpiceRequest<Usuario>(Usuario.class) {
            @Override
            public Usuario loadDataFromNetwork() {
                try {
                    return service.obtenerUsuario(nombreUsuario);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerUsuario(nombreUsuario);
                    }
                    return null;
                }
            }
        };
        return request;
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<DatosUsuario> datosUsuario(final String nombreUsuario) {
        String cacheKey = new StringBuilder().append(nombreUsuario).toString();
        SpiceRequest<DatosUsuario> request = new SpiceRequest<DatosUsuario>(DatosUsuario.class) {
            @Override
            public DatosUsuario loadDataFromNetwork() {
                try {
                    return service.obtenerDatosUsuario(nombreUsuario);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerDatosUsuario(nombreUsuario);
                    }
                    return null;
                }
            }
        };
        //return new SpiceRequest<DatosUsuario>(request, cacheKey, CACHE_EXPIRE);
        return request;
    }

    public SpiceRequest<DatosUsuarioLogueado> datosUsuarioLogueado() {
        SpiceRequest<DatosUsuarioLogueado> request = new SpiceRequest<DatosUsuarioLogueado>(DatosUsuarioLogueado.class) {
            @Override
            public DatosUsuarioLogueado loadDataFromNetwork() {
                try {
                    return service.obtenerDatosUsuarioLogueado();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerDatosUsuarioLogueado();
                    }
                    return null;
                }
            }
        };
        return request;
    }

    public SpiceRequest<List> obtenerRoles(final String nombreUsuario) {
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Rol> loadDataFromNetwork() {
                try {
                    ListaPaginada<Rol> roles = service.obtenerRoles(nombreUsuario);
                    return roles.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Rol> roles = service.obtenerRoles(nombreUsuario);
                        return roles.getLista();
                    }
                    return null;
                }
            }
        };
        return request;
    }


    public SpiceRequest<Response> obtenerClave(final String usuario, final Long idPin, final String respuesta, final String clave) {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() {
                try {
                    ModificarClave datos = new ModificarClave();
                    datos.setPinId(idPin);
                    datos.setRespuesta(respuesta);
                    datos.setClave(clave);
                    Response res = service.modificarClave(usuario,datos);
                    return res;
                } catch (Exception e) {
                    return null;
                }
            }
        };
        return request;
    }

}
