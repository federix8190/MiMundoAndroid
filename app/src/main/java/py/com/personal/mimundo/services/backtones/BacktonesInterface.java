package py.com.personal.mimundo.services.backtones;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.backtones.models.Categoria;
import py.com.personal.mimundo.services.backtones.models.ListaTonos;
import py.com.personal.mimundo.services.backtones.models.TonoImagen;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Konecta on 11/25/2014.
 */
public interface BacktonesInterface {

    /**
     * Obtiene las categorias de Backtones.
     *
     * @param inicio      Registro inicial
     * @param registros   Cantidad de registros o -1 para traer todos
     * @param ordenadoPor Nombre de atributo por el cual ordenar. Puede aparecer mas de
     *                    una vez.
     */
    @GET("/backtones/tonos-mas-escuchados")
    public ListaTonos obtenerTonosMasEscuchados(@Query("inicio") int inicio,
                                                @Query("registros") int registros,
                                                @Query("ordenadoPor") String ordenadoPor);


    /**
     * Obtiene las categorias de Backtones.
     *
     * @returnWrapped
     */
    @GET("/backtones/categorias")
    public ListaPaginada<Categoria> obtenerCategorias(@Query("registros") int registros);


    /**
     * Obtiene la imagen del backtone.
     *
     * @returnWrapped GrupoCategoria Lista paginada de grupos de categoria
     */
    @GET("/backtones/tonos/{idTono}/imagen")
    public TonoImagen obtenerImagenDeTono(@Path("idTono") int idTono);



    /**
     * Obtiene los tonos de una categoria.
     *
     * @param idCategoria
     *            Id de la categoria
     * @param inicio
     *            Registro inicial
     * @param registros
     *            Cantidad de registros o -1 para traer todos
     * @param ordenadoPor
     *            Nombre de atributo por el cual ordenar. Puede aparecer mas de
     *            una vez.
     **/
    @GET("/backtones/categorias/{idCategoria}/tonos")
    public ListaTonos obtenerTonosCategoria(@Path("idCategoria") int idCategoria,
                                          @Query("inicio") int inicio,
                                          @Query("registros") int registros,
                                          @Query("ordenadoPor") String ordenadoPor);

    @GET("/backtones/tonos")
    public ListaTonos buscarTonos(@Path("buscar") String texto,
                                 @Query("inicio") int inicio,
                                 @Query("registros") int registros);

}