package py.com.personal.mimundo.services.pines.service;

import py.com.personal.mimundo.services.pines.models.CrearPin;
import py.com.personal.mimundo.services.pines.models.Pin;
import py.com.personal.mimundo.services.pines.models.ValidarMail;
import py.com.personal.mimundo.services.pines.models.ValidarPin;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Konecta on 11/08/2014.
 */
public interface PinInterface {

    /**
     * Crea un PIN. Envia el PIN creado a la linea del usuario si el canal de
     * envio es SMS o a la direccion de email del usuario si el canal de envio
     * es MAIL.
     * @param datos Datos del Pin
     * @return
     */
    @POST("/pines")
    public Response crearPin(@Body CrearPin datos);

    @POST("/pines/validarmail")
    public Response validarmail(@Body ValidarMail datos);

    /**
     * Valida la respuesta del Pin.
     * @param pinId Identificador del Pin
     * @param validarPin Datos de validacion del Pin
     * @return
     */
    @POST("/pines/{pinId}")
    public Response validarPin(@Path("pinId")Long pinId, @Body ValidarPin validarPin);

    /**
     * Valida la respuesta del Pin.
     * @param nombreUsuario Número de Linea
     * pinSesion El pin de la sesión
     * @return
     */
    @POST("/usuarios/{nombreUsuario}/validar-pin-sesion")
    public Response validarPinSesion(@Path("nombreUsuario")String nombreUsuario, @Body Pin pinSesion);
}
