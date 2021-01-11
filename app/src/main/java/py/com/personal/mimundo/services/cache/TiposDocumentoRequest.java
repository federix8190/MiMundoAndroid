package py.com.personal.mimundo.services.cache;

import android.app.Activity;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.personas.PersonaInterface;
import py.com.personal.mimundo.services.personas.models.TipoDocumento;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 01/10/2014.
 */
public class TiposDocumentoRequest extends BaseRequest {

    PersonaInterface service;

    public TiposDocumentoRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(PersonaInterface.class);
    }

    public CachedSpiceRequest<List> consultar() {
        String cacheKey = new StringBuilder().append("tipos-documento:").toString();
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<TipoDocumento> loadDataFromNetwork() {
                try {
                    return service.obtenerTiposDeDocumento();
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.obtenerTiposDeDocumento();
                    }
                    return null;
                }
            }
        };
        return new CachedSpiceRequest<List>(request, cacheKey, CACHE_EXPIRE);
    }
}
