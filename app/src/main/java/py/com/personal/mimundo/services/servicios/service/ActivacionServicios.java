package py.com.personal.mimundo.services.servicios.service;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.servicios.service.ActivacionServiciosInterface;
import py.com.personal.mimundo.services.servicios.models.ActivarRoamingFull;
import py.com.personal.mimundo.services.servicios.models.RoamingOperadora;
import py.com.personal.mimundo.services.servicios.models.RoamingPais;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 14/10/2014.
 */
public class ActivacionServicios extends BaseRequest {

    ActivacionServiciosInterface service;

    public ActivacionServicios(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(ActivacionServiciosInterface.class);
    }

    public SpiceRequest<Response> activarRecargaContraFactura(final String numeroLinea) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.activarRecargaContraFactura(numeroLinea);
            }
        };
        return request;
    }

    public SpiceRequest<Response> desactivarRecargaContraFactura(final String numeroLinea) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.desactivarRecargaContraFactura(numeroLinea);
            }
        };
        return request;
    }

    public SpiceRequest<Response> activarRoamingFull(final String numeroLinea, final ActivarRoamingFull datos)
            throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.activarRoamingFull(numeroLinea, datos);
            }
        };
        return request;
    }

    public SpiceRequest<Response> desactivarRoamingFull(final String numeroLinea)
            throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {
                return service.desactivarRoamingFull(numeroLinea);
            }
        };
        return request;
    }

    public SpiceRequest<Resultado> activarRoamingAutomatico(final String numeroLinea) throws Exception {
        SpiceRequest<Resultado> request = new SpiceRequest<Resultado>(Resultado.class) {
            @Override
            public Resultado loadDataFromNetwork() throws Exception {
                return service.activarRoamingAutomatico(numeroLinea);
            }
        };
        return request;
    }

    public SpiceRequest<Resultado> desactivarRoamingAutomatico(final String numeroLinea) throws Exception {
        SpiceRequest<Resultado> request = new SpiceRequest<Resultado>(Resultado.class) {
            @Override
            public Resultado loadDataFromNetwork() throws Exception {
                return service.desactivarRoamingAutomatico(numeroLinea);
            }
        };
        return request;
    }

    public CachedSpiceRequest<List> obtenerPaisesRoaming() throws Exception {
        String cacheKey = new StringBuilder().append("paises-roaming:").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<RoamingPais> loadDataFromNetwork() throws Exception {
                ListaPaginada<RoamingPais> paises = service.obtenerPaisesRoaming();
                return paises.getLista();
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<List> obtenerOperadorasPorPais(final Integer pais) throws Exception {
        String cacheKey = new StringBuilder().append("paises-roaming:").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<RoamingOperadora> loadDataFromNetwork() throws Exception {
                ListaPaginada<RoamingOperadora> operadoras = service.obtenerOperadorasPorPais(pais);
                return operadoras.getLista();
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }

    public CachedSpiceRequest<List> obtenerOperadoras(final String linea) {
        String cacheKey = new StringBuilder().append("operadoras-roaming:").append(linea).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<RoamingOperadora> loadDataFromNetwork() throws Exception {
                try {
                    ListaPaginada<RoamingOperadora> operadoras = service.obtenerOperadorasRoaming(linea, -1);
                    return operadoras.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<RoamingOperadora> operadoras = service.obtenerOperadorasRoaming(linea, -1);
                        return operadoras.getLista();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
