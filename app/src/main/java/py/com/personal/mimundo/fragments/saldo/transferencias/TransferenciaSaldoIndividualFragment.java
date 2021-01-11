package py.com.personal.mimundo.fragments.saldo.transferencias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.DecimalFormat;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptarRechazar;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptar;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineasUsuarioRequest;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.transferencias.service.TransferirSaldoRequest;
import py.com.personal.mimundo.services.cache.lineas.ObtenerSaldosLineaRequest;
import py.com.personal.mimundo.services.lineas.models.DetalleSaldo;
import py.com.personal.mimundo.services.lineas.models.Saldos;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaInd;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoInd;
import py.com.personal.mimundo.utils.NumbersUtils;
import py.com.personal.mimundo.utils.StringUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static py.com.personal.mimundo.utils.StringUtils.esLineaTelefonia;
import static py.com.personal.mimundo.utils.StringUtils.formatoMiMundo;

public class TransferenciaSaldoIndividualFragment extends Fragment {

    private static final String OPERACION = "TRANSFERENCIA_SALDO";
    private static final String FRAGMENT_NAME = "TransferenciaSaldoIndividualFragment";
    private static final int TRANS_SAL_IND_CREAR_DIALOG_1 = 1;
    private static final int TRANS_SAL_IND_CREAR_DIALOG_2 = 2;

    // Selector de lineas
    private String linea;
    private ObtenerLineasUsuarioRequest lineasService;
    private RelativeLayout pantalla;
    private ProgressBar progressBar;

    private TransferirSaldoRequest transferirSaldoRequest;
    private ProgressDialog progress;

    private ObtenerSaldosLineaRequest saldosRequest;
    private Saldos saldos;

    private TextView saldoMoneda;
    private TextView numeroAmigo;
    private TextView montoTransferenciaTx;

    public static TransferenciaSaldoIndividualFragment newInstance() {
        TransferenciaSaldoIndividualFragment f = new TransferenciaSaldoIndividualFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_transferencia_individual, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setTitle(getResources().getString(R.string.transferencia_saldo_title));

        pantalla = (RelativeLayout) v.findViewById(R.id.pantalla);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
        showLoader(true);

        Button solicitarRecarga = (Button)v.findViewById(R.id.button_Tranferir_Saldo_Ind);
        numeroAmigo = (TextView)v.findViewById(R.id.numero_Tranferir_Saldo_Ind);
        montoTransferenciaTx = (TextView)v.findViewById(R.id.monto_Tranferir_Saldo_Ind);
        saldoMoneda = (TextView) v.findViewById(R.id.saldoMoneda);

        solicitarRecarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lineaOrigen = ((BaseDrawerActivity)getActivity()).obtenerLineaSeleccionada();
                String lineaDestino = numeroAmigo.getText().toString();
                String monto = montoTransferenciaTx.getText().toString();

                if(lineaOrigen.compareTo("") == 0 ) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.seleccione_linea_origen_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (lineaDestino.compareTo("") == 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.seleccione_linea_destino_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (monto.compareTo("") == 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.ingrese_monto_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //DialogFragment dialogo = crearDialogo();
                    DialogFragment dialogo = MensajesGenericosAceptarRechazar.newInstance(
                            getResources().getString(R.string.confirmar_title),
                            getResources().getString(R.string.confirma_transferencia_saldo),
                            "", false);

                    dialogo.setTargetFragment(TransferenciaSaldoIndividualFragment.this, TRANS_SAL_IND_CREAR_DIALOG_1);
                    dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");
                }
            }
        });

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.title_1)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.title_2)).setTypeface(tf1);

        Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CarroisGothic-Regular.ttf");
        ((TextView) v.findViewById(R.id.tituloNumeroDestino)).setTypeface(tf2);
        ((TextView) v.findViewById(R.id.monto_title)).setTypeface(tf2);
        numeroAmigo.setTypeface(tf2);
        montoTransferenciaTx.setTypeface(tf2);
        saldoMoneda.setTypeface(tf2);
        solicitarRecarga.setTypeface(tf2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineasService = new ObtenerLineasUsuarioRequest(getActivity(), new JacksonConverter(objectMapper), OPERACION);
        saldosRequest = new ObtenerSaldosLineaRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (activity.esOperacionActual(OPERACION)) {
            showLoader(false);
            activity.setActionBar(true, FRAGMENT_NAME);
            linea = ((BaseDrawerActivity)getActivity()).obtenerLineaSeleccionada();
            cargarDatosLinea();
        } else {
            activity.setActionBar(false, FRAGMENT_NAME);
            activity.getSpiceManager().execute(lineasService.listar(), new LineasRequestListener());
        }
    }

    public final class LineasRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showLoader(false);
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List lista) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (activity != null) {
                showLoader(false);
                if (lista != null && lista.size() > 0) {
                    activity.cargarListaLineas(lista, OPERACION);
                    activity.setActionBar(true, FRAGMENT_NAME);
                    List<Linea> lineas = lista;
                    linea = lineas.get(0).getNumeroLinea();
                    activity.guardarLineaSeleccionada(linea);
                    activity.seleccionarPrimerItem();
                    cargarDatosLinea();
                    TextView lineaSeleccionadaView = (TextView)activity.getmDrawerList().findViewById(R.id.reservedNamedId);
                    if (lineaSeleccionadaView != null) {
                        String nroLinea = activity.obtenerLineaSeleccionada();
                        if (nroLinea != null && esLineaTelefonia(nroLinea)) {
                            lineaSeleccionadaView.setText("0" + nroLinea);
                        } else {
                            lineaSeleccionadaView.setText(nroLinea);
                        }
                    }
                } else {
                    Toast.makeText(activity, MensajesDeUsuario.MENSAJE_SIN_LINEAS_OPERACION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cargarDatosLinea() {
        if (saldos == null) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            activity.getSpiceManager().execute(saldosRequest.saldosLinea(linea), new SaldosLineaRequestListener());
        } else {
            cargarDatosSaldo(saldos);
        }
    }

    public final class TransferenciaRequestListener implements RequestListener<RespuestaTransferenciaInd> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();

                DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                        getResources().getString(R.string.transferencia_saldo_title),
                        MensajesDeUsuario.TRANSFERENCIA_FALLIDA);

                dialogo.setTargetFragment(TransferenciaSaldoIndividualFragment.this, TRANS_SAL_IND_CREAR_DIALOG_2);

               /* DialogFragment dialogo = crearDialogo(getResources().getString(R.string.transferencia_saldo_title),
                        MensajesDeUsuario.TRANSFERENCIA_FALLIDA);*/
                dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");
            }
        }

        @Override
        public void onRequestSuccess(final RespuestaTransferenciaInd response) {
            Activity context = getActivity();
            if (context != null) {
                progress.dismiss();
                if (response != null && response.getExitoso().equals("true")) {
                    saldos = null;
                    Toast.makeText(getActivity(), getResources().getString(R.string.transferencia_saldo_exitosa),
                            Toast.LENGTH_SHORT).show();

                    DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                            getResources().getString(R.string.transferencia_saldo_title),
                            getResources().getString(R.string.transferencia_saldo_exitosa));

                    dialogo.setTargetFragment(TransferenciaSaldoIndividualFragment.this, TRANS_SAL_IND_CREAR_DIALOG_2);

                    /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.transferencia_saldo_title),
                            getResources().getString(R.string.transferencia_saldo_exitosa));*/
                    dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");

                    BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
                    String lineaOrigen = activity.obtenerLineaSeleccionada();
                    activity.getSpiceManager().cancel(saldosRequest.saldosLinea(lineaOrigen));

                    TransferenciaSaldoIndividualFragment fragment = new TransferenciaSaldoIndividualFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

                } else {

                    DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                            getResources().getString(R.string.transferencia_saldo_title),
                            getResources().getString(R.string.error_transferencia_saldo));

                    dialogo.setTargetFragment(TransferenciaSaldoIndividualFragment.this, TRANS_SAL_IND_CREAR_DIALOG_2);

                    /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.transferencia_saldo_title),
                            getResources().getString(R.string.error_transferencia_saldo));*/
                    dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");
                }
            }
        }
    }

    public final class SaldosLineaRequestListener implements RequestListener<Saldos> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                saldoMoneda.setText(getResources().getString(R.string.error_saldo_disponible));
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Saldos datos) {
            Activity context = getActivity();
            if (context != null) {
                if (datos != null) {
                    cargarDatosSaldo(datos);
                    saldos = datos;
                } else {
                    saldoMoneda.setText(getResources().getString(R.string.error_saldo_disponible));
                    Toast.makeText(context, MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cargarDatosSaldo(Saldos datos) {
        if (!datos.getMoneda().isEmpty()) {
            DetalleSaldo moneda = datos.getMoneda().get(0);
            String unidad = "";
            if (moneda.getUnidad() != null) {
                unidad = moneda.getUnidad();
            }
            String monto = getResources().getString(R.string.dato_desconocido);
            if (moneda.getMonto() != null) {
                monto = moneda.getMonto();
                DecimalFormat formatea = new DecimalFormat("###,###.##");
                monto = formatea.format(new Integer(monto));
            }
            saldoMoneda.setText(getResources().getString(R.string.saldo_disponible_transferencia)
                    + " " + monto + " " + NumbersUtils.formatearUnidad(unidad));
        } else {
            saldoMoneda.setText(getResources().getString(R.string.sin_saldo_disponible));
        }
    }

    public void showLoader(final boolean show) {
        pantalla.setVisibility(show? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show? View.VISIBLE : View.GONE);
    }

    public void ejecutarTransferencia(){

        String lineaOrigen = ((BaseDrawerActivity)getActivity()).obtenerLineaSeleccionada();
        String lineaDestino = numeroAmigo.getText().toString();
        String monto = montoTransferenciaTx.getText().toString();
        progress =  ProgressDialog.show(getActivity(), getResources().getString(R.string.transferencia_saldo_title),
                getResources().getString(R.string.ejecutando_operacion), true);

        SolcitarTransferenciaSaldoInd solicitud = new SolcitarTransferenciaSaldoInd(lineaOrigen, lineaDestino, monto, "");
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
            transferirSaldoRequest = new TransferirSaldoRequest(getActivity(), new JacksonConverter(objectMapper));
            String destino = formatoMiMundo(solicitud.getDestino());
            solicitud.setDestino(destino);
            activity.getSpiceManager().execute(
                    transferirSaldoRequest.solicitarInd(solicitud),
                    new TransferenciaRequestListener()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /*private DialogFragment crearDialogo() {



        return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ejecutarTransferencia();
                    }
                };
                DialogInterface.OnClickListener rechazarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.confirma_transferencia_saldo))
                        .setTitle(getResources().getString(R.string.confirmar_title));
                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
                builder.setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar), rechazarListener);
                AlertDialog dialog = builder.create();
                return dialog;
            }
        };
   }*/
    /*private DialogFragment crearDialogo(final String titulo, final String mensaje) {



        return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(mensaje).setTitle(titulo);
                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
                AlertDialog dialog = builder.create();
                return dialog;
            }
        };
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case TRANS_SAL_IND_CREAR_DIALOG_1:
                if (resultCode == Activity.RESULT_OK) {
                    //Toast.makeText(getActivity(),"Vino OK",Toast.LENGTH_SHORT).show();
                    ejecutarTransferencia();

                    // After Ok code.
                }

                break;
            case TRANS_SAL_IND_CREAR_DIALOG_2:

                break;

        }
    }
}
