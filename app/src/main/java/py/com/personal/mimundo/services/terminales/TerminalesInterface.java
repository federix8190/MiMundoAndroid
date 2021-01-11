package py.com.personal.mimundo.services.terminales;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.terminales.models.Marca;
import py.com.personal.mimundo.services.terminales.models.Modelo;
import py.com.personal.mimundo.services.terminales.models.Terminal;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Konecta on 14/10/2014.
 */
public interface TerminalesInterface {

    /**
     * Obtiene la lista de terminales.
     *
     * @return Lista de terminales
     */
    @GET("/terminales")
    public ListaPaginada<Terminal> obtenerTerminales(@Query("registros") int registros,
                                                     @Query("marca") String marca,
                                                     @Query("modelo") String modelo);

    /**
     * Obtiene la lista de marcas de terminal.
     *
     * @return Lista de terminales
     */
    @GET("/terminales/marcas")
    public ListaPaginada<Marca> obtenerMarcas(@Query("registros") int registros);

    /**
     * Obtiene la lista de modelos de terminal.
     *
     * @return Lista de terminales
     */
    @GET("/terminales/modelos")
    public ListaPaginada<Modelo> obtenerModelos(@Query("marca") String marca,
                                                @Query("registros") int registros);
}
