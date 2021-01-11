package py.com.personal.mimundo.services.servicios.service;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.Ticket;
import py.com.personal.mimundo.services.servicios.models.ActivarRoamingFull;
import py.com.personal.mimundo.services.servicios.models.EstadoServicio;
import py.com.personal.mimundo.services.servicios.models.EstadoServicios;
import py.com.personal.mimundo.services.servicios.models.RoamingOperadora;
import py.com.personal.mimundo.services.servicios.models.RoamingPais;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Konecta on 13/10/2014.
 */
public interface ActivacionServiciosInterface {

    /**
     * Obtiene el estado de todos los servicios activables.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @GET("/lineas/{numeroLinea}/activacion-servicios")
    public EstadoServicios consultarTodos(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene el estado del servicio de Recarga Contra Factura.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @GET("/lineas/{numeroLinea}/activacion-servicios/recarga-contra-factura")
    public EstadoServicio consultarRecargaContraFactura(@Path("numeroLinea") String numeroLinea);

    /**
     * Crea un ticket de activacion del servicio de Recarga Contra Factura.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/activacion-servicios/recarga-contra-factura/activar")
    @Headers("Content-Type: application/json")
    public Response activarRecargaContraFactura(@Path("numeroLinea") String numeroLinea);

    /**
     * Crea un ticket de desactivacion del servicio de Recarga Contra Factura.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/activacion-servicios/recarga-contra-factura/desactivar")
    @Headers("Content-Type: application/json")
    public Response desactivarRecargaContraFactura(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene el estado del servicio de Roaming Automatico.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @GET("/lineas/{numeroLinea}/activacion-servicios/roaming-automatico")
    public EstadoServicio consultarRoamingAutomatico(@Path("numeroLinea") String numeroLinea);

    /**
     * Activa el servicio de Roaming Automatico.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/activacion-servicios/roaming-automatico/activar")
    @Headers("Content-Type: application/json")
    public Resultado activarRoamingAutomatico(@Path("numeroLinea") String numeroLinea);

    /**
     * Desactiva el servicio de Roaming Automatico.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/activacion-servicios/roaming-automatico/desactivar")
    @Headers("Content-Type: application/json")
    public Resultado desactivarRoamingAutomatico(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene el estado del servicio de Roaming Full.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @GET("/lineas/{numeroLinea}/activacion-servicios/roaming-full")
    public EstadoServicio consultarRoamingFull(@Path("numeroLinea") String numeroLinea);

    /**
     * Crea un ticket de activacion del servicio de Roaming Full.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/activacion-servicios/roaming-full/activar")
    public Response activarRoamingFull(@Path("numeroLinea") String numeroLinea, @Body ActivarRoamingFull request);

    /**
     * Crea un ticket de desactivacion del servicio de Roaming Full.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/activacion-servicios/roaming-full/desactivar")
    @Headers("Content-Type: application/json")
    public Response desactivarRoamingFull(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene la lista de paises para Roaming.
     *
     * @return
     */
    @GET("/lineas/{numeroLinea}/activacion-servicios/roaming-full/paises")
    public ListaPaginada<RoamingPais> obtenerPaisesRoaming();

    /**
     * Obtiene los datos de un pais.
     * @param idPais del pais
     *
     * @return
     */
    @GET("/lineas/{numeroLinea}/activacion-servicios/roaming-full/paises/{id}")
    public RoamingPais obtenerPaisRoaming(@Path("id") Integer idPais);

    /**
     * Obtiene la lista de operadoras por pais.
     * @param idPais del pais
     *
     * @return
     */
    @GET("/lineas/{numeroLinea}/activacion-servicios/roaming-full/paises/{id}/operadoras")
    public ListaPaginada<RoamingOperadora> obtenerOperadorasPorPais(@Path("id") Integer idPais);

    /**
     * Obtiene la lista de operadoras.
     *
     * @return
     */
    @GET("/lineas/{numeroLinea}/activacion-servicios/roaming-full/operadoras")
    public ListaPaginada<RoamingOperadora> obtenerOperadorasRoaming(@Path("numeroLinea") String linea,
                                                                    @Query("registros") int registros);

}
