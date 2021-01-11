package py.com.personal.mimundo.services.clientes;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.clientes.models.Cliente;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Konecta on 28/10/2014.
 */
public interface ClientesInterface {

    /**
     * Obtiene la lista de clientes.
     *
     * @return Lista de clientes
     */
    @GET("/clientes")
    public ListaPaginada<Cliente> obtenerClientes( @Query("registros") int registros);

}
