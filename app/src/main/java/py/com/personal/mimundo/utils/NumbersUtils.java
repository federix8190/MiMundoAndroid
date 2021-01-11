package py.com.personal.mimundo.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Konecta on 03/11/2014.
 */
public class NumbersUtils {

    public static String formatear(long monto) {
        DecimalFormat formateador = new DecimalFormat("###,###.##");
        String montoFormateado = formateador.format(monto);
        return montoFormateado.replace(",", ".");
    }

    public static String formatear(String montoString) {
        if (montoString != null) {
            try {
                DecimalFormat formateador = new DecimalFormat("###,###.##");
                Long monto = new Long(montoString);
                String montoFormateado = formateador.format(monto);
                return montoFormateado.replace(",", ".");
            } catch (NumberFormatException e) {
                return montoString;
            }
        } else {
            return "0";
        }
    }

    public static Double round(Double val, int escala) {
        return new BigDecimal(val.toString()).setScale(escala, RoundingMode.HALF_UP).doubleValue();
    }

    public static String formatearUnidad(String unidad) {
        switch (unidad) {
            case "GUARANIES":
                return "Gs";
            case "Gua":
                return "Gs";
            case "MEGABYTES":
                return "MB";
            default:
                return unidad;
        }
    }
}
