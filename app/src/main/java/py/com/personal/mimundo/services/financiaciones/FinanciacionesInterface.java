package py.com.personal.mimundo.services.financiaciones;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.financiaciones.models.Cuota;
import py.com.personal.mimundo.services.financiaciones.models.Factura;
import py.com.personal.mimundo.services.financiaciones.models.Financiacion;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Konecta on 04/08/2014.
 */
public interface FinanciacionesInterface {

    /**
     * Obtiene la lista de finaciaciones del usuario.
     * @return
     */
    @GET("/financiaciones")
    public ListaPaginada<Financiacion> obtenerFinanciaciones(@Query("registros") int registros);

    /**
     * Obtiene los datos de una financiacion.
     * @param numeroCuenta Numero de Cuenta
     * @return
     */
    @GET("/financiaciones/{numeroCuenta}")
    public Financiacion obtenerFinanciacion(@Path("numeroCuenta") Long numeroCuenta);

    /**
     * Obtiene la lista de facturas de la financiacion
     * @param numeroCuenta Numero de Cuenta
     * @return
     */
    @GET("/financiaciones/{numeroCuenta}/facturas")
    public ListaPaginada<Factura> obtenerFacturas(@Path("numeroCuenta") Long numeroCuenta);


    /**
     * Obtiene la lista de cuotas de la financiacion.
     * @param numeroCuenta Numero de Cuenta
     * @return
     */
    @GET("/financiaciones/{numeroCuenta}/cuotas")
    public ListaPaginada<Cuota> obtenerCuotas(@Path("numeroCuenta") Long numeroCuenta,
                                              @Query("registros") int registros, @Query("ordenadoPor") String campo);
}
