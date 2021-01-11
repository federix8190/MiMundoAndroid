package py.com.personal.mimundo.services.cache.lineas;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.terminales.models.Terminal;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 10/09/2014.
 */
public class ObtenerTerminalLineaRequest extends BaseRequest {

    LineasInterface service;

    public ObtenerTerminalLineaRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    // Proxy - wrap Retrofit request into CachedSpiceRequest
    public SpiceRequest<Terminal> datosTerminalLinea(final String numeroLinea) {
        //String cacheKey = new StringBuilder().append("terminal:").append(numeroLinea).toString();
        SpiceRequest<Terminal> request = new SpiceRequest<Terminal>(Terminal.class) {
            @Override
            public Terminal loadDataFromNetwork() {
                try {
                    System.err.println("datosTerminalLinea...");
                    Terminal t = service.obtenerTerminal(numeroLinea);
                    System.err.println("datosTerminalLinea : " + t.getMarca());
                    return t;
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerTerminal(numeroLinea);
                    }
                    return null;
                }
            }
        };
        return request;
        //return new CachedSpiceRequest<Terminal>(request, cacheKey, CACHE_EXPIRE);
    }

}
