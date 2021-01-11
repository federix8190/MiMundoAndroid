package py.com.personal.mimundo.services.PuntosCercanos.service;

import java.util.List;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.PuntosCercanos.models.PuntoCercano;
import py.com.personal.mimundo.services.lineas.packs.AcreditarPack;
import py.com.personal.mimundo.services.lineas.packs.RespuestaListaArbolPack;
import py.com.personal.mimundo.services.terminales.models.Terminal;
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
public interface PuntosCercanosInterface {

    /**
     * Obtiene la lista de puntos cercanos.
     *
     * @return Lista de puntos cercanos
     */
    @GET("/puntos-venta")
    public List<PuntoCercano> obtenerPuntosCercanos(@Query("latitud") String latitud,
                                                    @Query("longitud") String longitud);

}
