package py.com.personal.mimundo.services.servicios.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.disenhos.Constantes;
import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.servicios.models.EstadoServicio;
import py.com.personal.mimundo.services.servicios.models.EstadoServicios;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 13/10/2014.
 */
public class ConsultarEstadosServicios extends BaseRequest {

    private ActivacionServiciosInterface service;

    public ConsultarEstadosServicios(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(ActivacionServiciosInterface.class);
    }

    public SpiceRequest<EstadoServicios> consultar(final String numeroLinea) {
        SpiceRequest<EstadoServicios> request = new SpiceRequest<EstadoServicios>(EstadoServicios.class) {
            @Override
            public EstadoServicios loadDataFromNetwork() {
                try {
                    return service.consultarTodos(numeroLinea);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return service.consultarTodos(numeroLinea);
                    }
                    return null;
                }
            }
        };
        return request;
    }

    public SpiceRequest<EstadoServicio> consultar(final String numeroLinea, final String servicio) {
        SpiceRequest<EstadoServicio> request = new SpiceRequest<EstadoServicio>(EstadoServicio.class) {
            @Override
            public EstadoServicio loadDataFromNetwork() {
                try {
                    return consultarEstado(numeroLinea, servicio);
                } catch (Exception e) {
                    if (expiroToken(e, context, converter)) {
                        return consultarEstado(numeroLinea, servicio);
                    }
                    return null;
                }
            }
        };
        return request;
    }

    private EstadoServicio consultarEstado(String numeroLinea, String servicio) {
        if (servicio.equals(Constantes.SERVICIO_RECARGA_FACTURA)) {
            return service.consultarRecargaContraFactura(numeroLinea);
        } else if (servicio.equals(Constantes.SERVICIO_ROAMING_AUTOMATICO)) {
            return service.consultarRoamingAutomatico(numeroLinea);
        } else if (servicio.equals(Constantes.SERVICIO_ROAMING_FULL)) {
            return service.consultarRoamingFull(numeroLinea);
        } else {
            return null;
        }
    }
}
