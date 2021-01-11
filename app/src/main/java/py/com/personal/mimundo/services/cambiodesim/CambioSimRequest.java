package py.com.personal.mimundo.services.cambiodesim;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.Resultado;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 18/05/2016.
 */
public class CambioSimRequest extends BaseRequest {

    CambioSimInterface service;

    public CambioSimRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(CambioSimInterface.class);
    }

    public SpiceRequest<Resultado> procesar(final String numeroLinea, final CambioDeSimParametros parametros) throws Exception {
        SpiceRequest<Resultado> request = new SpiceRequest<Resultado>(Resultado.class) {
            @Override
            public Resultado loadDataFromNetwork() throws Exception {
                try {
                    return service.procesar(numeroLinea, parametros);
                } catch (Exception e) {
                    return null;
                }
            }
        };
        return request;
    }

    public SpiceRequest<List> obtenerTiposDeCambio(final String numeroLinea) {
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<TipoDeCambio> loadDataFromNetwork() {
                try {
                    ListaPaginada<TipoDeCambio> lista = service.obtenerTiposDeCambio(numeroLinea);
                    return lista.getLista();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        ListaPaginada<TipoDeCambio> lista = service.obtenerTiposDeCambio(numeroLinea);
                        return lista.getLista();
                    }
                    return null;
                }
            }
        };
        return request;
    }

}
