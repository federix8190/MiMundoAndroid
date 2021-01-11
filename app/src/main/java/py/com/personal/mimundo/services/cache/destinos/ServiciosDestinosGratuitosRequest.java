package py.com.personal.mimundo.services.cache.destinos;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.destinos.service.DestinosGratuitosInterface;
import py.com.personal.mimundo.services.lineas.models.Servicio;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 07/10/2014.
 */
public class ServiciosDestinosGratuitosRequest extends BaseRequest {

    DestinosGratuitosInterface service;

    public ServiciosDestinosGratuitosRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(DestinosGratuitosInterface.class);
    }

    public SpiceRequest<List> consultar(final String numeroLinea) {
        //String cacheKey = new StringBuilder().append("servicios-destinos-gratuitos:").append(numeroLinea).toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Servicio> loadDataFromNetwork() {
                try {
                    ListaPaginada<Servicio> lista = service.obtenerServicios(numeroLinea, -1);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<Servicio> lista = service.obtenerServicios(numeroLinea, -1);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
