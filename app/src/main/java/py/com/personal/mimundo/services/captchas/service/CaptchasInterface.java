package py.com.personal.mimundo.services.captchas.service;

import py.com.personal.mimundo.services.captchas.models.PeticionCaptcha;
import retrofit.http.POST;

/**
 * Created by Konecta on 25/07/2014.
 */
public interface CaptchasInterface {

        /**
     * Crea un nuevo captcha.
     * @return
     */
    @POST("/captchas")
    public PeticionCaptcha crearCaptcha();
}
