package py.com.personal.mimundo.fragments.saldo.recarga;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.AdapterSpinner;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptarRechazar;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptar;
import py.com.personal.mimundo.services.lineas.service.SolicitarRecargaContraFactura;
import py.com.personal.mimundo.services.cache.recargas.MontosRecargaContraFacturaRequest;
import py.com.personal.mimundo.services.lineas.models.ItemOpcionesRecargaContraFactura;
import py.com.personal.mimundo.services.lineas.models.RespuestaRecargaFactura;
import py.com.personal.mimundo.utils.NumbersUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class MontosRecargaFragment extends Fragment {

    // Selector de lineas
    private String linea;
    private ScrollView pantalla;
    private ProgressBar progressBar;
    private ViewStub stub;

    // Opciones para recarga contra factura.
    private Spinner opcionesRecargaContraFactura;
    private ArrayAdapter<String> adapterListaOpcionesRecargaContraFactura;
    private List<String> listaOpcionesRecargaContraFactura;
    private ItemOpcionesRecargaContraFactura montoSeleccionado = null;

    // Peticion de saldo.
    private Button botonDeSolicitud;
    private SolicitarRecargaContraFactura solicitarRecargaContraFactura;
    private ProgressDialog progress;

    // Listas.
    private RelativeLayout contenedorSpinnerDeMontos;
    private TextView msgOpcionesRecargaContraFactura;

    private List<ItemOpcionesRecargaContraFactura> listaOpcionesRecargaContraFacturaHidden;
    private MontosRecargaContraFacturaRequest montoslRequest;
    private List<ItemOpcionesRecargaContraFactura> listaDeRecargas;
    public static final int MONTOS_RECARGA_CREAR_DIALOG_1 = 1;
    public static final int MONTOS_RECARGA_CREAR_DIALOG_2 = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_recarga_contra_factura, container, false);

        pantalla = (ScrollView)v.findViewById(R.id.pantalla);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbar);
        botonDeSolicitud = (Button) v.findViewById(R.id.solicitar_recarga_contra_factura);
        stub = (ViewStub) v.findViewById(R.id.stub);

        // Spinner para opciones de recarga.
        opcionesRecargaContraFactura = (Spinner) v.findViewById(R.id.opciones_recarga_contra_factura);
        opcionesRecargaContraFactura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                montoSeleccionado = listaDeRecargas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Boton para solicitar el prestamo.
        botonDeSolicitud.setEnabled(false);
        //botonDeSolicitud.setTextColor(getResources().getColor(R.color.gris_texto_boton));
        botonDeSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment dialogo = MensajesGenericosAceptarRechazar.newInstance(
                        getResources().getString(R.string.confirmar_title),
                        getResources().getString(R.string.confirmar_rcf),
                        "", false);

                dialogo.setTargetFragment(MontosRecargaFragment.this, MONTOS_RECARGA_CREAR_DIALOG_1);
                //DialogFragment dialogo = crearDialogo();
                dialogo.show(getActivity().getSupportFragmentManager(), "Recarga Contra Factura");
            }
        });

        msgOpcionesRecargaContraFactura = (TextView)v.findViewById(R.id.msj_opciones_recarga_contra_factura);
        contenedorSpinnerDeMontos = (RelativeLayout) v.findViewById(R.id.cuerpo_recarga_saldo);

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.title_1)).setTypeface(tf1);

        Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CarroisGothic-Regular.ttf");
        msgOpcionesRecargaContraFactura.setTypeface(tf2);
        botonDeSolicitud.setTypeface(tf2);
        ((TextView) v.findViewById(R.id.tituloNumeroDestino)).setTypeface(tf2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        montoslRequest = new MontosRecargaContraFacturaRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (activity.getListaLineas() != null && activity.getListaLineas().size() > 0) {
            showLoader(true);
            linea = activity.obtenerLineaSeleccionada();
            activity.getSpiceManager().execute(montoslRequest.obtenerLista(linea), new MontosRequestListener());
        } else {
            setearMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_LINEAS);
        }
    }

    public final class SolicitudRequestListener implements RequestListener<RespuestaRecargaFactura> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();

                DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                        getResources().getString(R.string.rcf_title),
                        getResources().getString(R.string.rcf_fallo));

                dialogo.setTargetFragment(MontosRecargaFragment.this, MONTOS_RECARGA_CREAR_DIALOG_2);

                /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.rcf_title),
                        getResources().getString(R.string.rcf_fallo));*/
                dialogo.show(getActivity().getSupportFragmentManager(), "Recarga contra factura");
            }
        }

        @Override
        public void onRequestSuccess(final RespuestaRecargaFactura response) {
            if (getActivity() != null) {
                final String linea = ((BaseDrawerActivity)getActivity()).obtenerLineaSeleccionada();
                progress.dismiss();
                if (response != null && response.getExitoso() != null && !response.getExitoso().equals("false")) {
                    /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.rcf_title),
                            getResources().getString(R.string.rcf_exitosa));*/
                    DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                            getResources().getString(R.string.rcf_title),
                            getResources().getString(R.string.rcf_exitosa));

                    dialogo.setTargetFragment(MontosRecargaFragment.this, MONTOS_RECARGA_CREAR_DIALOG_2);

                    dialogo.show(getActivity().getSupportFragmentManager(), "Recarga contra factura");
                    BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
                    activity.getSpiceManager().cancel(montoslRequest.obtenerLista(linea));
                    MontosRecargaFragment fragment = new MontosRecargaFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                } else {
                    String msg = getResources().getString(R.string.rcf_fallo);
                    if (response != null && response.getMensaje() != null) {
                        msg = response.getMensaje();
                    }
                    DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                            getResources().getString(R.string.rcf_title),
                            msg);

                    dialogo.setTargetFragment(MontosRecargaFragment.this, MONTOS_RECARGA_CREAR_DIALOG_2);

                    /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.rcf_title), msg);*/
                    dialogo.show(getActivity().getSupportFragmentManager(), "Recarga contra factura");
                }
            }
        }
    }

    public final class MontosRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showLoader(false);
                String mensaje = getResources().getString(R.string.error_obtener_montos_recarga);
                if (spiceException.getCause().getMessage().contains("400")) {
                    mensaje = getResources().getString(R.string.error_linea_sin_recarga);
                }
                botonDeSolicitud.setEnabled(false);
                //botonDeSolicitud.setTextColor(getResources().getColor(R.color.gris_texto_boton));
                contenedorSpinnerDeMontos.setVisibility(View.GONE);
                msgOpcionesRecargaContraFactura.setVisibility(View.VISIBLE);
                msgOpcionesRecargaContraFactura.setText(mensaje);
                Toast.makeText(getActivity(), MensajesDeUsuario.TITULO_SIN_DATOS, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List datos) {
            if (getActivity() != null) {
                showLoader(false);
                if (datos != null) {
                    if(getView() != null) {
                        if (datos.size() > 0) {
                            listaOpcionesRecargaContraFactura = new ArrayList<>();
                            listaOpcionesRecargaContraFacturaHidden = new ArrayList<>();
                            contenedorSpinnerDeMontos.setVisibility(View.VISIBLE);
                            msgOpcionesRecargaContraFactura.setVisibility(View.GONE);
                            msgOpcionesRecargaContraFactura.setText("");
                            botonDeSolicitud.setEnabled(true);
                            //botonDeSolicitud.setTextColor(getResources().getColor(R.color.white));
                            listaDeRecargas = datos;
                            AdapterSpinner adapter = new AdapterSpinner(true, getActivity());
                            opcionesRecargaContraFactura.setAdapter(adapter);
                            for (int i = 0; i < listaDeRecargas.size(); i++) {
                                Double monto = listaDeRecargas.get(i).getMonto();
                                adapter.addItem(NumbersUtils.formatear(monto.intValue()) + " " + NumbersUtils.formatearUnidad("GUARANIES"));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            botonDeSolicitud.setEnabled(false);
                            //botonDeSolicitud.setTextColor(getResources().getColor(R.color.gris_texto_boton));
                            contenedorSpinnerDeMontos.setVisibility(View.GONE);
                            msgOpcionesRecargaContraFactura.setVisibility(View.VISIBLE);
                            msgOpcionesRecargaContraFactura.setText(getResources().getString(R.string.no_hay_opciones_recarga));
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), MensajesDeUsuario.TITULO_SIN_DATOS, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void showLoader(final boolean show) {
        pantalla.setVisibility(show? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show? View.VISIBLE : View.GONE);
    }

    private void setearMensajeFalla(String mensaje) {
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

    public void ejecutarPedirRecarga() {
        progress = ProgressDialog.show(getActivity(), getResources().getString(R.string.rcf_title),
                getResources().getString(R.string.ejecutando_operacion), true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        solicitarRecargaContraFactura = new SolicitarRecargaContraFactura(getActivity(), new JacksonConverter(objectMapper));
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        try {
            Double monto = montoSeleccionado.getMonto();
            Integer porcentaje = montoSeleccionado.getPorcentaje();
            activity.getSpiceManager().execute(
                    solicitarRecargaContraFactura.solicitar(linea, monto, porcentaje),
                    new SolicitudRequestListener()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case MONTOS_RECARGA_CREAR_DIALOG_1:
                if (resultCode == Activity.RESULT_OK) {
                    //Toast.makeText(getActivity(),"Vino OK",Toast.LENGTH_SHORT).show();
                    ejecutarPedirRecarga();

                    // After Ok code.
                } else if (resultCode == Activity.RESULT_CANCELED){
                    //Toast.makeText(getActivity(),"Vino CANCEL",Toast.LENGTH_SHORT).show();
                    // After Cancel code.
                }

                break;
            case MONTOS_RECARGA_CREAR_DIALOG_2:

                break;


        }
    }
}
