package py.com.personal.mimundo.services.lineas.service;

import java.util.List;

import py.com.personal.mimundo.models.DetallesConsumo;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.Ticket;
import py.com.personal.mimundo.services.lineas.consumo.HistorialesConsumo;
import py.com.personal.mimundo.services.lineas.models.DetallesRecargaContraFactura;
import py.com.personal.mimundo.services.lineas.models.ItemOpcionesRecargaContraFactura;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.models.MontosRecargaSos;
import py.com.personal.mimundo.services.lineas.models.OpcionSuspension;
import py.com.personal.mimundo.services.lineas.models.ParamPedido;
import py.com.personal.mimundo.services.lineas.models.Plan;
import py.com.personal.mimundo.services.lineas.models.RecargaSos;
import py.com.personal.mimundo.services.lineas.models.RespuestaRecargaFactura;
import py.com.personal.mimundo.services.lineas.models.SaldoPcrf;
import py.com.personal.mimundo.services.lineas.models.Saldos;
import py.com.personal.mimundo.services.lineas.models.Servicio;
import py.com.personal.mimundo.services.lineas.models.SolcitarRecarga;
import py.com.personal.mimundo.services.lineas.models.SolcitarRecargaFactura;
import py.com.personal.mimundo.services.terminales.models.Terminal;

import py.com.personal.mimundo.services.lineas.packs.AcreditarPack;
import py.com.personal.mimundo.services.lineas.packs.RespuestaListaArbolPack;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Konecta on 24/07/2014.
 */
public interface LineasInterface {

    /**
     * Obtiene la lista de lineas del usuario.
     *
     * @return Lista de lineas del usuario
     */
    @GET("/lineas")
    @Headers("Content-Type: application/json")
    public ListaPaginada<Linea> obtenerLineas( @Query("registros") int registros);

    /**
     * Obtiene la lista de lineas del usuario por codigo de operacion.
     *
     * @return Lista de lineas del usuario
     */
    @GET("/lineas")
    public ListaPaginada<Linea> obtenerLineas( @Query("registros") int registros,
                                               @Query("operacion") String operacion);

    /**
     * Obtiene la lista de lineas que puedan operar en el Aumento de Limite de Consumo.
     *
     * @return Lista de lineas del usuario
     */
    @GET("/lineas")
    public ListaPaginada<Linea> obtenerLineasLimiteConsumo(@Query("registros") int registros,
                                                           @Query("operacion") String operacion,
                                                           @Query("claseComercial") String claseComercial);

    /**
     * Obtiene la lista de lineas del usuario por codigo de operacion.
     *
     * @return Lista de lineas del usuario
     */
    @GET("/lineas")
    public ListaPaginada<Linea> obtenerLineasDelUsuario( @Query("registros") int registros,
                                               @Query("ordenadoPor") String ordenadoPor);

    /**
     * Obtiene los datos de la linea.
     * @param numeroLinea Numero de Linea
     * @return
     */
    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=2419200")
    @GET("/lineas/{numeroLinea}")
    public Linea obtenerLinea(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene los datos del terminal de la linea.
     * @param numeroLinea Numero de Linea
     * @return
     */
    @GET("/lineas/{numeroLinea}/terminal")
    public Terminal obtenerTerminal(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene los planes de la linea.
     * @param numeroLinea Numero de Linea
     * @return
     */
    @GET("/lineas/{numeroLinea}/planes")
    public ListaPaginada<Plan> obtenerPlanes(@Path("numeroLinea") String numeroLinea,
                                             @Query("registros") int registros);

    /**
     * Obtiene la lista de servicios de la linea.
     * @param numeroLinea Numero de Linea
     * @return
     */
    @GET("/lineas/{numeroLinea}/servicios")
    public ListaPaginada<Servicio> obtenerServicios(@Path("numeroLinea") String numeroLinea,
                                                    @Query("registros") int registros);

    /**
     * Obtiene datos de los saldos de la linea.
     * @param numeroLinea Numero de Linea
     * @return
     */
    @GET("/lineas/{numeroLinea}/saldos")
    public Saldos obtenerSaldos(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene datos de los saldos de la linea.
     * @param numeroLinea Numero de Linea
     * @return
     */
    @GET("/portal-cautivo/saldo-pcrf")
    public SaldoPcrf obtenerSaldoPcrf(@Query("numeroLinea") String numeroLinea);


    @GET("/lineas/{numeroLinea}/consumos/detalle-consumo-limitado")
    public ListaPaginada<DetallesConsumo> obtenerDetalleConsumoLimitado(@Path("numeroLinea") String numeroLinea);

    @GET("/lineas/{numeroLinea}/consumos/historiales-consumo")
    public HistorialesConsumo obtenerHistorialConsumo(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene el historial de prestamos de saldo de la linea.
     * @param numeroLinea Numero de Linea
     * @return RecargaSos
     */
    @GET("/lineas/{numeroLinea}/saldos/recargas-sos")
    public RecargaSos obtenerRecargasSos(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene los montos disponibles para la recarga de saldo.
     * @param numeroLinea Numero de Linea
     * @return RecargaSos
     */
    @GET("/lineas/{numeroLinea}/saldos/recargas-sos/montos-recarga")
    public MontosRecargaSos obtenerMontosRecargasSos(@Path("numeroLinea") String numeroLinea);

    /**
     * Solicita recarga SOS para una linea.
     * @param numeroLinea Numero de linea.
     * @param solcitarRecarga Monto por el cual se desea realizar la recarga.
     * @return
     */
    @POST("/lineas/{numeroLinea}/saldos/recargas-sos")
    public Response solicitarRecargaSos(@Path("numeroLinea")String numeroLinea,
                               @Body SolcitarRecarga solcitarRecarga);


    /**
     * Solicita saldo para la linea.
     * @param numeroLinea Numero de linea.
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/saldos/pedidos")
    public Response pedirSaldo(@Path("numeroLinea")String numeroLinea, @Body ParamPedido numeroDestino);


    /**
     * Obtiene el historial de recargas contra factura de la linea en el ultimo ciclo.
     * @param numeroLinea
     * @return DetallesRecargaContraFactura
     */
    @GET("/lineas/{numeroLinea}/saldos/recargas-contra-factura")
    public List<DetallesRecargaContraFactura> obtenerRecargasContraFactura(@Path("numeroLinea") String numeroLinea);

    /**
     * Obtiene las opciones de recargas contra factura de la linea.
     * @param numeroLinea
     * @return DetallesRecargaContraFactura
     */
    @GET("/lineas/{numeroLinea}/saldos/recargas-contra-factura/montos-recarga")
    public ListaPaginada<ItemOpcionesRecargaContraFactura> montosRecargasContraFactura(@Path("numeroLinea") String numeroLinea);


    /**
     * Solicita recarga contra factura para una linea.
     * @param numeroLinea Numero de linea.
     * @param solcitarRecargaFactura Monto por elcual se desea realizar la recarga.
     * @return
     */
    @POST("/lineas/{numeroLinea}/saldos/recargas-contra-factura")
    public RespuestaRecargaFactura solicitarRecargaContraFactura(@Path("numeroLinea")String numeroLinea,
                                        @Body SolcitarRecargaFactura solcitarRecargaFactura);

    /**
     * Obtiene los packs que puede activar el usuario.
     * @param numeroLinea Numero de Linea
     * @param canal Canal
     * @return
     */
    @GET("/lineas/{numeroLinea}/packs")
    public RespuestaListaArbolPack obtenerPacks(@Path("numeroLinea") String numeroLinea,
                                                @Query("canal") String canal);

    /**
     * Acredita un pack a la linea.
     * @param numeroLinea Numero de Linea
     * @param request Datos de la acreditacion
     * @return
     */
    @POST("/lineas/{numeroLinea}/packs")
    public Response acreditarPacks(@Path("numeroLinea") String numeroLinea, @Body AcreditarPack request);

    /**
     * Solicita la suspension voluntaria de la Linea.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/suspensiones/suspension-voluntaria")
    @Headers("Content-Type: application/json")
    public Response suspensionVoluntaria(@Path("numeroLinea") String numeroLinea);

    /**
     * Solicita la restitucion voluntaria de la Linea.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/suspensiones/restitucion-voluntaria")
    @Headers("Content-Type: application/json")
    public Response restitucionVoluntaria(@Path("numeroLinea") String numeroLinea);

    /**
     * Crea un nuevo ticket de suspension temporal por robo. La linea debe pertenecer al usuario.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/suspensiones/suspension-robo")
    public Response suspensionPorRobo(@Path("numeroLinea") String numeroLinea, @Body OpcionSuspension opcion);

    /**
     * Crea un nuevo ticket de suspension temporal por siniestro. La linea debe pertenecer al usuario.
     * @param numeroLinea Numero de Linea
     *
     * @return
     */
    @POST("/lineas/{numeroLinea}/suspensiones/suspension-siniestro")
    public Response suspensionPorSiniestro(@Path("numeroLinea") String numeroLinea, @Body OpcionSuspension opcion);

}
