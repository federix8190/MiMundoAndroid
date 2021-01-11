package py.com.personal.mimundo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import py.com.personal.mimundo.services.usuarios.models.ListaRol;
import py.com.personal.mimundo.services.usuarios.models.Rol;
/*
import py.com.personal.mimundo.disenhos.Constantes;
import py.com.personal.mimundo.services.lineas.models.ListaLineas;
import py.com.personal.mimundo.services.usuarios.models.ListaRol;
import py.com.personal.mimundo.services.usuarios.models.Rol;*/

/**
 * Created by Konecta on 20/08/2014.
 */
public class PreferenceUtils {

    /** Boolean para indicar si el usuario esta logueado o no */
    public static final String IS_LOGGED_IN = "is_logged_in";

    /** Codigo del usuario logueado */
    public static final String CODIGO_USUARIO = "codigo_usuario";

    /** Numero de linea seleccionada */
    public static final String LINEA_SELECCIONADA = "LineaSeleccionada";

    /** Lista de roles del usuario */
    public static final String LISTA_ROLES = "ListaRoles";

    /** Tipo de persona : FISICA o JURIDICA */
    public static final String TIPO_PERSONA = "TipoPersona";

    /** Nombre del usuario */
    public static final String NOMBRE_USUARIO = "NombreUsuario";

    /** Tipo de usuario */
    public static final String TIPO_USUARIO = "TipoUsuario";

    /** Tipo de usuario nivel 1 */
    public static final String TIPO_USUARIO_NIVEL_1 = "TMP";

    /** Tipo de usuario nivel 1 */
    public static final String TIPO2_USUARIO_NIVEL_1 = "RES";

    /** Tipo de usuario nivel 2 */
    public static final String TIPO_USUARIO_NIVEL_2 = "TIT";

    /** Id del carrito de compra para compra de terminal */
    public static final String ID_CARTS = "IdCarts";

    /** Modelo de telefono para roaming */
    public static final String MODELO_TELEFONO_ROAMING_FULL = "ModeloTelefonoRoamingFull";

    private Context mContext;

    @Inject
    public PreferenceUtils(Context context) {
        mContext = context;
    }

    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public boolean isLoggedIn() {
        return getPreferences().getBoolean(IS_LOGGED_IN, false);
    }

    public Editor getEditor() {
        return getPreferences().edit();
    }

    public void guardarCodigoUsuario(String codigoUsuario) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(CODIGO_USUARIO, codigoUsuario);
        editor.commit();
    }

    public void guardarPassword(String password) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString("Password", password);
        editor.commit();
    }

    public String getPassword() {
        final SharedPreferences pref = getPreferences();
        String password = pref.getString("Password", "");
        return password;
    }

    public void guardarLineaSeleccionada(String numeroLinea) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(LINEA_SELECCIONADA, numeroLinea);
        editor.commit();
    }

    public void guardarBusquedaFacturacion(String numeroLinea) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString("BusquedaFacturacion", numeroLinea);
        editor.commit();
    }

    public String getBusquedaFacturacion() {
        final SharedPreferences pref = getPreferences();
        String linea = pref.getString("BusquedaFacturacion", "");
        return linea;
    }

    public void guardarLineaFacturacion(String numeroLinea) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString("LineaFacturacion", numeroLinea);
        editor.commit();
    }

    public String getLineaFacturacion() {
        final SharedPreferences pref = getPreferences();
        String linea = pref.getString("LineaFacturacion", "");
        return linea;
    }

    public void guardarRolesUsuario(String listaRoles) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(LISTA_ROLES, listaRoles);
        editor.commit();
    }

    public void guardarTipoPersona(String tipoPersona) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(TIPO_PERSONA, tipoPersona);
        editor.commit();
    }

    public void guardarNombreUsuario(String nombreUsuario) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(NOMBRE_USUARIO, nombreUsuario);
        editor.commit();
    }

    public void guardarTipoUsuario(String tipoUsuario) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(TIPO_USUARIO, tipoUsuario);
        editor.commit();
    }

    public void guardarIdCarts(Long idCart) {
        Editor editor = getEditor();
        editor.putLong(ID_CARTS, idCart);
        editor.commit();
    }

    public String getCodigoUsuario() {
        final SharedPreferences pref = getPreferences();
        String user = pref.getString(CODIGO_USUARIO, "");
        return user;
    }

    public String getLineaSeleccionada() {
        final SharedPreferences pref = getPreferences();
        String linea = pref.getString(LINEA_SELECCIONADA, "");
        return linea;
    }

    public Long obtenerIdCarts() {
        final SharedPreferences pref = getPreferences();
        Long idCart = pref.getLong(ID_CARTS, 0);
        return idCart;
    }

    public List<String> getRolesUsuario() {
        final SharedPreferences pref = getPreferences();
        String jsonListaRoles = pref.getString(LISTA_ROLES, "");
        ListaRol lista = new Gson().fromJson(jsonListaRoles, ListaRol.class);
        List<String> roles = new ArrayList<String>();
        for (Rol rol : lista.getRoles()) {
            roles.add(rol.getNombre());
        }
        return roles;
    }

    public String getTipoPersona() {
        final SharedPreferences pref = getPreferences();
        String tipoPersona = pref.getString(TIPO_PERSONA, "");
        return tipoPersona;
    }

    public String getNombreUsuario() {
        final SharedPreferences pref = getPreferences();
        String tipoPersona = pref.getString(NOMBRE_USUARIO, "");
        return tipoPersona;
    }

    public String getTipoUsuario() {
        final SharedPreferences pref = getPreferences();
        String tipoPersona = pref.getString(TIPO_USUARIO, "");
        return tipoPersona;
    }

    public void borrarDatosSession() {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.putString(CODIGO_USUARIO, null);
        editor.putString(LINEA_SELECCIONADA, null);
        editor.putString(NOMBRE_USUARIO, null);
        editor.putString("Password", null);
        editor.putString(TIPO_USUARIO, null);
        editor.putString(TIPO_PERSONA, null);
        editor.putString(MODELO_TELEFONO_ROAMING_FULL, null);
        editor.putString("Authorization", null);
        editor.commit();
    }

    public void guardarModeloTelefonoRoamingFull(String modelo) {
        Editor editor = getEditor();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(MODELO_TELEFONO_ROAMING_FULL, modelo);
        editor.commit();
    }

    public String getModeloTelefonoRoamingFull() {
        final SharedPreferences pref = getPreferences();
        String linea = pref.getString(MODELO_TELEFONO_ROAMING_FULL, "");
        return linea;
    }

    public void borrarModeloTelefonoRoamingFull() {
        Editor editor = getEditor();
        editor.putString(MODELO_TELEFONO_ROAMING_FULL, null);
        editor.commit();
    }
}
