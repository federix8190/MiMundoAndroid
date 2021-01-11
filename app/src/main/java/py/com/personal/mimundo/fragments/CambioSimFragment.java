package py.com.personal.mimundo.fragments;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.AdapterLineasTransferencia;
import py.com.personal.mimundo.adapters.AdapterSpinner;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.saldo.transferencias.TransferenciaCorporativaPaso1;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineasUsuarioRequest;
import py.com.personal.mimundo.services.cambiodesim.CambioDeSimParametros;
import py.com.personal.mimundo.services.cambiodesim.CambioSimRequest;
import py.com.personal.mimundo.services.cambiodesim.TipoDeCambio;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.utils.StringUtils;
import retrofit.converter.JacksonConverter;
import py.com.personal.mimundo.activities.CustomScannerActivity;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 18/05/2016.
 */
public class CambioSimFragment  extends Fragment {

    // TODO: Ver que operacion usar aca
    private static final String OPERACION = "PEDIR_SALDO";
    private static final String FRAGMENT_NAME = "CambioSimFragment";

    private List<Linea> listaLineas;
    private CambioSimRequest cambioSimRequest;
    private ProgressDialog progress;
    private ViewStub stub;

    private ObtenerLineasUsuarioRequest lineasService;
    private RelativeLayout pantalla;
    private RelativeLayout progressBar;
    private TextView lineaSeleccionadaTextView;
    private TextView tituloSimCard;
    private TextView simCardTextView;

    private TextView nroLineaTitleTextView;
    private EditText nroLineaView;
    private RelativeLayout cargarLineaView;

    private ObtenerLineasUsuarioRequest lineaRequest;
    private Button agregarBtn;

    private List<String> codTipoDeCambio;
    private List<String> descTipoDeCambio;
    private Spinner tipoDeCambio;
    private ImageButton scan;
    private String toast;
    private EditText edtxtSim;
    public static final int CAMBIO_SIM_CREAR_DIALOG_1 = 1;
    public static final int CAMBIO_SIM_CREAR_DIALOG_2 = 2;
    /*private ProgressBar progressBarSim;
    private ImageView imgSimAceptado;
    private ImageView imgSimRechazado;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_cambio_de_sim, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setTitle(getResources().getString(R.string.cambio_de_sim_title));
        activity.setActionBar(false, null);

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.title_1)).setTypeface(tf1);
        Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CarroisGothic-Regular.ttf");

        pantalla = (RelativeLayout)v.findViewById(R.id.pantalla);
        progressBar = (RelativeLayout) v.findViewById(R.id.progressbar);
        stub = (ViewStub) v.findViewById(R.id.stub);
        scan = (ImageButton) v.findViewById(R.id.botonEscanearSim); //AGREGADO
        edtxtSim = (EditText) v.findViewById(R.id.simCard);
        nroLineaTitleTextView = (TextView) v.findViewById(R.id.nroLineaTitle);
        nroLineaView = (EditText) v.findViewById(R.id.nroLineaInput);
        cargarLineaView = (RelativeLayout) v.findViewById(R.id.cargarLinea);
        /*progressBarSim = (ProgressBar) v.findViewById(R.id.progress3_sim);
        imgSimRechazado = (ImageView) v.findViewById(R.id.imgSimRechazado_sim);
        imgSimAceptado = (ImageView) v.findViewById(R.id.imgSimAceptado_sim);*/

        Button cambioDeSim = (Button)v.findViewById(R.id.buttonCambioSim);
        cambioDeSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String simCard = simCardTextView.getText().toString();
                if (simCard == null || simCard.isEmpty() || simCard.length() != 13) {
                    Toast.makeText(getActivity(), MensajesDeUsuario.INGRESAR_SIM_CARD, Toast.LENGTH_SHORT).show();
                } else {
                    //DialogFragment dialogo = crearDialogo();
                    DialogFragment dialogo = MensajesGenericosAceptarRechazar.newInstance(
                            getResources().getString(R.string.confirmar_title),
                            getResources().getString(R.string.confirma_cambio_sim),
                            "", false);
                    dialogo.setTargetFragment(CambioSimFragment.this,CAMBIO_SIM_CREAR_DIALOG_1);
                    dialogo.show(getActivity().getSupportFragmentManager(), "Cambio de Sim");
                }
            }
        });
        cambioDeSim.setTypeface(tf2);

        lineaSeleccionadaTextView = (TextView) v.findViewById(R.id.numro_linea_seleccionada);
        lineaSeleccionadaTextView.setTypeface(tf2);
        tituloSimCard = (TextView) v.findViewById(R.id.tituloSimCard);
        tituloSimCard.setTypeface(tf2);
        simCardTextView = (TextView) v.findViewById(R.id.simCard);
        simCardTextView.setTypeface(tf2);

        tipoDeCambio = (Spinner) v.findViewById(R.id.tipo_cambio_sim_field);
        ((TextView) v.findViewById(R.id.tipo_cambio_sim_label)).setTypeface(tf2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineasService = new ObtenerLineasUsuarioRequest(getActivity(), new JacksonConverter(objectMapper), OPERACION);
        cambioSimRequest = new CambioSimRequest(getActivity(), new JacksonConverter(objectMapper));

        lineaRequest = new ObtenerLineasUsuarioRequest(getActivity(), new JacksonConverter(objectMapper), "PEDIR_SALDO");

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });

        agregarBtn = v.findViewById(R.id.cargarLineaOrigen);
        agregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nroLinea = nroLineaView.getText().toString();
                simCardTextView.setEnabled(false);
                scan.setEnabled(false);
                if (nroLinea != null && !nroLinea.isEmpty()) {
                    agregarBtn.setText("Cargando");
                    agregarBtn.setEnabled(false);
                    validarLinea(nroLinea);
                } else {
                    Toast.makeText(getActivity(), "Debe ingresar el número de línea", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
            toast = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showLoader(true);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.getSpiceManager().execute(lineaRequest.getListaPaginada(), new LineaRequestListener());
        /*if (activity.esOperacionActual(OPERACION)) {
            showLoader(false);
            if (activity.getListaLineas() != null && activity.getListaLineas().size() > 0) {
                activity.cargarListaLineas(listaLineas, OPERACION);
                activity.setActionBar(true, FRAGMENT_NAME);
                String lineaSeleccionada = activity.obtenerLineaSeleccionada();
                if (lineaSeleccionada != null && StringUtils.esLineaTelefonia(lineaSeleccionada)) {
                    lineaSeleccionadaTextView.setText("Linea Seleccionada : " + "0" + lineaSeleccionada);
                } else {
                    lineaSeleccionadaTextView.setText("Linea Seleccionada : " + lineaSeleccionada);
                }
                if (activity.lineaSeleccionada) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
                    activity.getSpiceManager().execute(
                            cambioSimRequest.obtenerTiposDeCambio(lineaSeleccionada),
                            new TipoDeCambioRequestListener()
                    );
                }
            } else {
                setearMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_LINEAS);
            }
        } else {
            showLoader(true);
            activity.setActionBar(false, null);
            activity.getSpiceManager().execute(lineasService.listar(), new LineasRequestListener());
        }*/
    }

    public final class LineaRequestListener implements RequestListener<ListaPaginada> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showLoader(false);
                setearMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_LINEAS);
            }
        }

        @Override
        public void onRequestSuccess(final ListaPaginada datos) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (getActivity() != null) {
                showLoader(false);
                if (datos != null) {
                    List<Linea> lista = datos.getLista();
                    int total = datos.getCantidad();
                    if (total > 10) {
                        activity.setActionBar(false, null);
                        nroLineaTitleTextView.setVisibility(View.VISIBLE);
                        cargarLineaView.setVisibility(View.VISIBLE);
                        simCardTextView.setEnabled(false);
                        scan.setEnabled(false);
                        //setearSinServicios();
                    } else {
                        activity.setActionBar(true, "AtencionPersonalFragment");
                        lineaSeleccionadaTextView.setVisibility(View.VISIBLE);
                        activity.cargarListaLineas(lista, OPERACION);
                        activity.setActionBar(true, FRAGMENT_NAME);
                        String lineaSeleccionada = activity.obtenerLineaSeleccionada();
                        if (lineaSeleccionada != null && StringUtils.esLineaTelefonia(lineaSeleccionada)) {
                            lineaSeleccionadaTextView.setText("Linea Seleccionada : " + "0" + lineaSeleccionada);
                        } else {
                            lineaSeleccionadaTextView.setText("Linea Seleccionada : " + lineaSeleccionada);
                        }
                        System.err.println("Linea seleccionada : " + lineaSeleccionada + " - " + activity.lineaSeleccionada);
                        if (lineaSeleccionada != null && !lineaSeleccionada.isEmpty()) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
                            activity.getSpiceManager().execute(
                                    cambioSimRequest.obtenerTiposDeCambio(lineaSeleccionada),
                                    new TipoDeCambioRequestListener()
                            );
                        }
                        //cargarDatosServ(lista, total);
                    }
                    listaLineas = lista;
                } else {
                    setearMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_LINEAS);
                }
            }
        }
    }

    public void validarLinea(String nroLinea) {
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.getSpiceManager().execute(lineaRequest.datosLinea(nroLinea), new DatosLineaRequestListener());
    }

    public final class DatosLineaRequestListener implements RequestListener<Linea> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            agregarBtn.setText("Cargar");
            agregarBtn.setEnabled(true);
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "No puede ver los datos de la linea de otro titular", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Linea datos) {
            agregarBtn.setText("Cargar");
            agregarBtn.setEnabled(true);
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (getActivity() != null) {
                showLoader(false);
                if (datos != null) {
                    simCardTextView.setEnabled(true);
                    scan.setEnabled(true);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
                    activity.getSpiceManager().execute(
                            cambioSimRequest.obtenerTiposDeCambio(datos.getNumeroLinea()),
                            new TipoDeCambioRequestListener()
                    );
                } else {
                    Toast.makeText(getActivity(), "No puede ver los datos de la linea de otro titular", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void scanFromFragment() {
        IntentIntegrator.forSupportFragment(this).setCaptureActivity((CustomScannerActivity.class)).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelado";
            } else {
                toast = "Resultado obtenido: " + result.getContents();
                edtxtSim.setText(result.getContents().substring(7, result.getContents().length()));
               // new ValidarSimTask().execute(edtxtSim.getText().toString());
            }
            // At this point we may or may not have a reference to the activity
            displayToast();
        }

        switch(requestCode) {

            case CAMBIO_SIM_CREAR_DIALOG_1:
                if (resultCode == Activity.RESULT_OK) {
                    ejecutarCambioDeSim();
                }

                break;

            case CAMBIO_SIM_CREAR_DIALOG_2:
                break;

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
                    TextView lineaSeleccionadaView = (TextView) activity.getmDrawerList().findViewById(R.id.reservedNamedId);
                    if (lineaSeleccionadaView != null) {
                        String lineaSeleccionada = activity.obtenerLineaSeleccionada();
                        if (lineaSeleccionada != null && StringUtils.esLineaTelefonia(lineaSeleccionada)) {
                            lineaSeleccionadaTextView.setText("Linea Seleccionada : " + "0" + lineaSeleccionada);
                            lineaSeleccionadaView.setText("0" + lineaSeleccionada);
                        } else {
                            lineaSeleccionadaTextView.setText("Linea Seleccionada : " + lineaSeleccionada);
                            lineaSeleccionadaView.setText(lineaSeleccionada);
                        }
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
                        activity.getSpiceManager().execute(
                                cambioSimRequest.obtenerTiposDeCambio(lineaSeleccionada),
                                new TipoDeCambioRequestListener()
                        );
                    }
                } else {
                    setearMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_LINEAS);
                }
            }
        }
    }

    /**
     * Obtiene los tipos de documento.
     */
    public final class TipoDeCambioRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "No se pudo obtener los tipos de Cambio", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final List lista) {
            if (lista != null) {
                List<TipoDeCambio> tipos = lista;
                cargarTiposDeCambio(tipos);
            } else {
                Toast.makeText(getActivity(), "No se pudo obtener los tipos de Cambio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Cargamos el spinner de los tipos de cambio
    private void cargarTiposDeCambio(List<TipoDeCambio> lista) {
        codTipoDeCambio = new ArrayList<>();
        descTipoDeCambio = new ArrayList<>();
        for (TipoDeCambio tipo : lista) {
            codTipoDeCambio.add(tipo.getCodigo());
            descTipoDeCambio.add(tipo.getDescripcion());
        }
        AdapterSpinner adapter = new AdapterSpinner(true, getActivity());
        for (String item : descTipoDeCambio) {
            adapter.addItem(item);
        }
        tipoDeCambio.setAdapter(adapter);
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

    public final class CambioDeSimRequestListener implements RequestListener<Resultado> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();
                DialogFragment dialogo = crearDialogoInformativo(getResources().getString(R.string.cambio_de_sim_title),
                        getResources().getString(R.string.error_operacion));
                dialogo.show(getActivity().getSupportFragmentManager(), "Cambio de Sim");
            }
        }

        @Override
        public void onRequestSuccess(final Resultado resultado) {
            if (getActivity() != null) {
                progress.dismiss();
                String mensaje = getResources().getString(R.string.error_operacion);
                if (resultado != null) {
                    if (resultado.isExitoso()) {
                        mensaje = getResources().getString(R.string.operacion_exitosa);
                    } else {
                        mensaje = resultado.getMensaje();
                    }
                }
                DialogFragment dialogo = crearDialogoInformativo("Cambio de SIM", mensaje);
                dialogo.show(getActivity().getSupportFragmentManager(), "Cambio de Sim");
            }
        }
    }

    public void showLoader(final boolean show) {
        pantalla.setVisibility(show? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show? View.VISIBLE : View.GONE);
    }

    /*private DialogFragment crearDialogo() {
        return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ejecutarCambioDeSim();
                    }
                };
                DialogInterface.OnClickListener rechazarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.confirma_cambio_sim))
                        .setTitle(getResources().getString(R.string.confirmar_title));
                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
                builder.setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar), rechazarListener);
                AlertDialog dialog = builder.create();
                return dialog;
            }
        };
    }*/

    private void ejecutarCambioDeSim() {

        if (codTipoDeCambio == null || codTipoDeCambio.size() == 0) {
            Toast.makeText(getActivity(), "No se pudo obtener los tipos de Cambio", Toast.LENGTH_SHORT).show();
            return;
        }
        String lineaOrigen = ((BaseDrawerActivity)getActivity()).obtenerLineaSeleccionada();
        String simCard = simCardTextView.getText().toString();
        int tipoDeCambioSelected = tipoDeCambio.getSelectedItemPosition();
        String codigoTipoDeCambio = codTipoDeCambio.get(tipoDeCambioSelected);

        progress =  ProgressDialog.show(getActivity(), MensajesDeUsuario.TITULO_CAMBIO_DE_SIM,
                getResources().getString(R.string.ejecutando_operacion), true);

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        CambioDeSimParametros parametros = new CambioDeSimParametros();
        parametros.setLineaOrigen(lineaOrigen);
        parametros.setSimDestino(simCard);
        parametros.setCodigoTipoCambio(codigoTipoDeCambio);
        try {
            activity.getSpiceManager().execute(
                    cambioSimRequest.procesar(lineaOrigen, parametros),
                    new CambioDeSimRequestListener()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DialogFragment crearDialogoInformativo(final String titulo, final String mensaje) {
        DialogFragment dialogo = MensajesGenericosAceptar.newInstance(titulo, mensaje);
        dialogo.setTargetFragment(CambioSimFragment.this, CAMBIO_SIM_CREAR_DIALOG_2);

        return dialogo;

        /*return new DialogFragment() {
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
        };*/
    }

}
