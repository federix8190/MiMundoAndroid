package py.com.personal.mimundo.services.personas;

import java.util.List;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.personas.models.Persona;
import py.com.personal.mimundo.services.personas.models.TipoDocumento;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Konecta on 29/07/2014.
 */
public interface PersonaInterface {

    @GET("/personas")
    public ListaPaginada<Persona> obtenerPersonas(
            @Query("numeroRuc") String numeroRuc,
            @Query("numeroDocumento") String numeroDocumento);

    @GET("/personas/tipos-documento")
    public List<TipoDocumento> obtenerTiposDeDocumento();
}
