package py.com.personal.mimundo.services.pines.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.pines.models.CrearPin;
import py.com.personal.mimundo.services.pines.models.ValidarMail;
import py.com.personal.mimundo.services.pines.models.ValidarPin;
import py.com.personal.mimundo.services.pines.models.Pin;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.Converter;

/**
 * Created by mabpg on 08/06/17.
 */

public class PinValidacionService extends BaseRequest {

    PinInterface service;

    public PinValidacionService(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(PinInterface.class);
    }

    public PinValidacionService(Activity context, Converter converter, boolean autenticar) {
        super(context, converter, autenticar);
        service = adapter.create(PinInterface.class);
    }

    public SpiceRequest<Response> validarMail(final ValidarMail datos) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {

                try {

                    return service.validarmail(datos);

                } catch(Exception e) {
                    System.err.print("Error en validar Mail : " + e.getMessage());

                }
                return null;
            }
        };
        return request;
    }

    public SpiceRequest<Response> generarPin(final String numeroLinea, final String  canal) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {

                try {

                    CrearPin datos = new CrearPin();
                    datos.setCanalEnvio(canal);
                    datos.setNombreUsuario(numeroLinea);
                    datos.setMensaje("Su PIN para servicios de gestion de saldo es: %s");
                    return service.crearPin(datos);


                } catch(Exception e){
                    System.err.print("Error en generarPin");

                }
                return null;
            }
        };
        return request;
    }


    public SpiceRequest<Response> validarPinSesion(final String  numeroLinea, final String pinId, final String respuestaPin) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {

                try {

                    Pin pin = new Pin();
                    pin.setId(Long.parseLong(pinId));
                    pin.setRespuesta(respuestaPin);

                    return service.validarPinSesion(numeroLinea, pin);


                } catch(RetrofitError e){
                    System.err.print("Error en validarPinSesion");

                }
                return null;
            }
        };
        return request;
    }

    public SpiceRequest<Response> validarPin(final Long pinId, final String respuesta) throws Exception {
        SpiceRequest<Response> request = new SpiceRequest<Response>(Response.class) {
            @Override
            public Response loadDataFromNetwork() throws Exception {

                try {

                    ValidarPin datos = new ValidarPin(respuesta);
                    return service.validarPin(pinId, datos);

                } catch(Exception e){
                    System.err.print("Error en validarPin de");

                }
                return null;
            }
        };
        return request;
    }


}
