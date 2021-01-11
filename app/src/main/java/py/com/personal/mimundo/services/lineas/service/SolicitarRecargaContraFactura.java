package py.com.personal.mimundo.services.lineas.service;

import android.app.Activity;

import com.octo.android.robospice.request.SpiceRequest;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.lineas.models.RespuestaRecargaFactura;
import py.com.personal.mimundo.services.lineas.models.SolcitarRecargaFactura;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 01/10/2014.
 */
public class SolicitarRecargaContraFactura extends BaseRequest {

    LineasInterface service;

    public SolicitarRecargaContraFactura(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(LineasInterface.class);
    }

    public SpiceRequest<RespuestaRecargaFactura> solicitar(final String numeroLinea, final Double monto, final Integer porcentaje) throws Exception {
        SpiceRequest<RespuestaRecargaFactura> request = new SpiceRequest<RespuestaRecargaFactura>(RespuestaRecargaFactura.class) {
            @Override
            public RespuestaRecargaFactura loadDataFromNetwork() throws Exception {
                SolcitarRecargaFactura datos = new SolcitarRecargaFactura(monto, porcentaje);
                RespuestaRecargaFactura res = service.solicitarRecargaContraFactura(numeroLinea, datos);
                return res;
            }
        };
        return request;
    }
}
