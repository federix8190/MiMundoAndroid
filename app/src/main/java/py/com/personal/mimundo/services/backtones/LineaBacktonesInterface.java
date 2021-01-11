package py.com.personal.mimundo.services.backtones;

import py.com.personal.mimundo.services.backtones.models.GrupoLineas;
import py.com.personal.mimundo.services.backtones.models.ListaGruposLinea;
import py.com.personal.mimundo.services.backtones.models.ListaLineaGrupo;
import py.com.personal.mimundo.services.backtones.models.ListaTonos;
import py.com.personal.mimundo.services.backtones.models.TonoConfiguracion;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Konecta on 11/25/2014.
 */
public interface LineaBacktonesInterface {

    /*
	 * Consulta y administracion de tonos de una linea.
	 */

    /**
     * Obtiene los tonos de una linea.
     *
     * @HTTP 404 Si el numero de linea no existe
     * @returnWrapped Tono Lista paginada de tonos
     *
     */
    @GET("/lineas/{numeroLinea}/backtones/tonos")
    public ListaTonos obtenerTonosLinea(@Path("numeroLinea") String numeroLinea,
                                      @Query("inicio") int inicio,
                                      @Query("registros") int registros,
                                      @Query("ordenadoPor") String ordenadoPor);

    /**
     * Realiza la compra de un backTone.
     *
     * @param numeroLinea
     *            Numero de linea a la cual se le activara el tono
     * @param idTono
     *            Id del tono a ser comprado
     * @HTTP 404 Si el numero de linea
     * @returnWrapped Resultado de la operacion
     * 	{
     * 		exitoso : True o False
     * 		mensaje : "Mensaje de la operacion"
     * 	}
     */
    @PUT("/lineas/{numeroLinea}/backtones/tonos/{idTono}")
    public Response comprarBacktone(@Path("numeroLinea") String numeroLinea, @Path("idTono") Integer idTono);

    /**
     * Desactiva el backtone.
     *
     * @param numeroLinea
     *            Numero de linea a la cual se le desactivara el tono
     * @param idTono
     *            Id del tono
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Resultado de la operacion
     */
    @DELETE("/lineas/{numeroLinea}/backtones/tonos/{idTono}")
    public Response borrarBacktone(@Path("numeroLinea") String numeroLinea,
                                   @Path("idTono") Integer idTono);

    /**
     * Obtiene los datos previos a la compra de un tono.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param idTono
     *            Id del tono
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Datos para la compra del tono
     *
     */
    @GET("/lineas/{numeroLinea}/backtones/tonos/{idTono}/datos-compra")
    public Response obtenerDatosCompra(@Path("numeroLinea") String numeroLinea,
                                       @Path("idTono") Integer idTono);

    /**
     * Obtiene la configuracion del tono.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param idTono
     *            Id del tono
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped TonoConfiguracion Datos de la configuracion
     *
     */
    @GET("/lineas/{numeroLinea}/backtones/tonos/{idTono}/configuracion")
    public TonoConfiguracion obtenerConfiguracionTono(@Path("numeroLinea") String numeroLinea,
                                             @Path("idTono") Integer idTono);

    /**
     * Modifica la configuracion del tono para la linea.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param idTono
     *            Id del tono
     *
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Resultado de la operacion
     *
     * </pre>
     */
    @PUT("/lineas/{numeroLinea}/backtones/tonos/{idTono}/configuracion")
    public Response modificarConfiguracionTono(@Path("numeroLinea") String numeroLinea,
                                               @Path("idTono") Integer idTono,
                                               @Body TonoConfiguracion configuracion);


    /*
	 * Lineas asignadas a un backtone.
	 */

    /**
     * Obtiene las lineas asignadas a un backtone.
     *
     * @param numeroLinea
     *            Numero de linea del titular
     * @param idTono
     *            Id del tono
     * @param inicio
     *            Registro inicial
     * @param registros
     *            Cantidad de registros o -1 para traer todos
     * @HTTP 400 Si los parametros de paginacion son invalidos
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Lista paginada de lineas
     *
     */
    @GET("/lineas/{numeroLinea}/backtones/tonos/{idTono}/lineas-asignadas")
    public ListaLineaGrupo obtenerLineasAsignadas(@Path("numeroLinea") String numeroLinea,
                                           @Path("idTono") Integer idTono,
                                           @Query("inicio") int inicio,
                                           @Query("registros") int registros);

    /**
     * Obtiene los datos de una linea asignada a un backtone.
     *
     * @param numeroLinea
     *            Numero de linea del titular
     * @param idTono
     *            Id del Tono
     * @param numeroLineaAsignada
     *            Numero de linea asignada
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Linea Datos de la linea
     *
     */
    @GET("/lineas/{numeroLinea}/backtones/tonos/{idTono}/lineas-asignadas/{numeroLineaAsignada}")
    public Response obtenerLineaAsignada(@Path("numeroLinea") String numeroLinea,
                                         @Path("idTono") Integer idTono,
                                         @Path("numeroLineaAsignada") String numeroLineaAsignada);

    /**
     * Asigna un backtone a una linea.
     *
     * @param numeroLinea
     *            Numero de linea del titular
     * @param idTono
     *            Id del Tono
     * @param numeroLineaAsignada
     *            Numero de linea asignada al tono
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Resultado de la operacion
     */
    @PUT("/lineas/{numeroLinea}/backtones/tonos/{idTono}/lineas-asignadas/{numeroLineaAsignada}")
    public Response asignarLineaTono(@Path("numeroLinea") String numeroLinea,
                                     @Path("idTono") Integer idTono,
                                     @Path("numeroLineaAsignada") String numeroLineaAsignada);

    /**
     * Desasigna un backtone a una linea
     *
     * @param numeroLinea
     *            Numero de linea del titular
     * @param idTono
     *            Id del Tono
     * @param numeroLineaAsignada
     *            Numero de linea asignada al tono
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Resultado de la operacion
     */
    @DELETE("/lineas/{numeroLinea}/backtones/tonos/{idTono}/lineas-asignadas/{numeroLineaAsignada}")
    public Response sacarLineaAsignada(@Path("numeroLinea") String numeroLinea,
                                       @Path("idTono") Integer idTono,
                                       @Path("numeroLineaAsignada") String numeroLineaAsignada);

    /*
	 * Grupos asignados a un backtone
	 */

    /**
     * Obtiene los grupos asignados a un backtone.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param idTono
     *            Id del Tono
     * @param inicio
     *            Registro inicial
     * @param registros
     *            Cantidad de registros o -1 para traer todos
     * @HTTP 400 Si los parametros de paginacion son invalidos
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Grupo Lista paginada de grupos
     *
     */
    @GET("/lineas/{numeroLinea}/backtones/tonos/{idTono}/grupos-asignados")
    public ListaGruposLinea obtenerGruposAsignados(@Path("numeroLinea") String numeroLinea,
                                           @Path("idTono") Integer idTono,
                                           @Query("inicio") int inicio,
                                           @Query("registros") int registros);

    /**
     * Obtiene los datos de un grupo asignado a un backtone.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param idTono
     *            Id del Tono
     * @param idGrupo
     *            Id del grupo
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Grupo Datos del grupo
     *
     */
    @GET("/lineas/{numeroLinea}/backtones/tonos/{idTono}/grupos-asignados/{idGrupo}")
    public GrupoLineas obtenerGrupoAsignado(@Path("numeroLinea") String numeroLinea,
                                         @Path("idTono") Integer idTono,
                                         @Path("idGrupo") Integer idGrupo);

    /**
     * Asigna un backtone a un grupo.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param idTono
     *            Id del Tono
     * @param idGrupo
     *            Id del grupo
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Resultado de la operacion
     *
     */
    @PUT("/lineas/{numeroLinea}/backtones/tonos/{idTono}/grupos-asignados/{idGrupo}")
    public Response asignarGrupoTono(@Path("numeroLinea") String numeroLinea,
                                     @Path("idTono") Integer idTono,
                                     @Path("idGrupo") Integer idGrupo);

    /**
     * Desasigna un backtone de un grupo.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param idTono
     *            Id del Tono
     * @param idGrupo
     *            Id del grupo
     * @HTTP 404 Si el numero de linea o el tono no existe
     * @returnWrapped Resultado de la operacion
     *
     */
    @DELETE("/lineas/{numeroLinea}/backtones/tonos/{idTono}/grupos-asignados/{idGrupo}")
    public Response desasignarGrupoTono(@Path("numeroLinea") String numeroLinea,
                                        @Path("idTono") Integer idTono,
                                        @Path("idGrupo") Integer idGrupo);

    /**
     * Obtiene los grupos creados por una linea.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param inicio
     *            Registro inicial
     * @param registros
     *            Cantidad de registros o -1 para traer todos
     * @param ordenadoPor
     *            Nombre de atributo por el cual ordenar. Puede aparecer mas de
     *            una vez.
     * @HTTP 400 Si los parametros de paginacion son invalidos
     * @HTTP 404 Si el numero de linea no existe
     * @returnWrapped Grupo Lista paginada de grupos
     */
    @GET("/lineas/{numeroLinea}/backtones/grupos-lineas")
    public ListaGruposLinea obtenerGruposLinea(@Path("numeroLinea") String numeroLinea,
                                       @Query("inicio") int inicio,
                                       @Query("registros") int registros,
                                       @Query("ordenadoPor") String ordenadoPor);

    /**
     * Crea un nuevo grupo.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param datosGrupo
     *            Datos del grupo a crear
     */
    @POST("/lineas/{numeroLinea}/backtones/grupos-lineas")
    public Response crearGrupoLinea(@Path("numeroLinea") String numeroLinea, @Body GrupoLineas datosGrupo);

    /**
     * Obtiene las lineas asignadas a un grupo.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param idGrupo
     *            Id del grupo
     * @param inicio
     *            Registro inicial
     * @param registros
     *            Cantidad de registros o -1 para traer todos
     * @HTTP 400 Si los parametros de paginacion son invalidos
     * @HTTP 404 Si el numero de linea no existe
     * @returnWrapped Lista paginada de lineas
     */
    @GET("/lineas/{numeroLinea}/backtones/grupos-lineas/{idGrupo}/lineas-llamantes")
    public ListaLineaGrupo obtenerLineasGrupo(@Path("numeroLinea") String numeroLinea,
                                       @Path("idGrupo") Integer idGrupo,
                                       @Query("inicio") int inicio,
                                       @Query("registros") int registros);

    /**
     * Asigna una linea a un grupo.
     *
     * @param numeroLinea
     *            Numero de linea del titular
     * @param idGrupo
     *            Id del grupo
     * @param miembro
     *            Numero de linea a agregar al grupo
     * @HTTP 400 Si la linea miembro ya pertenece al grupo
     * @HTTP 404 Si el numero de linea o el grupo no existe
     * @returnWrapped Resultado de la operacion
     */
    @PUT("/lineas/{numeroLinea}/backtones/grupos-lineas/{idGrupo}/lineas-llamantes/{lineaMiembro}")
    public Response asignarLineaGrupo(@Path("numeroLinea") String numeroLinea,
                                      @Path("idGrupo") Integer idGrupo,
                                      @Path("lineaMiembro") String miembro);

    /**
     * Desasigna una linea de un grupo.
     *
     * @param numeroLinea
     *            Numero de linea del titular
     * @param idGrupo
     *            Id del grupo
     * @param miembro
     *            Numero de linea perteneciente al grupo
     * @HTTP 404 Si el numero de linea o el grupo no existe
     * @returnWrapped Resultado de la operacion
     *
     */
    @DELETE("/lineas/{numeroLinea}/backtones/grupos-lineas/{idGrupo}/lineas-llamantes/{lineaMiembro}")
    public Response desasignarLineaGrupo(@Path("numeroLinea") String numeroLinea,
                                         @Path("idGrupo") Integer idGrupo,
                                         @Path("lineaMiembro") String miembro);
}
