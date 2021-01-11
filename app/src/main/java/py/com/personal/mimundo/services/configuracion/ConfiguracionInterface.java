package py.com.personal.mimundo.services.configuracion;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.configuracion.model.RtaVersion;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mabpg on 05/06/2017.
 */
public interface ConfiguracionInterface {


    /**
     * Obtiene la última versión de la app de Mi mundo .
     *
     * @param clave      Registro inicial
     *
     */
    @GET("/configuraciones")
    public  ListaPaginada<RtaVersion> obtenerAppVersion(@Query("clave") String clave);
}
