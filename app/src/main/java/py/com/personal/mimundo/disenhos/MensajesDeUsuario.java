package py.com.personal.mimundo.disenhos;

import java.util.List;

import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Clase que contiene todos los mensajes desplegados a los usuarios.
 */
public class MensajesDeUsuario {

    // Mensaje de Usuario

    public static final String MENSAJE_SIN_LINEAS = "No hay lineas para esta operacion";

    public static final String INFORMACION_GENERAL = "Información";	

    public static final String ACUERDO_DE_USUARIO = "ACUERDO DE USUARIO";
    public static final String BASES_Y_CONDICIONES = "Bases y condiciones Alta Autogestión individual sin " +
            "firma del documento(alta digital)";

    public static final String TITULO_SIN_SERVICIO = "Sin servicio";
    public static final String MENSAJE_SIN_SERVICIO = "Lo sentimos, los datos no están disponibles en este momento.";
    public static final String MENSAJE_FALLO_PETICION = "No se pudieron obtener los datos.";
    public static final String TITULO_SIN_DATOS = "Sin datos";
    public static final String MENSAJE_SIN_DATOS = "No existen datos para este servicio.";
    public static final String MENSAJE_SIN_LINEAS_OPERACION = "No existen datos disponibles para realizar esta operación.";

    public static final String OPERACION_EXITOSA = "Operación exitosa";
    public static final String OPERACION_EXITOSA_MSG = "La operación se realizó correctamente";
    public static final String OPERACION_ERROR = "Error al ejecutar la Operación";

    public static final String SI = "SÍ";
    public static final String NO = "NO";

    public static final String TITULO_LINEA_SIN_SERVICIOS = "No exiten servicios para esta línea.";
    public static final String TITULO_LOADING = "Solicitando datos...";
    public static final String MENSAJE_LOADING = "Los datos están siendo solicitados a nuestros servicios.";
    public static final String SIN_DATOS_LINEA = "No se pudo obtener los datos de la línea";
    public static final String SIN_DATOS_USUARIO = "No se pudo obtener los datos del usuario";
    public static final String ERROR_EMAIL_USUARIO = "No se pudo obtener el email del usuario";

    public static final String MENSAJE_NULL = "Desconocido";

    public static final String ERROR_OBTENER_LINEAS_USUARIO = "No se pudo obtener las lineas del usuario";
    public static final String ERROR_OBTENER_DATOS_USUARIO = "Error al obtener los datos del usuario";
    public static final String ERROR_OBTENER_ROLES_USUARIO = "Error al obtener los roles del usuario";

    // Modificar Perfil
    public static final String TITULO_DIALOGO_MODIFICAR_PERFIL = "Modificar Perfil";
    public static final String LINEAS_SIN_PERFIL = "No se encontraron perfiles para esta linea";

    // Packs
    public static final String PACK_DIALOGO_MSG = "Desea realizar la operación?";
    public static final String COMPRA_PACK_EXITOSA = "!Listo! La compra fue exitosa. El Pack se ha activado a la linea ";
    public static final String ERROR_ACREDITAR_PACK = "Error al acreditar el pack";
    public static final String COMPRA_PACK_DIALOGO_TITULO = "Compras de packs";
    public static final String REGALO_PACK_EXITOSA = "!Listo! La operacion fue exitosa.";
    public static final String ERROR_REGALAR_PACK = "Error al regalar el pack";
    public static final String REGALAR_PACK_DIALOGO_TITULO = "Regalar packs";
    public static final String SUSCRIBIR_PACK_EXITOSA = "!Listo! La operacion fue exitosa.";
    public static final String ERROR_SUSCRIBIR_PACK = "Error en la suscripción del pack";
    public static final String SUSCRIBIR_PACK_DIALOGO_TITULO = "Suscripción de packs";

    // Pedir Saldo
    public static final String SIN_NUMERO_LINEA_PEDIR_SALDO = "Debe especificar el número de línea a quien pedirá el saldo...";
    public static final String TITULO_DIALOGO_PEDIR_SALDO = "Pedir Saldo";
    public static final String MENSAJE_DIALOGO_PEDIR_SALDO = "Solicitando Saldo...";

    // Cambio de Sim
    public static final String INGRESAR_SIM_CARD = "Debe ingresar los ultimos 13 digitos de la SIM CARD";
    public static final String TITULO_CAMBIO_DE_SIM = "Cambio de SIM";

    // Destinos Gratuitos
    public static final String ERROR_DESTINOS_GRATUITOS = "Error al obtener los Destinos Gratuitos";
    public static final String ERROR_SERVICIOS_DESTINOS_GRATUITOS = "No se pudo obtener los servicios de Destinos Gratuitos";
    public static final String TITULO_DIALOGO_MODIFICAR_DESINOS = "Destinos Gratuitos";
    public static final String MENSAJE_DIALOGO_MODIFICAR_DESTINOS = "Guardando datos...";
    public static final String ERROR_MODIFICAR_DESTINOS_GRATUITOS = "Error al guardar los datos";

    // Suspension y restitucion de linea
    public static final String ERROR_TIPO_SUSPENSION = "Debe seleccionar el tipo de suspensión";
    public static final String TITULO_DIALOGO_SUSPENDER_LINEA = "Suspension de linea";
    public static final String TITULO_DIALOGO_RESTITUIR_LINEA = "Restitucion de linea";
    public static final String MENSAJE_DIALOGO_SUSPENDER_LINEA = "Realizando la operacion...";
    public static final String ERROR_SUSPENDER_LINEA = "Error al suspender la linea";
    public static final String ERROR_RESTITUIR_LINEA = "Error al restituir la linea";
    public static final String MENSAJE_SUSPENSION_EXITOSA = "La linea --- ha sido suspendida exitosamente.";
    public static final String MENSAJE_RESTITUCION_EXITOSA = "La linea --- ha sido restituida exitosamente.";

    public static final String ERROR_ACTIVAR_SERVICIO = "Error al activar el servicio";
    public static final String ERROR_DESACTIVAR_SERVICIO = "Error al desactivar el servicio";

    // Roaming
    public static final String TITULO_DIALOGO_ACTIVAR_ROAMING = "Activación de roaming";
    public static final String MENSAJE_DIALOGO_ACTIVAR_ROAMING = "Realizando la activación...";

    // Recarga contra factura
    public static final String TITULO_DIALOGO_ACTIVAR_RCF = "Activación de recarga contra factura";
    public static final String TITULO_DIALOGO_DESACTIVAR_RCF = "Desactivación de recarga contra factura";
    public static final String MENSAJE_DIALOGO_ACTIVAR_RCF = "Realizando la activación...";
    public static final String MENSAJE_DIALOGO_DESACTIVAR_RCF = "Realizando la desactivación...";

    // Acticacion de lineas
    public static final String ELEGIR_OPCION_TERMINAL = "Debe seleccionar la opción de terminal";
    public static final String ELEGIR_TIPO_PLAN = "Debe seleccionar el tipo de plan";
    public static final String ERROR_ACTIVAR_LINEA = "Error al activar la linea";
    public static final String ERROR_CAMBIAR_PLAN = "Error al cambiar el plan";

    // estado de servicio
    public static final String SIN_ESTADO_SERVICIO = "No se pudo obtener el estado del servicio";
    public static final String TITULO_DIALOGO_DESACTIVAR_ROAMING = "Desactivación de Roaming";
    public static final String MENSAJE_DIALOGO_DESACTIVAR_ROAMING = "Realizando la desactivación...";
    public static final String MSG_ROAMING_FULL = "Gracias! Tu solicitud sera atendida en un plazo " +
            "maximo de 24 horas, el codigo de operacion es %s, de ser necesario un representante de " +
            "atencion al cliente se comunicara contigo";

    // Activacion de lineas.
    public static String TITULO_ACTIVACION = "Activación de línea";
    public static String TITULO_CAMBIO_PLANES = "Cambio de plan";
    public static String MENSAJE_ACTIVACION = "Realizando la activación...";
    public static String MENSAJE_CAMBIO_PLANES = "Realizando el cambio de plan...";

    // Transferencia de saldo.
    public static final String TRANSFERENCIA_FALLIDA = "No se pudo realizar la transferencia, error " +
            "del servidor";

    public static final String ERROR_MODIFICAR_LIMITE_CONSUMO = "Error al modificar el limite de consumo";

    // Mensaje ticket generico.
    public static final String MSG_TICKET_GENERICO = "Gracias! Tu solicitud sera atendida en un plazo "
            + "maximo de 24 horas, el codigo de operacion es %s, de ser necesario un representante de "
            + "atencion al cliente se comunicara contigo";

    // Backtones
    public static final String TITULO_DIALOGO_DESACTIVAR_BACKTONES = "Desactivar backtone";
    public static final String MENSAJE_DIALOGO_DESACTIVAR_BACKTONES = "Desactivando...";
    public static final String TITULO_DIALOGO_ACTIVAR_PARA_TODAS_LAS_LLAMADAS = "Activar backtone para todas las llamadas";
    public static final String MENSAJE_DIALOGO_ACTIVAR_PARA_TODAS_LAS_LLAMADAS = "Activando...";
    public static final String TITULO_DIALOGO_DESACTIVAR_PARA_TODAS_LAS_LLAMADAS = "Desactivar backtone para todas las llamadas";
    public static final String MENSAJE_DIALOGO_DESACTIVAR_PARA_TODAS_LAS_LLAMADAS = "Desactivando...";
    public static final String TITULO_DIALOGO_ACTIVAR_REPRODUCCION_ALEATORIA = "Activar reproducción aleatoria";
    public static final String MENSAJE_DIALOGO_ACTIVAR_REPRODUCCION_ALEATORIA = "Activando...";
    public static final String TITULO_DIALOGO_DESACTIVAR_REPRODUCCION_ALEATORIA = "Desactivar reproducción aleatoria";
    public static final String MENSAJE_DIALOGO_DESACTIVAR_REPRODUCCION_ALEATORIA = "Desactivando...";
    public static final String TITULO_DIALOGO_ACTIVAR_RENOVACION_AUTOMATICA = "Activar renovación automática";
    public static final String MENSAJE_DIALOGO_ACTIVAR_RENOVACION_AUTOMATICA = "Activando...";
    public static final String TITULO_DIALOGO_DESACTIVAR_RENOVACION_AUTOMATICA = "Desactivar renovación automática";
    public static final String MENSAJE_DIALOGO_DESACTIVAR_RENOVACION_AUTOMATICA = "Desactivando...";

    // Compra de terminal
    public static final String SELECCION_MINIMA_TELEFONOS = "Debe seleccionar al menos 2 telefonos. Mantenga presionado" +
            " los items que desee comparar";
    public static final String SELECCION_MAXIMA_TELEFONOS = "Solo puede seleccionar hasta 3 telefonos";

    public static String obtenerMensajeDeTicket(Response response, String mensaje) {
        String urlLocation = null;
        List<Header> headers = response.getHeaders();
        for (Header header : headers) {
            if (header != null && header.getName() != null && header.getName().equals("Location")) {
                urlLocation = header.getValue();
                break;
            }
        }
        if (urlLocation != null) {
            String[] path = urlLocation.split("/");
            if (path.length > 0) {
                String codOperacion = path[path.length - 1];
                return String.format(mensaje, codOperacion);
            } else {
                return "Operación exitosa";
            }
        } else {
            return "Operación exitosa";
        }
    }
}
