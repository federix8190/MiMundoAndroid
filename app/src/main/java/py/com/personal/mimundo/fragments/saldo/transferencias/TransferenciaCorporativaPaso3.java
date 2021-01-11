package py.com.personal.mimundo.fragments.saldo.transferencias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.GeneradorDeDialogo;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.MensajesGenericos;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptarRechazar;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.lineas.models.DetalleSaldo;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.transferencias.service.TransferirSaldoRequest;

import py.com.personal.mimundo.services.cache.lineas.ObtenerSaldosLineaRequest;

import py.com.personal.mimundo.services.lineas.models.Saldos;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaCorp;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoCorp;
import py.com.personal.mimundo.utils.NumbersUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class TransferenciaCorporativaPaso3 extends Fragment {

    private static final String ARG_PARAM1 = "parametro_lineas_origen_transferencia";
    private static final String ARG_PARAM2 = "parametro_lineas_destino_transferencia";
    private static final String ARG_PARAM3 = "tipo_lineas_origen_transferencia";

    public static final int TRANS_CORP_PASO_3_CREAR_DIALOG_1 = 1;
    public static final int TRANS_CORP_PASO_3_CREAR_DIALOG_2 = 2;

    private TransferirSaldoRequest transferirSaldoRequest;
    private ProgressDialog progress;

    private ObtenerSaldosLineaRequest saldosRequest;
    private Saldos saldos;

    private List<SolcitarTransferenciaSaldoCorp> parametrosInvocacion;

    private List<String> listaLineaOrigen;
    private List<String> listaLineaDestino;
    private List<String> tipoLineaOrigen;
    private String tipoLinea;

    private TextView inputMoneda;
    private TextView inputMinutos;
    private TextView inputMensajes;
    private TextView inputDatos;

    private TextView saldoMoneda;
    private TextView saldoDisponible;
    private TextView saldoMinutos;
    private TextView saldoMensajes;
    private TextView saldoDatos;

    private LinearLayout saldoDisponibleContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            listaLineaOrigen = getArguments().getStringArrayList(ARG_PARAM1);
            listaLineaDestino= getArguments().getStringArrayList(ARG_PARAM2);
            tipoLineaOrigen = getArguments().getStringArrayList(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_saldos_transferencia_corp, container, false);
        listaLineaOrigen = getArguments().getStringArrayList(ARG_PARAM1);
        listaLineaDestino = getArguments().getStringArrayList(ARG_PARAM2);
        tipoLineaOrigen = getArguments().getStringArrayList(ARG_PARAM3);
        parametrosInvocacion = new ArrayList<>();

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.setActionBar(false, null);

        ImageView transferenciaBtn = (ImageView)v.findViewById(R.id.button_transferencia_paso3);

        // Referencias al Texts y EditTexts del form.
        inputMoneda = (TextView)v.findViewById(R.id.inputMoneda);
        inputMinutos = (TextView)v.findViewById(R.id.inputMinutos);
        inputMensajes = (TextView)v.findViewById(R.id.inputMensajes);
        inputDatos = (TextView)v.findViewById(R.id.inputDatos);
        saldoMoneda = (TextView) v.findViewById(R.id.saldoMoneda);
        saldoDisponible = (TextView) v.findViewById(R.id.saldoDisponible);
        saldoMinutos = (TextView) v.findViewById(R.id.saldoMinutos);
        saldoMensajes = (TextView) v.findViewById(R.id.saldoMensajes);
        saldoDatos = (TextView) v.findViewById(R.id.saldoDatos);
        saldoDisponibleContent = (LinearLayout) v.findViewById(R.id.saldoDisponible_content);

        transferenciaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtInputMoneda = inputMoneda.getText().toString();
                String txtInputMinutos = inputMinutos.getText().toString();
                String txtInputMensajes = inputMensajes.getText().toString();
                String txtInputDatos = inputDatos.getText().toString();

                if(listaLineaOrigen.size() <= 0 ) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.seleccione_linea_origen_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (listaLineaDestino.size() <= 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.seleccione_linea_destino_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (txtInputMoneda.compareTo("") == 0 && txtInputMinutos.compareTo("") == 0
                        && txtInputMensajes.compareTo("") == 0 && txtInputDatos.compareTo("") == 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_seleccionar_tipo_saldo_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //DialogFragment dialogo = crearDialogo();
                    /*DialogFragment dialogo = TransfCorpPaso3DialogFragment.newInstance(
                            getResources().getString(R.string.confirmar_title),
                            getResources().getString(R.string.confirma_transferencia_saldo));*/

                    DialogFragment dialogo = MensajesGenericosAceptarRechazar.newInstance(
                            getResources().getString(R.string.confirmar_title),
                            getResources().getString(R.string.confirma_transferencia_saldo),
                            "", false);

                    dialogo.setTargetFragment(TransferenciaCorporativaPaso3.this, TRANS_CORP_PASO_3_CREAR_DIALOG_2);


                    dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");
                }
            }
        });

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.titulo_transferencia_corporativa)).setTypeface(tf1);

        ((TextView) v.findViewById(R.id.moneda_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.saldoMoneda_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.saldoMoneda)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.datos_title)).setTypeface(tf1);

        ((TextView) v.findViewById(R.id.minutos_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.saldoMinutos)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.saldoMinutos_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.saldoDatos_title)).setTypeface(tf1);

        ((TextView) v.findViewById(R.id.mensajes_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.saldoMensajes)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.saldoMensajes_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.saldoDatos)).setTypeface(tf1);

        ((EditText) v.findViewById(R.id.inputMensajes)).setTypeface(tf1);
        ((EditText) v.findViewById(R.id.inputMoneda)).setTypeface(tf1);
        ((EditText) v.findViewById(R.id.inputMinutos)).setTypeface(tf1);
        ((EditText) v.findViewById(R.id.inputDatos)).setTypeface(tf1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        saldosRequest = new ObtenerSaldosLineaRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        String lineaOrigen = "";
        if (listaLineaOrigen != null && listaLineaOrigen.size() == 1 ) {
            lineaOrigen = listaLineaOrigen.get(0);
            tipoLinea = tipoLineaOrigen.get(0);
            for (String t : tipoLineaOrigen) {
                System.err.println("Transferencia : " + t);
            }
            System.err.println("Transferencia Paso 3 : " + lineaOrigen);
            if (saldos == null) {
                BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
                activity.getSpiceManager().execute(saldosRequest.saldosLinea(lineaOrigen), new SaldosLineaRequestListener());
            } else {
                cargarDatosSaldo(saldos);
            }
        }
    }

    public final class TransferenciaRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();
                mostrarDialogMsg("Error", MensajesDeUsuario.MENSAJE_FALLO_PETICION);
            }
        }

        @Override
        public void onRequestSuccess(final List responseList) {
            Activity context = getActivity();
            if (context != null) {
                progress.dismiss();
                if (responseList != null) {

                    List<Resultado> listaResultados = (List<Resultado>) responseList;
                    int i = 0;
                    List<ResultadoTransferencia> lista = new ArrayList<>();
                    for (Resultado r : listaResultados) {
                        SolcitarTransferenciaSaldoCorp datos = parametrosInvocacion.get(i);
                        lista.add(new ResultadoTransferencia(r, datos));
                        i++;
                    }
                    ResultadoTransferenciasFragment fragment = ResultadoTransferenciasFragment.newInstance(lista);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    /*DialogFragment dialogo = GeneradorDeDialogo.crearDialogo(
                            getResources().getString(R.string.transferencia_saldo_title),
                            getResources().getString(R.string.error_transferir_saldo),
                            getResources().getString(R.string.alta_usuario_btn_aceptar));*/
                    DialogFragment dialogo = GeneradorDeDialogo.GeneradorDialogFragment.newInstance(
                            getResources().getString(R.string.transferencia_saldo_title),
                            getResources().getString(R.string.error_transferir_saldo),
                            getResources().getString(R.string.alta_usuario_btn_aceptar));
                    dialogo.show( getActivity().getSupportFragmentManager(), "Transferencia Fallida");
                }
            }
        }
    }

    private void mostrarDialogMsg(final String header, final String msgError){
        //DialogFragment dialogo = TransfCorp3DialogFragment.newInstance("Error", msgError);

        DialogFragment dialogo = MensajesGenericos.newInstance(header, msgError, false);
        dialogo.setTargetFragment(TransferenciaCorporativaPaso3.this, TRANS_CORP_PASO_3_CREAR_DIALOG_1);
        dialogo.show(getActivity().getSupportFragmentManager(), "Transferir-Saldo");
    }



    public final class SaldosLineaRequestListener implements RequestListener<Saldos> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                saldoMoneda.setText(getResources().getString(R.string.error_saldo_disponible));
                saldoMinutos.setText(getResources().getString(R.string.error_saldo_disponible));
                saldoMensajes.setText(getResources().getString(R.string.error_saldo_disponible));
                saldoDatos.setText(getResources().getString(R.string.error_saldo_disponible));
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Saldos datos) {
            Activity context = getActivity();
            if (context != null) {
                if (datos != null) {
                    saldos = datos;
                    cargarDatosSaldo(datos);
                } else {
                    saldoMoneda.setText(getResources().getString(R.string.error_saldo_disponible));
                    saldoMinutos.setText(getResources().getString(R.string.error_saldo_disponible));
                    saldoMensajes.setText(getResources().getString(R.string.error_saldo_disponible));
                    saldoDatos.setText(getResources().getString(R.string.error_saldo_disponible));
                    Toast.makeText(context, MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cargarParametrosSolitudTransferencia(List<String>listaLineaOrigen, List<String>listaLineaDestino,
            String inputMoneda, String inputMinutos, String inputMensajes, String inputDatos) {

        SolcitarTransferenciaSaldoCorp solicitud;
        parametrosInvocacion = new ArrayList<>();

        for (String lineaOrigen : listaLineaOrigen) {
            for (String lineaDestino : listaLineaDestino) {
                if (inputMoneda.compareTo("") != 0 ) {
                    solicitud = new SolcitarTransferenciaSaldoCorp(
                            lineaOrigen,
                            lineaDestino,
                            inputMoneda,
                            "GUA",
                            ""
                    );
                    parametrosInvocacion.add(solicitud);
                }

                if (inputMinutos.compareTo("") != 0) {
                    solicitud = new SolcitarTransferenciaSaldoCorp(
                            lineaOrigen,
                            lineaDestino,
                            inputMinutos,
                            "MIN",
                            ""
                    );
                    parametrosInvocacion.add(solicitud);
                }

                if (inputMensajes.compareTo("") != 0) {
                    solicitud = new SolcitarTransferenciaSaldoCorp(
                            lineaOrigen,
                            lineaDestino,
                            inputMensajes,
                            "SMS",
                            ""
                    );
                    parametrosInvocacion.add(solicitud);
                }

                if (inputDatos.compareTo("") != 0) {
                    solicitud = new SolcitarTransferenciaSaldoCorp(
                            lineaOrigen,
                            lineaDestino,
                            inputDatos,
                            "DAT",
                            ""
                    );
                    parametrosInvocacion.add(solicitud);
                }
            }
        }
    }

    private void cargarDatosSaldo(Saldos datos) {

        if (!datos.getMoneda().isEmpty()) {
            String monto = NumbersUtils.formatear(datos.getMoneda().get(0).getMonto());
            if (tipoLinea != null && tipoLinea.equals("POSPA")) {
                List<DetalleSaldo> moneda = datos.getMoneda();
                List<DetalleSaldo> pospago = datos.getPospago();
                monto = obtenerSaldoMonedaPospago(moneda, pospago);
                monto = monto + " " + datos.getMoneda().get(0).getUnidad();
                saldoDisponibleContent.setVisibility(View.VISIBLE);
                saldoMoneda.setText(getSaldoNormal(datos.getMoneda()));
                String montoSaldoDisponible = getSaldoDisponible(datos.getPospago());
                saldoDisponible.setText(montoSaldoDisponible);
            } else {
                saldoMoneda.setText(monto);
            }
        } else {
            saldoMoneda.setText(getResources().getString(R.string.sin_saldo));
        }

        if (!datos.getMensajes().isEmpty()) {
            String monto = NumbersUtils.formatear(datos.getMensajes().get(0).getMonto());
            monto = monto  + " " + datos.getMensajes().get(0).getUnidad();
            saldoMensajes.setText(monto);
        } else {
            saldoMensajes.setText(getResources().getString(R.string.sin_saldo));
        }

        if (!datos.getMinutos().isEmpty()) {
            String monto = NumbersUtils.formatear(datos.getMinutos().get(0).getMonto());
            monto = monto  + " " + datos.getMinutos().get(0).getUnidad();
            saldoMinutos.setText(monto);
        } else {
            saldoMinutos.setText(getResources().getString(R.string.sin_saldo));
        }

        if (!datos.getDatos().isEmpty()) {
            String monto = NumbersUtils.formatear(datos.getDatos().get(0).getMonto());
            monto = monto  + " " + datos.getDatos().get(0).getUnidad();
            saldoDatos.setText(monto);
        } else {
            saldoDatos.setText(getResources().getString(R.string.sin_saldo));
        }
    }

    private String getSaldoDisponible(List<DetalleSaldo> datos) {

        for (DetalleSaldo d : datos) {
            String tipo = d.getTipo();
            if (tipo.equalsIgnoreCase("Saldo Disponible")) {
                String monto = NumbersUtils.formatear(d.getMonto());
                return monto + " GUARANIES";
            }
        }
        return "0 GUARANIES";
    }

    private String obtenerSaldoMonedaPospago(List<DetalleSaldo> moneda, List<DetalleSaldo> pospago) {
        if (moneda != null && moneda.size() > 0) {
            DetalleSaldo d = null;
            for (DetalleSaldo detalle : moneda) {
                if (detalle.getTipo().equals(Saldos.SALDO_NORMAL))
                    d = detalle;
            }
            if (d == null) return "0";
            Long montoMoneda = new Long(d.getMonto());
            if (pospago != null && pospago.size() > 0) {
                for (DetalleSaldo detallePospago : pospago) {
                    if (detallePospago.getTipo().contains("Disponible")) {
                        Long montoPospago = new Long(detallePospago.getMonto());
                        montoMoneda = montoMoneda - montoPospago;
                    }
                }
            }
            return NumbersUtils.formatear(montoMoneda);
        }
        return "0";
    }

    private String getSaldoNormal(List<DetalleSaldo> moneda) {
        DetalleSaldo d = null;
        for (DetalleSaldo detalle : moneda) {
            if (detalle.getTipo().equals(Saldos.SALDO_NORMAL))
                d = detalle;
        }
        if (d == null) return "0";
        Long montoMoneda = new Long(d.getMonto());
        return NumbersUtils.formatear(montoMoneda);
    }

    public void ejecutarTransferencia(){
        String txtInputMoneda = inputMoneda.getText().toString();
        String txtInputMinutos = inputMinutos.getText().toString();
        String txtInputMensajes = inputMensajes.getText().toString();
        String txtInputDatos = inputDatos.getText().toString();

        progress =  ProgressDialog.show(getActivity(), getResources().getString(R.string.transferencia_saldo_title),
                getResources().getString(R.string.ejecutando_operacion), true);
        cargarParametrosSolitudTransferencia(listaLineaOrigen, listaLineaDestino,
                txtInputMoneda, txtInputMinutos, txtInputMensajes, txtInputDatos);

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
            transferirSaldoRequest = new TransferirSaldoRequest(
                    getActivity(),
                    new JacksonConverter(objectMapper)
            );
            activity.getSpiceManager().execute(
                    transferirSaldoRequest.solicitarCorp(parametrosInvocacion),
                    new TransferenciaRequestListener()
            );
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case TRANS_CORP_PASO_3_CREAR_DIALOG_1:

                break;
            case TRANS_CORP_PASO_3_CREAR_DIALOG_2:
                if (resultCode == Activity.RESULT_OK) {
                    //Toast.makeText(getActivity(),"Vino OK",Toast.LENGTH_SHORT).show();
                    ejecutarTransferencia();

                    // After Ok code.
                } else if (resultCode == Activity.RESULT_CANCELED){
                    //Toast.makeText(getActivity(),"Vino CANCEL",Toast.LENGTH_SHORT).show();
                    // After Cancel code.
                }

                break;

        }
    }

}

