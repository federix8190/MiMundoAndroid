package py.com.personal.mimundo.services.transferencias.service;

import android.app.Activity;
import android.app.admin.SystemUpdateInfo;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import py.com.personal.mimundo.services.BaseRequest;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaCorp;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaInd;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoCorp;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoInd;
import retrofit.converter.Converter;

/**
 * Created by Konecta on 24/09/2014.
 */
public class TransferirSaldoRequest extends BaseRequest {

    TransferenciaInterface service;

    public TransferirSaldoRequest(Activity context, Converter converter) {
        super(context, converter);
        service = adapter.create(TransferenciaInterface.class);
    }

    public SpiceRequest<RespuestaTransferenciaInd> solicitarInd(final SolcitarTransferenciaSaldoInd solicitud) throws Exception {
        SpiceRequest<RespuestaTransferenciaInd> request = new SpiceRequest<RespuestaTransferenciaInd>(RespuestaTransferenciaInd.class) {
            @Override
            public RespuestaTransferenciaInd loadDataFromNetwork() throws Exception {
                return service.solicitarTransferenciaInd(solicitud);
            }
        };
        return request;
    }

    public SpiceRequest<List> solicitarCorp(final List<SolcitarTransferenciaSaldoCorp> solicitud) throws Exception {
        SpiceRequest<List> request = new SpiceRequest<List>(List.class) {
            @Override
            public List<Resultado> loadDataFromNetwork() {
                try {
                    return service.solicitarTransferenciaCorp(solicitud);
                } catch (Exception ex) {
                    if (expiroToken(ex, context, converter)) {
                        return service.solicitarTransferenciaCorp(solicitud);
                    }
                    return null;
                }
            }
        };
        return request;
    }
}
