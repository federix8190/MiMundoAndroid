package py.com.personal.mimundo.services.destinos.service;

import java.util.List;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.destinos.models.Destino;
import py.com.personal.mimundo.services.lineas.models.Servicio;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Konecta on 06/10/2014.
 */
public interface DestinosGratuitosInterface {

    /**
     * Obtiene la lista de destinos gratuitos de la linea.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param registros
     *            Cantidad de registros o -1 para obtener todos
     *
     * @return Lista de Destinos gratuitos de la linea
     */
    @GET("/lineas/{numeroLinea}/destinos-gratuitos")
    public ListaPaginada<Destino> obtenerDestinos(@Path("numeroLinea") String numeroLinea,
                                                  @Query("registros") int registros);

    /**
     * Modifica la lista de destinos gratuitos de la linea. El servicio recibe
     * como parametro una lista de destinos gratuitos y determina cuales de los
     * destinos se deben agregar y cuales se deben eliminar de acuerdo a la
     * lista actual de destinos de la linea.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param destinos
     *            Lista de destinos gratuitos.
     *
     * @return Lista de resultados de las operaciones.
     */
    @POST("/lineas/{numeroLinea}/destinos-gratuitos")
    public List<Resultado> guardarDestinos(@Path("numeroLinea") String numeroLinea,
                                           @Body List<Destino> destinos);

    /**
     * Obtiene la lista de servicios de destinos gratuitos de la linea.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param registros
     *            Cantidad de registros o -1 para obtener todos
     *
     * @return Lista de servicios de la linea
     */
    @GET("/lineas/{numeroLinea}/destinos-gratuitos/servicios")
    public ListaPaginada<Servicio> obtenerServicios(@Path("numeroLinea") String numeroLinea,
                                                    @Query("registros") int registros);

    /**
     * Obtiene la cantidad total de lineas destino disponibles de acuerdo al codigo de servicio.
     *
     * @param numeroLinea
     *            Numero de linea
     * @param codigoServicio
     *            Codigo del Servicio
     * @param codigoGrupo
     *            Codigo de grupo
     *
     * @return Lista de servicios de la linea
     */
    @GET("/lineas/{numeroLinea}/destinos-gratuitos/servicios/{codigoServicio}/{codigoGrupo}/cantidad")
    public Integer obtenerCantidad(@Path("numeroLinea") String numeroLinea,
                                   @Path("codigoServicio") String codigoServicio,
                                   @Path("codigoGrupo") String codigoGrupo);
}
