package py.com.personal.mimundo.services.configuracion;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;


import py.com.personal.mimundo.services.BaseRequestSinAuthorization;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.configuracion.model.RtaVersion;
import retrofit.converter.Converter;

/**
 * Created by mabpg on 05/06/2017.
 */
public class ConfiguracionService extends BaseRequestSinAuthorization {

    ConfiguracionInterface service;

    public ConfiguracionService(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(ConfiguracionInterface.class);
    }

    public SpiceRequest<List> utlimaVersion() throws Exception {
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<RtaVersion> loadDataFromNetwork() throws Exception {

                try {
                    ListaPaginada<RtaVersion> res = service.obtenerAppVersion("MiMundoAndroidversionCode");
                    return res.getLista();
                } catch(Exception e){

                }
                return null;
            }
        };
        return request;
    }
}
