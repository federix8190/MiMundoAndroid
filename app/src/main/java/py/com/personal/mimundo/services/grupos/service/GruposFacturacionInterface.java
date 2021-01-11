package py.com.personal.mimundo.services.grupos.service;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.grupos.models.DetalleFactura;
import py.com.personal.mimundo.services.grupos.models.Direccion;
import py.com.personal.mimundo.services.grupos.models.Factura;
import py.com.personal.mimundo.services.grupos.models.GrupoFacturacion;
import py.com.personal.mimundo.services.grupos.models.ResumenGrupo;
import py.com.personal.mimundo.services.lineas.models.Linea;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Konecta on 25/07/2014.
 */
public interface GruposFacturacionInterface {

    /**
     * Obtiene la lista de grupos de facturacion del usuario.
     *
     * @return
     */
    @GET("/grupos-facturacion")
    public ListaPaginada<GrupoFacturacion> obtenerGrupos(@Query("registros") int registros);

    @GET("/grupos-facturacion")
    public ListaPaginada<GrupoFacturacion> getLista(@Query("registros") int registros, @Query("inicio") int inicio);

    @GET("/grupos-facturacion")
    public ListaPaginada<GrupoFacturacion> obtenerGruposByCodigo(@Query("codigo") String codigo);

    /**
     * Obtiene el grupo de facturacion.
     *
     * @param codigoGrupo Codigo de grupo
     * @return
     */
    @GET("/grupos-facturacion/{codigoGrupo}")
    public GrupoFacturacion obtenerGrupo(@Path("codigoGrupo") String codigoGrupo);


    /**
     * Obtiene las lineas del grupo de facturacion.
     * @param codigoGrupo Codigo de grupo
     * @return
     */
    @GET("/lineas")
    public ListaPaginada<Linea> obtenerLineas(@Query("grupoFacturacion") String grupoFacturacion);

    /**
     * Obtiene los datos del Resumen del Grupo de Facturacion.
     * @param codigoGrupo Codigo de grupo
     * @return
     */
    @GET("/grupos-facturacion/{codigoGrupo}/resumen")
    public ResumenGrupo obtenerResumen(@Path("codigoGrupo") String codigoGrupo);

    /**
     * Obtiene las direcciones del grupo de facturacion.
     *
     * @param codigoGrupo Codigo de grupo
     * @return
     */
    @GET("/grupos-facturacion/{codigoGrupo}/direcciones")
    public ListaPaginada<Direccion> obtenerDirecciones(@Path("codigoGrupo") String codigoGrupo,
                                                       @Query("registros") int registros);

    /**
     * Actualiza los datos de la direccion de un grupo.
     * @param codigoGrupo Codigo de grupo
     * @param idDireccion Id de la direccion
     * @param datos Datos a modificar de la direccion
     * @return
     */
    @PUT("/grupos-facturacion/{codigoGrupo}/direcciones/{idDireccion}")
    public Response actualizarDireccion(@Path("codigoGrupo") String codigoGrupo,
            @Path("idDireccion") String idDireccion, @Body Direccion datos);

    /**
     * Obtiene las facturas del grupo de facturacion.
     * @param codigoGrupo Codigo de grupo
     * @return
     */
    @GET("/grupos-facturacion/{codigoGrupo}/facturas")
    public ListaPaginada<Factura> obtenerFacturas(@Path("codigoGrupo") String codigoGrupo,
                                                  @Query("registros") int registros);

    @GET("/grupos-facturacion/{codigoGrupo}/facturas")
    public ListaPaginada<Factura> listar(@Path("codigoGrupo") String codigoGrupo,
                                                  @Query("registros") int registros,
                                                  @Query("inicio") int inicio);

    /**
     * Obtiene los datos de una factura.
     * @param codigoGrupo Codigo de grupo de facturacion
     * @param codigoDocumento Tipo de documento de la factura
     * @param numeroFactura Numero de factura
     * @return
     */
    @GET("/grupos-facturacion/{codigoGrupo}/facturas/{codigoDocumento}/{numeroFactura}")
    public Factura obtenerFactura(@Path("codigoGrupo") String codigoGrupo,
                                  @Path("codigoDocumento")String codigoDocumento,
                                  @Path("numeroFactura") Long numeroFactura);

    /**
     * Obtiene el detalle de la factura.
     * @param codigoGrupo Codigo de grupo de facturacion
     * @param codigoDocumento Tipo de documento de la factura
     * @param numeroFactura Numero de factura
     * @return
     */
    @GET("/grupos-facturacion/{codigoGrupo}/facturas/{codigoDocumento}/{numeroFactura}/detalle")
    public ListaPaginada<DetalleFactura> obtenerDetalleFactura(@Path("codigoGrupo") String codigoGrupo,
                                                        @Path("codigoDocumento")String codigoDocumento,
                                                        @Path("numeroFactura") Long numeroFactura,
                                                        @Query("ordenadoPor") String ordenadoPor,
                                                        @Query("registros") int registros);

    /**
     * Obtiene el detalle de la factura en formato PDF.
     * @param codigoGrupo Codigo de grupo de facturacion
     * @param codigoDocumento Tipo de documento de la factura
     * @param numeroFactura Numero de factura
     * @return
     */
    @GET("/grupos-facturacion/{codigoGrupo}/facturas/{codigoDocumento}/{numeroFactura}/detalle/exportar")
    @Headers("Accept: application/pdf")
    public Response obtenerDetalleFacturaDigital(@Path("codigoGrupo") String codigoGrupo,
           @Path("codigoDocumento")String codigoDocumento, @Path("numeroFactura") Long numeroFactura);
}
