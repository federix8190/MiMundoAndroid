package py.com.personal.mimundo.services.destinos.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.destinos.models.Destino;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 07/10/2014.
 */
public class GuardarDestinosGratuitos extends BaseRequest {

    DestinosGratuitosInterface service;

    public GuardarDestinosGratuitos(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(DestinosGratuitosInterface.class);
    }

    public SpiceRequest<List> ejecutar(final String numeroLinea, final List<Destino> destinos) throws Exception {
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Resultado> loadDataFromNetwork() throws Exception {
                try {
                    return service.guardarDestinos(numeroLinea, destinos);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.guardarDestinos(numeroLinea, destinos);
                    }
                    return null;
                }
            }
        };
        return request;
    }
}
