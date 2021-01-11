package py.com.personal.mimundo.fragments.saldo.transferencias;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.AdapterSpinner;
import py.com.personal.mimundo.disenhos.GeneradorDeDialogo;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptarRechazar;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptar;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineasUsuarioRequest;
import py.com.personal.mimundo.services.cache.lineas.ObtenerSaldosLineaRequest;
import py.com.personal.mimundo.services.lineas.models.Saldos;
import py.com.personal.mimundo.services.transferencias.service.TransferirSaldoRequest;
import py.com.personal.mimundo.services.cache.LineasRequest;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.models.ListaLineas;
import py.com.personal.mimundo.services.transferencias.models.RespuestaTransferenciaCorp;
import py.com.personal.mimundo.services.transferencias.models.SolcitarTransferenciaSaldoCorp;
import py.com.personal.mimundo.utils.NumbersUtils;
import py.com.personal.mimundo.utils.StringUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class TransferenciaCorporativaPaso2_2 extends Fragment {

    private static final String ARG_PARAM1 = "parametro_lineas_origen_transferencia";
    private static final String ARG_PARAM2 = "parametro_lineas_destino_transferencia";

    private List<String> listaLineasSeleccionadasPaso1;
    private AdapterSpinner lineasAdapter;
    private List<Linea> listaLineas;
    private Spinner listaLineasView;
    private LinearLayout lineaDestinoView;
    private EditText nroLineaDestino;
    private int total;
    public static final int TRANS_CORP_PASO_2_CREAR_DIALOG_1 = 1;
    public static final int TRANS_CORP_PASO_2_CREAR_DIALOG_2 = 2;

    private List<Linea> listaLineas_other;

    private ObtenerLineasUsuarioRequest lineaRequest;
    private RelativeLayout mProgressView;
    private ScrollView scrollView;

    private List<String> listaLineasSeleccionadas;
    private ProgressDialog progress;

    private List<SolcitarTransferenciaSaldoCorp> parametrosInvocacion;

    private TextView inputMoneda;
    private TextView inputMinutos;
    private TextView inputMensajes;

    private TransferirSaldoRequest transferirSaldoRequest;

    // Elementos de redisenho.
    private RelativeLayout loader_lista_linea_destino;
    private TextView textLoading;
    private Integer posicionLineaSeleccionada;

    public static TransferenciaCorporativaPaso2_2 newInstance() {
        TransferenciaCorporativaPaso2_2 f = new TransferenciaCorporativaPaso2_2();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonObject = savedInstanceState.getString("listaLinea");
            ListaLineas lista = new Gson().fromJson(jsonObject, ListaLineas.class);
            listaLineas = lista.getLista();
            listaLineasSeleccionadasPaso1 = getArguments().getStringArrayList(ARG_PARAM1);
            getArguments().getInt(" ");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListaLineas lista = new ListaLineas();
        lista.setLista(listaLineas);
        outState.putString("listaLinea", new Gson().toJson(lista));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_destinos_transferencia2, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = false;
        activity.setActionBar(false, null);

        listaLineasSeleccionadasPaso1 = getArguments().getStringArrayList(ARG_PARAM1);
        listaLineasSeleccionadas = new ArrayList<String>();

        mProgressView = (RelativeLayout) v.findViewById(R.id.progressbar);
        scrollView = (ScrollView) v.findViewById(R.id.scrollFromulario);
        lineaDestinoView = (LinearLayout) v.findViewById(R.id.lineaDestinoView);
        nroLineaDestino = (EditText)  v.findViewById(R.id.nroLineaDestino);

        // Listener para el selector de marcas
        AdapterView.OnItemSelectedListener onLineaSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                onLineaSelect(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        listaLineasView = (Spinner) v.findViewById(R.id.lista_lineas_transferencia_spinner);
        listaLineasView.setOnItemSelectedListener(onLineaSelectedListener);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineaRequest = new ObtenerLineasUsuarioRequest(getActivity(), new JacksonConverter(objectMapper), "TRANSFERENCIA_SALDO");

        ImageView siguientePaso2 = (ImageView)v.findViewById(R.id.button_transferencia_paso2_2);

        inputMoneda = (TextView)v.findViewById(R.id.inputMoneda);
        inputMinutos = (TextView)v.findViewById(R.id.inputMinutos);
        inputMensajes = (TextView)v.findViewById(R.id.inputMensajes);

        loader_lista_linea_destino = (RelativeLayout) v.findViewById(R.id.loader_lista_linea_destino);
        textLoading =  (TextView) v.findViewById(R.id.text_loaading_lineas_transferencias);

        siguientePaso2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parametrosInvocacion = new ArrayList<SolcitarTransferenciaSaldoCorp>();
                String txtInputMoneda = inputMoneda.getText().toString();
                String txtInputMinutos = inputMinutos.getText().toString();
                String txtInputMensajes = inputMensajes.getText().toString();

                if (total <= 10) {
                    if (posicionLineaSeleccionada != null && listaLineas_other != null) {
                        listaLineasSeleccionadas.clear();
                        listaLineasSeleccionadas.add(listaLineas_other.get(posicionLineaSeleccionada).getNumeroLinea());
                    } else {
                        return;
                    }
                } else {
                    String lineaDestino = nroLineaDestino.getText().toString();
                    if (lineaDestino != null && !lineaDestino.isEmpty()) {
                        listaLineasSeleccionadas.add(lineaDestino);
                    } else {
                        return;
                    }
                }

                if (listaLineasSeleccionadasPaso1.size() <= 0 ) {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.seleccione_linea_origen_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (listaLineasSeleccionadas.size() <= 0) {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.seleccione_linea_destino_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (listaLineasSeleccionadas.size() > 1) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_linea_destino_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (txtInputMoneda.compareTo("") == 0 && txtInputMinutos.compareTo("") == 0 && txtInputMensajes.compareTo("") == 0) {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.error_seleccionar_tipo_saldo_transferencia),
                            Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    //DialogFragment dialogo = crearDialogo();

                    /*DialogFragment dialogo = TransfCorpDialogFragment.newInstance(
                            getResources().getString(R.string.confirmar_title),
                            getResources().getString(R.string.confirma_transferencia_saldo));*/

                    DialogFragment dialogo = MensajesGenericosAceptarRechazar.newInstance(
                            getResources().getString(R.string.confirmar_title),
                            getResources().getString(R.string.confirma_transferencia_saldo),
                            "", false);

                    dialogo.setTargetFragment(TransferenciaCorporativaPaso2_2.this, TRANS_CORP_PASO_2_CREAR_DIALOG_1);

                    dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");
                }
            }
        });

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.transferencia_titulo_destinos)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.text_loaading_lineas_transferencias)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.moneda_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.minutos_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.mensajes_title)).setTypeface(tf1);
        ((EditText) v.findViewById(R.id.inputMensajes)).setTypeface(tf1);
        ((EditText) v.findViewById(R.id.inputMoneda)).setTypeface(tf1);
        ((EditText) v.findViewById(R.id.inputMinutos)).setTypeface(tf1);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgress(true);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.getSpiceManager().execute(lineaRequest.getListaPaginada(), new LineaRequestListener());
    }

    public final class LineaRequestListener implements RequestListener<ListaPaginada> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showProgress(false);
                removerEstadoLoading();
                setearMensajeDeFalla();
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final ListaPaginada datos) {
            if (getActivity() != null) {
                removerEstadoLoading();
                showProgress(false);
                if (datos != null) {
                    List<Linea> lista = datos.getLista();
                    total = datos.getCantidad();
                    System.err.println("onRequestSuccess : " + lista.size());
                    System.err.println("onRequestSuccess : " + total);
                    if(lista.size() == 0) {
                        setearSinServicios();
                    } else {
                        cargarDatosServ(lista, total);
                    }
                    listaLineas = lista;
                } else {
                    setearMensajeDeFalla();
                    Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public final class TransferenciaRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();
                /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.transferencia_saldo_title),
                        MensajesDeUsuario.MENSAJE_FALLO_PETICION);*/

                DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                        getResources().getString(R.string.transferencia_saldo_title),
                        MensajesDeUsuario.MENSAJE_FALLO_PETICION);

                dialogo.setTargetFragment(TransferenciaCorporativaPaso2_2.this, TRANS_CORP_PASO_2_CREAR_DIALOG_2);

                dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");
            }
        }

        @Override
        public void onRequestSuccess(final List responseList) {

            Activity context = getActivity();
            if (context != null) {
                progress.dismiss();
                if (responseList != null) {
                    List<RespuestaTransferenciaCorp> respuestaTransf =  new ArrayList<RespuestaTransferenciaCorp>();
                    for (Object object : responseList) {
                        respuestaTransf.add((RespuestaTransferenciaCorp) object);
                    }
                    for (RespuestaTransferenciaCorp response : respuestaTransf) {
                        if (response != null && response.getExitoso().equals("true")) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.transferencia_saldo_exitosa)
                                    , Toast.LENGTH_SHORT).show();
                            /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.transferencia_saldo_title),
                                    getResources().getString(R.string.transferencia_saldo_exitosa));*/

                            DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                                    getResources().getString(R.string.transferencia_saldo_title),
                                            getResources().getString(R.string.transferencia_saldo_exitosa));

                            dialogo.setTargetFragment(TransferenciaCorporativaPaso2_2.this, TRANS_CORP_PASO_2_CREAR_DIALOG_2);

                            dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");

                        } else if (response != null) {
                            /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.transferencia_saldo_title),
                                    getResources().getString(R.string.error_transferencia_saldo) + " : " + response.getMensaje());*/

                            DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                                    getResources().getString(R.string.transferencia_saldo_title),
                                    getResources().getString(R.string.error_transferencia_saldo) + " : " + response.getMensaje());

                            dialogo.setTargetFragment(TransferenciaCorporativaPaso2_2.this, TRANS_CORP_PASO_2_CREAR_DIALOG_2);

                            dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");
                        } else {
                            /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.transferencia_saldo_title),
                                    getResources().getString(R.string.error_transferencia_saldo));*/

                            DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                                    getResources().getString(R.string.transferencia_saldo_title),
                                    getResources().getString(R.string.error_transferencia_saldo));

                            dialogo.setTargetFragment(TransferenciaCorporativaPaso2_2.this, TRANS_CORP_PASO_2_CREAR_DIALOG_2);

                            dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia de Saldo");
                        }
                    }
                }  else {
                    /*DialogFragment dialogo = GeneradorDeDialogo.crearDialogo(
                            getResources().getString(R.string.transferencia_saldo_title),
                            getResources().getString(R.string.error_transferir_saldo),
                            getResources().getString(R.string.alta_usuario_btn_aceptar));*/
                    DialogFragment dialogo = GeneradorDeDialogo.GeneradorDialogFragment.newInstance(
                            getResources().getString(R.string.transferencia_saldo_title),
                            getResources().getString(R.string.error_transferir_saldo),
                            getResources().getString(R.string.alta_usuario_btn_aceptar));

                    dialogo.show(getActivity().getSupportFragmentManager(), "Transferencia Fallida");
                }
            }
        }
    }

    private void cargarParametrosSolitudTransferencia(List<String>listaLineaOrigen,List<String> listaLineaDestino,
            String inputMoneda, String inputMinutos, String inputMensajes) {

        SolcitarTransferenciaSaldoCorp solicitud;
        for (String lineaOrigen : listaLineaOrigen) {
            for (String lineaDestino : listaLineaDestino) {
                if (inputMoneda.compareTo("") != 0 ) {
                    solicitud = new SolcitarTransferenciaSaldoCorp(lineaOrigen, lineaDestino, inputMoneda ,"GUA", "");
                    parametrosInvocacion.add(solicitud);
                }

                if (inputMinutos.compareTo("") != 0) {
                    solicitud = new SolcitarTransferenciaSaldoCorp(lineaOrigen, lineaDestino, inputMinutos ,"MIN", "");
                    parametrosInvocacion.add(solicitud);
                }

                if (inputMensajes.compareTo("") != 0) {
                    solicitud = new SolcitarTransferenciaSaldoCorp(lineaOrigen, lineaDestino, inputMensajes ,"SMS", "");
                    parametrosInvocacion.add(solicitud);
                }
            }
        }
    }

    private void removerEstadoLoading() {
        loader_lista_linea_destino.setVisibility(View.GONE);
        listaLineasView.setVisibility(View.VISIBLE);
    }

    private void setearMensajeDeFalla() {
        textLoading.setText(getResources().getString(R.string.error_obtener_datos));
    }

    private void cargarDatosServ(List<Linea> datos, int total) {
        if (total > 10) {
            //nroLineaDestino.setVisibility(View.VISIBLE);
            lineaDestinoView.setVisibility(View.VISIBLE);
        } else {
            listaLineasView.setVisibility(View.VISIBLE);
            listaLineas_other = datos;
            lineasAdapter = new AdapterSpinner(true, getActivity());
            for (Linea linea : datos) {
                if (StringUtils.esLineaTelefonia(linea.getNumeroLinea())) {
                    lineasAdapter.addItem("0" + linea.getNumeroLinea());
                } else {
                    lineasAdapter.addItem(linea.getNumeroLinea());
                }
            }
            listaLineasView.setAdapter(lineasAdapter);
        }
    }

    private void setearSinServicios() {
        textLoading.setText(getResources().getString(R.string.sin_servicio));
    }

    private void sinLineas() {
        textLoading.setText(getResources().getString(R.string.no_hay_lineas));
    }

    private void onLineaSelect(int position) {
        posicionLineaSeleccionada = position;
    }

    public void ejecutarTransferencia(){
        parametrosInvocacion = new ArrayList<SolcitarTransferenciaSaldoCorp>();
        String txtInputMoneda = inputMoneda.getText().toString();
        String txtInputMinutos = inputMinutos.getText().toString();
        String txtInputMensajes = inputMensajes.getText().toString();
        progress =  ProgressDialog.show(getActivity(), getResources().getString(R.string.transferencia_saldo_title),
                getResources().getString(R.string.ejecutando_operacion), true);

        cargarParametrosSolitudTransferencia(
                listaLineasSeleccionadasPaso1,
                listaLineasSeleccionadas,
                txtInputMoneda,
                txtInputMinutos,
                txtInputMensajes
        );

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
            transferirSaldoRequest = new TransferirSaldoRequest(getActivity(), new JacksonConverter(objectMapper));
            activity.getSpiceManager().execute(
                    transferirSaldoRequest.solicitarCorp(parametrosInvocacion),
                    new TransferenciaRequestListener()
            );
        } catch (Exception e) {
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

        DialogFragment newFragment = TransfCorporativaDialogFragment.newInstance(
                titulo,
                mensaje);

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
            case TRANS_CORP_PASO_2_CREAR_DIALOG_1:

                if (resultCode == Activity.RESULT_OK) {

                    ejecutarTransferencia();

                } else if (resultCode == Activity.RESULT_CANCELED){
                    //Toast.makeText(getActivity(),"Vino CANCEL",Toast.LENGTH_SHORT).show();
                    // After Cancel code.
                }
                break;
            case TRANS_CORP_PASO_2_CREAR_DIALOG_2:

                break;

        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void showProgress(final boolean show) {
        //scrollView.setVisibility(show ? View.VISIBLE : View.GONE);
        //mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}


