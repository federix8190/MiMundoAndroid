package py.com.personal.mimundo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Konecta on 29/06/2016.
 */
public class StringUtils {

    /**
     * Valida que los caracteres ingresados en la linea sean correctos.
     *
     * @param numeroLinea
     * @return
     */
    public static boolean esLineaTelefonia(String numeroLinea) {
        Pattern pattern = Pattern.compile("^(09|9|5959|\\+5959)([1-9]{2})([0-9]{6})$");
        Matcher matcher = pattern.matcher(numeroLinea);
        return matcher.matches();
    }

    public static String formatoMiMundo(String destino) {
        if (esLineaTelefonia(destino)) {
            if (destino.charAt(0) == '0') {
                destino = destino.substring(1);
            }
        }
        return destino;
    }
}
