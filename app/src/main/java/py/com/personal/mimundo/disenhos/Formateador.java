package py.com.personal.mimundo.disenhos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import py.com.personal.mimundo.services.lineas.models.DetalleSaldo;
import py.com.personal.mimundo.utils.json.JSONObject;

/**
 * Created by Usuario on 9/17/2014.
 */
public class Formateador {

    public static final String PROCESAR_MONTO = "monto";
    public static final String PROCESAR_UNIDAD = "unidad";

    public static final String PROCESAR_DATOS = "datos";
    public static final String PROCESAR_MONEDA = "moneda";
    public static final String PROCESAR_MENSAJES = "mensajes";
    public static final String PROCESAR_MINUTOS = "minutos";

    public static String formatearFecha(Long vencimiento) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date vencimientoDate = new Date(vencimiento);
        return sdf.format(vencimientoDate);
    }

    public static String formatearNumeroLinea(String numeroLinea) {
        return numeroLinea;
    }

    public static JSONObject procesarSaldo(List<DetalleSaldo> tipoDeSaldo, String tipo) {

        String montoTotal = "Desconocido";
        String unidad = "Desconocido";

        // Se procesa por tipo de saldo: moneda, mensaje, dato, etc.
        if (PROCESAR_DATOS.equals(tipo)) {
            if (tipoDeSaldo.isEmpty()) {
                montoTotal = "0";
                unidad = "KILOBYTES";
            } else {
                Double montoDouble = 0.0;
                for (DetalleSaldo detalleSaldo : tipoDeSaldo) {
                    Double monto = Double.valueOf(detalleSaldo.getMonto());
                    montoDouble = montoDouble + monto;
                    unidad = detalleSaldo.getUnidad();
                }
                montoTotal = montoDouble.toString();

            }
        } else if (PROCESAR_MONEDA.equals(tipo)) {
            if (tipoDeSaldo.isEmpty()) {
                montoTotal = "0";
                unidad = "GUARANIES";
            } else {
                Double montoDouble = 0.0;
                for (DetalleSaldo detalleSaldo : tipoDeSaldo) {
                    Double monto = Double.valueOf(detalleSaldo.getMonto());
                    montoDouble = montoDouble + monto;
                    unidad = detalleSaldo.getUnidad();
                }
                montoTotal = montoDouble.toString();

            }
        } else if (PROCESAR_MINUTOS.equals(tipo)) {
            if (tipoDeSaldo.isEmpty()) {
                montoTotal = "0";
                unidad = "MINUTOS";
            } else {
                Integer montoInteger = 0;
                for (DetalleSaldo detalleSaldo : tipoDeSaldo) {
                    Integer monto = Integer.valueOf(detalleSaldo.getMonto().split(":")[0]);
                    montoInteger = montoInteger + monto;
                    unidad = detalleSaldo.getUnidad();
                }
                montoTotal = montoInteger.toString();

            }
        } else if (PROCESAR_MENSAJES.equals(tipo)) {
            if (tipoDeSaldo.isEmpty()) {
                montoTotal = "0";
                unidad = "CANTIDAD";
            } else {
                Integer montoInteger = 0;
                for (DetalleSaldo detalleSaldo : tipoDeSaldo) {
                    Integer monto = Integer.valueOf(detalleSaldo.getMonto());
                    montoInteger = montoInteger + monto;
                    unidad = detalleSaldo.getUnidad();
                }
                montoTotal = montoInteger.toString();

            }
        }

        // Se formatean los datos y se retornan.
        JSONObject json = new JSONObject();
        json.put(PROCESAR_MONTO, montoTotal);
        json.put(PROCESAR_UNIDAD, unidad);
        return json;
    }
}
