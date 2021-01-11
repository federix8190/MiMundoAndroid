package py.com.personal.mimundo.services.transferencias.service;

import java.util.List;

import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaCorp;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaInd;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoCorp;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoInd;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Konecta on 24/07/2014.
 */
public interface TransferenciaInterface {


    /**
     * Solicita transferencia de saldo de una linea origen a una destino.
     * @param solcitarTransferenciaSaldoInd Monto por elcual se desea realizar la transferencia.
     * @return
     */
    @POST("/transferencias/individual")
    public RespuestaTransferenciaInd solicitarTransferenciaInd(@Body SolcitarTransferenciaSaldoInd solcitarTransferenciaSaldoInd);

    /**
     * Solicita transferencia de un tipo saldo de una linea origen a una destino.
     * @param solcitarTransferenciaSaldoCorp cantidad de saldo por elcual se desea realizar la transferencia.
     * @return
     */
    @POST("/transferencias/corporativo")
    public List<Resultado> solicitarTransferenciaCorp(@Body List<SolcitarTransferenciaSaldoCorp> solcitarTransferenciaSaldoCorp);
}
