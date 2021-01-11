package py.com.personal.mimundo.fragments.saldo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;

import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptarRechazar;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptar;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineasUsuarioRequest;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.service.PedirSaldoRequest;
import py.com.personal.mimundo.utils.StringUtils;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class PedirSaldoFragment extends Fragment {

    private static final String OPERACION = "PEDIR_SALDO";
    private static final String FRAGMENT_NAME = "PedirSaldoFragment";

    public static final int PEDIR_SALDO_CREAR_DIALOG_1 = 1;
    public static final int PEDIR_SALDO_CREAR_DIALOG_2 = 2;

    private List<Linea> listaLineas;
    private PedirSaldoRequest pedirSaldoRequest;
    private ProgressDialog progress;
    private ViewStub stub;

    private ObtenerLineasUsuarioRequest lineasService;
    private RelativeLayout pantalla;
    private ProgressBar progressBar;
    private TextView tituloNumeroDestino;
    private TextView numeroAmigo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_pedir_saldo, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setTitle(getResources().getString(R.string.pedir_saldo_title));

        pantalla = (RelativeLayout)v.findViewById(R.id.pantalla);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbar);
        stub = (ViewStub) v.findViewById(R.id.stub);

        Button solicitarRecarga = (Button)v.findViewById(R.id.button_Pedir_Saldo);
        tituloNumeroDestino = (TextView) v.findViewById(R.id.tituloNumeroDestino);
        numeroAmigo = (TextView) v.findViewById(R.id.numero_pedir_saldo);

        solicitarRecarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lineaDestino = numeroAmigo.getText().toString();
                if (lineaDestino == null || lineaDestino.isEmpty()) {
                    Toast.makeText(getActivity(), MensajesDeUsuario.SIN_NUMERO_LINEA_PEDIR_SALDO, Toast.LENGTH_SHORT).show();
                } else {
                    if (esLinea(lineaDestino)) {
                        //DialogFragment dialogo = crearDialogo();

                        DialogFragment newFragment = MensajesGenericosAceptarRechazar.newInstance(
                                getString(R.string.confirmar_title),
                                getString(R.string.confirma_pedir_saldo),
                                "", false);

                        newFragment.setTargetFragment(PedirSaldoFragment.this, PEDIR_SALDO_CREAR_DIALOG_1);

                        newFragment.show(getActivity().getSupportFragmentManager(), "Pedir Saldo");
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.error_formato_linea),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.title_1)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.title_2)).setTypeface(tf1);

        Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CarroisGothic-Regular.ttf");
        tituloNumeroDestino.setTypeface(tf2);
        numeroAmigo.setTypeface(tf2);
        solicitarRecarga.setTypeface(tf2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineasService = new ObtenerLineasUsuarioRequest(getActivity(), new JacksonConverter(objectMapper), OPERACION);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (activity.esOperacionActual(OPERACION)) {
            showLoader(false);
            if (activity.getListaLineas() != null && activity.getListaLineas().size() > 0) {
                activity.cargarListaLineas(listaLineas, OPERACION);
                activity.setActionBar(true, FRAGMENT_NAME);
            } else {
                setearMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_LINEAS);
            }
        } else {
            showLoader(true);
            activity.setActionBar(false, null);
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
                    listaLineas = lista;

                    activity.cargarListaLineas(listaLineas, OPERACION);
                    activity.setActionBar(true, FRAGMENT_NAME);
                    String linea = listaLineas.get(0).getNumeroLinea();
                    activity.guardarLineaSeleccionada(linea);

                    if (activity.totalLineas <= 10) {
                        activity.seleccionarPrimerItem();
                    }

                    TextView lineaSeleccionadaView = (TextView)activity.getmDrawerList().findViewById(R.id.reservedNamedId);
                    if (lineaSeleccionadaView != null) {
                        String nroLinea = activity.obtenerLineaSeleccionada();
                        if (nroLinea != null && StringUtils.esLineaTelefonia(nroLinea)) {
                            lineaSeleccionadaView.setText("0" + nroLinea);
                        } else {
                            lineaSeleccionadaView.setText(nroLinea);
                        }
                    }
                } else {
                    setearMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_LINEAS);
                }
            }
        }
    }

    private void setearMensajeFalla(String mensaje) {
        pantalla.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_mensaje_generico);
            View inflatedView = stub.inflate();
            stub.setVisibility(View.VISIBLE);
            if (mensaje != null) {
                TextView mensajeTextView = (TextView) inflatedView.findViewById(R.id.mensaje_falla);
                mensajeTextView.setText(mensaje);
            }
        }
    }

    /**
     * Realiza el pedido de saldo.
     */
    public final class PedirSaldoRequestListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();

                DialogFragment newFragment = MensajesGenericosAceptar.newInstance(
                        getResources().getString(R.string.pedir_saldo_title),
                        getResources().getString(R.string.pedido_saldo_exitoso));

                newFragment.setTargetFragment(PedirSaldoFragment.this, PEDIR_SALDO_CREAR_DIALOG_2);

                /*crearDialogo(getResources().getString(R.string.pedir_saldo_title),
                        getResources().getString(R.string.error_pedido_saldo));

                dialogo.show(getActivity().getSupportFragmentManager(), getResources().getString(R.string.pedir_saldo_title));*/
                newFragment.show(getActivity().getSupportFragmentManager(), getResources().getString(R.string.pedir_saldo_title));
            }
        }

        @Override
        public void onRequestSuccess(final Response response) {
            if (getActivity() != null) {
                progress.dismiss();
                if(response != null && response.getStatus() == 200) {

                   // DialogFragment dialogo = crearDialogo(getResources().getString(R.string.pedir_saldo_title),
                    //        getResources().getString(R.string.pedido_saldo_exitoso));

                    DialogFragment newFragment = MensajesGenericosAceptar.newInstance(
                            getResources().getString(R.string.pedir_saldo_title),
                            getResources().getString(R.string.pedido_saldo_exitoso));
                    newFragment.setTargetFragment(PedirSaldoFragment.this, PEDIR_SALDO_CREAR_DIALOG_2);
                    newFragment.show(getActivity().getSupportFragmentManager(), "Pedir Saldo");
                } else {
                    DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                            getResources().getString(R.string.pedir_saldo_title),
                            getResources().getString(R.string.error_pedido_saldo));
                    dialogo.setTargetFragment(PedirSaldoFragment.this, PEDIR_SALDO_CREAR_DIALOG_2);
                    dialogo.show(getActivity().getSupportFragmentManager(), "Pedir Saldo");
                    /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.pedir_saldo_title),
                            getResources().getString(R.string.error_pedido_saldo));
                    dialogo.show(getActivity().getSupportFragmentManager(), "Pedir Saldo");*/
                }
            }
        }
    }

    public void showLoader(final boolean show) {
        pantalla.setVisibility(show? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show? View.VISIBLE : View.GONE);
    }

    public void ejecutarPedirSaldo(){
        String lineaOrigen = ((BaseDrawerActivity)getActivity()).obtenerLineaSeleccionada();
        String lineaDestino = numeroAmigo.getText().toString();

        if (lineaDestino.length() == 10) {
            lineaDestino = lineaDestino.substring(1,10);
        }

        progress =  ProgressDialog.show(getActivity(), MensajesDeUsuario.TITULO_DIALOGO_PEDIR_SALDO,
                MensajesDeUsuario.MENSAJE_DIALOGO_PEDIR_SALDO, true);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        pedirSaldoRequest = new PedirSaldoRequest(getActivity(), new JacksonConverter(objectMapper));
        try {
            activity.getSpiceManager().execute(
                    pedirSaldoRequest.ejecutar(lineaOrigen, lineaDestino),
                    new PedirSaldoRequestListener()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean esLinea(String linea) {
        //Regex numLineValidator = new Regex("^(09|9|5959|\\+5959)([1-9]{2})([0-9]{6})$");
        return linea.matches("^(09|9|5959|\\+5959)([1-9]{2})([0-9]{6})$");
    }

    //private void crearDialogo() {

        //DialogFragment newFragment = PedirSaldoDialogFragment.newInstance(getString(R.string.confirmar_title),
         //       getString(R.string.confirma_pedir_saldo));
        //newFragment.show(getSupportFragmentManager(), "Confirmar");

        /*return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ejecutarPedirSaldo();
                    }
                };
                DialogInterface.OnClickListener rechazarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.confirma_pedir_saldo))
                        .setTitle(getResources().getString(R.string.confirmar_title));
                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
                builder.setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar), rechazarListener);
                AlertDialog dialog = builder.create();
                return dialog;
            }
        };*/
    //}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PEDIR_SALDO_CREAR_DIALOG_1:

                if (resultCode == Activity.RESULT_OK) {

                    ejecutarPedirSaldo();
                }

                break;
            case PEDIR_SALDO_CREAR_DIALOG_2:
                if (resultCode == Activity.RESULT_OK) {

                }

                    break;
        }
    }
}


