package py.com.personal.mimundo.services.usuarios.service;

import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.pines.models.Pin;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.usuarios.models.CrearUsuarioTemporal;
import py.com.personal.mimundo.services.usuarios.models.DatosSesion;
import py.com.personal.mimundo.services.usuarios.models.DatosUsuario;
import py.com.personal.mimundo.services.usuarios.models.DatosUsuarioLogueado;
import py.com.personal.mimundo.services.usuarios.models.ModificarClave;
import py.com.personal.mimundo.services.usuarios.models.Rol;
import py.com.personal.mimundo.services.usuarios.models.Usuario;
import py.com.personal.mimundo.services.usuarios.models.ValidarUsuario;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PUT;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Servicios de consulta de usuarios.
 * 
 * @author Ruben Lopez, Federico Torres
 */
public interface UsuarioInterface {

	/**
	 * Obtiene los datos del usuario.
	 *
	 * @param nombreUsuario
	 *            Nombre de usuario
	 *
	 * @return Usuario Datos del usuario
	 */
	@GET("/usuarios/{nombreUsuario}")
	public Usuario obtenerUsuario(@Path("nombreUsuario") String nombreUsuario);

    /**
     * Obtiene los datos del usuario logueado, del cliente asociado al usuario y
     * de su sesion.
     *
     * @return Usuario Datos del usuario logueado
     */
    @GET("/usuarios/datos-usuario-logueado")
    public DatosUsuarioLogueado obtenerDatosUsuarioLogueado();

	/**
	 * Obtiene los datos del usuario de KML.
	 * 
	 * @param nombreUsuario
	 *            Nombre de usuario
	 * 
	 * @return DatosUsuario Datos del usuario de KML
	 * 
	 *         <pre>
	 * {
	 *     "nombres": "Sergio",
	 *     "apellidos": "Florentin",
	 *     "codigoPersona": null,
	 *     "tipoDocumento": null,
	 *     "numeroDocumento": null,
	 *     "telefonoContacto": null,
	 *     "email": null,
	 *     "ciudad": null,
	 *     "barrio": null,
	 *     "direccion": null,
	 * }
	 * </pre>
	 */
    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=2419200")
	@GET("/usuarios/{nombreUsuario}/datos-usuario")
	public DatosUsuario obtenerDatosUsuario(@Path("nombreUsuario") String nombreUsuario);

    /**
     * Obtiene los datos de la sesion del usuario.
     *
     * @param nombreUsuario
     *            Nombre de usuario
     */
    @GET("/usuarios/{nombreUsuario}/datos-sesion")
    public DatosSesion obtenerDatosSesion(@Path("nombreUsuario") String nombreUsuario);

    /**
     * Obtiene los roles del usuario.
     *
     * @param nombreUsuario
     *            Nombre de usuario
     */
    @GET("/usuarios/{nombreUsuario}/roles")
    public ListaPaginada<Rol> obtenerRoles(@Path("nombreUsuario") String nombreUsuario);

	/**
	 * Modifica los datos del usuario en KML.
	 * 
	 * @param nombreUsuario
	 *            Nombre de usuario
	 * @param datosUsuario
	 *            Datos del usuario
	 * 
	 *            <pre>
	 * {
	 *     "nombres": "Sergio",
	 *     "apellidos": "Florentin",
	 *     "codigoPersona": null,
	 *     "tipoDocumento": null,
	 *     "numeroDocumento": null,
	 *     "telefonoContacto": null,
	 *     "email": null,
	 *     "ciudad": null,
	 *     "barrio": null,
	 *     "direccion": null,
	 * }
	 * </pre>
	 */
	@PUT("/usuarios/{nombreUsuario}/datos-usuario")
	public Response modificarDatosUsuario(
            @Path("nombreUsuario") String nombreUsuario,
            @Body DatosUsuario datosUsuario);

    /**
     * Valida que un usuario pueda ser creado o no.
     * @param body Datos para la validacion de usuario
     * @return
     */
    @POST("/usuarios/validaciones/creacion")
    public Resultado valiarCreacionUsuario(@Body ValidarUsuario body);

    /**
     * Crea un usuario de tipo Temporal.
     *
     * @param body Datos del nuevo usuario temporal
     * @return
     */
    @POST("/usuarios/temporal")
    public Response crearUsuarioTemporal(
            @Body CrearUsuarioTemporal body);

    /**
     * Activa el usuario.
     * @param nombreUsuario Nombre de usuario
     * @param pin Datos del Pin
     */
    @POST("/usuarios/{nombreUsuario}/activar")
    public Response activarUsuario(@Path("nombreUsuario") String nombreUsuario, @Body Pin pin);

    /**
     * Resetea la clave del usuario. Envia la nueva clave al usuario por SMS o
     * MAIL.
     * @param nombreUsuario Nombre de usuario
     * @param canal Canal de envio de la nueva clave. Puede ser SMS o MAIL
     * @return
     */
    @POST("/usuarios/{nombreUsuario}/resetear")
    public Response resetearClave(@Path("nombreUsuario") String nombreUsuario,
                                  @Query("canalEnvio") String canal);

    /**
     * Cambia la clave del usuario.
     * @param nombreUsuario Nombre de usuario
     * @param datos Datos del Pin y la nueva clave
     * @return
     */
    @PUT("/usuarios/{nombreUsuario}/clave")
    public Response modificarClave(@Path("nombreUsuario") String nombreUsuario,
                                   @Body ModificarClave datos);


}
