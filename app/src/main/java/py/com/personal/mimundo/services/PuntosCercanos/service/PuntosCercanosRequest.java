package py.com.personal.mimundo.services.PuntosCercanos.service;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.ArrayList;
import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.PuntosCercanos.models.PuntoCercano;
import py.com.personal.mimundo.services.grupos.models.GrupoFacturacion;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 01/10/2014.
 */
public class PuntosCercanosRequest extends BaseRequest {

    PuntosCercanosInterface service;

    public PuntosCercanosRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(PuntosCercanosInterface.class);
    }

    public CachedSpiceRequest<List> getPuntosCercanos(final String latitud, final String longitud) {
        String cacheKey = new StringBuilder().append("puntos-cercanos").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<PuntoCercano> loadDataFromNetwork() throws Exception {
                try {
                    System.err.println("getPuntosCercanos latitud : " + latitud);
                    System.err.println("getPuntosCercanos longitud : " + longitud);
                    List<PuntoCercano> puntos = service.obtenerPuntosCercanos(latitud, longitud);
                    System.err.println("getPuntosCercanos total : " + puntos.size());
                    return puntos;
                } catch (Exception e) {
                    System.err.println("getPuntosCercanos error : " + e.getMessage());
                    return new ArrayList<PuntoCercano>();
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
