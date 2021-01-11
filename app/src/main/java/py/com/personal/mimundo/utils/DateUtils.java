package py.com.personal.mimundo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Konecta on 07/01/2016.
 */
public class DateUtils {

    /**
     * Formatos soportados para conversiones de fechas
     */
    public enum Formato {
        DD_MM_YYYY, YYYY_MM_DD_TIME, DD_MM_YYYY_TIME
    }

    /**
     * Convierte una fecha en formato {@code formato} a un objeto String.
     *
     * @param fecha
     *            Fecha
     * @param formato
     *            Formato de la fecha
     * @return String o {@code null} si no se pudo convertir
     */
    public static String convertirADate(Date fecha, Formato formato) {
        if (fecha == null) {
            return null;
        }
        String fechaString = null;
        try {
            SimpleDateFormat sdf;
            switch (formato) {
                case DD_MM_YYYY:
                    sdf = new SimpleDateFormat("dd-MM-yyyy");
                    fechaString = sdf.format(fecha);
                    break;
                case YYYY_MM_DD_TIME:
                    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    fechaString = sdf.format(fecha);
                    break;
                case DD_MM_YYYY_TIME:
                    sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    fechaString = sdf.format(fecha);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            return "Desconocido";
        }
        return fechaString;
    }

}
