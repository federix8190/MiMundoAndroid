package py.com.personal.mimundo.fragments.configuracion;

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
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.AdapterDestinosGratuitos;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptarRechazar;
import py.com.personal.mimundo.services.destinos.service.GuardarDestinosGratuitos;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.cache.destinos.DestinosGratuitosRequest;
import py.com.personal.mimundo.services.cache.destinos.ServiciosDestinosGratuitosRequest;
import py.com.personal.mimundo.services.destinos.models.Destino;
import py.com.personal.mimundo.services.lineas.models.Servicio;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 06/10/2014.
 */
public class DestinosGratuitosFragment extends Fragment {

    private String linea;

    private List<Servicio> listaServicios;
    private RecyclerView lineasDestinosView;
    private EditText nroLineaOrigenView;
    private TextView serviciosDisponibles;
    private AdapterDestinosGratuitos mAdapter;
    private ServiciosDestinosGratuitosRequest serviciosDestinosGratuitosRequest;

    private List<Destino> listaDestinos;
    private List<String> lineasDestinos;
    private DestinosGratuitosRequest destinosGratuitosRequest;

    private GuardarDestinosGratuitos guardarDestinosGratuitos;
    private Button modificarDatosBtn;
    private ProgressDialog progress;

    private ScrollView mLoginFormView1;
    private ProgressBar mProgressView;
    private RelativeLayout layout;

    // Redisenho.
    private Typeface tf1;
    private ViewStub stub;
    private RelativeLayout cardLoading;
    private boolean cargado = false;
    public static final int DESTINOS_GRATUITOS_CREAR_DIALOG_1 = 1;

    public static DestinosGratuitosFragment newInstance() {
        return new DestinosGratuitosFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_destinos_gratuitos, container, false);

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        linea = activity.obtenerLineaSeleccionada();
        lineasDestinosView = (RecyclerView) v.findViewById(R.id.lineas_destinos);
        serviciosDisponibles = (TextView) v.findViewById(R.id.servicios_disponibles);

        lineasDestinos = new ArrayList<>();
        listaDestinos = new ArrayList<>();

        nroLineaOrigenView = (EditText) v.findViewById(R.id.nroLineaOrigen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lineasDestinosView.setLayoutManager(mLayoutManager);

        Button agregarBtn = v.findViewById(R.id.cargarLineaOrigen);
        agregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nroLinea = nroLineaOrigenView.getText().toString();
                if (nroLinea != null && !nroLinea.isEmpty()) {
                    listaDestinos.add(new Destino(nroLinea));
                    lineasDestinos.add(nroLinea);
                    mAdapter = new AdapterDestinosGratuitos(getActivity(), lineasDestinos);
                    lineasDestinosView.setAdapter(mAdapter);
                    nroLineaOrigenView.setText("");
                } else {
                    Toast.makeText(getActivity(), "Debe ingresar el número de línea", Toast.LENGTH_SHORT).show();
                }
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogo = MensajesGenericosAceptarRechazar.newInstance(
                        "Confirmar",
                        getResources().getString(R.string.guardar_destinos_gratuitos),
                        "", false);
                dialogo.setTargetFragment(DestinosGratuitosFragment.this, DESTINOS_GRATUITOS_CREAR_DIALOG_1);
                dialogo.show(getActivity().getSupportFragmentManager(), "Destinos-Gratuitos");
            }
        };
        modificarDatosBtn = (Button) v.findViewById(R.id.button_guadar_destinos_gratuitos);
        modificarDatosBtn.setOnClickListener(onClickListener);

        tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.titulo_estado)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.servicios_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.destinos_title)).setTypeface(tf1);

        // Redisenho.
        stub = (ViewStub) v.findViewById(R.id.stub);
        cardLoading = (RelativeLayout) v.findViewById(R.id.servicio_estado_loading);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        serviciosDestinosGratuitosRequest = new ServiciosDestinosGratuitosRequest(getActivity(), new JacksonConverter(objectMapper));
        destinosGratuitosRequest = new DestinosGratuitosRequest(getActivity(), new JacksonConverter(objectMapper));

        mProgressView = (ProgressBar) v.findViewById(R.id.progressbar_login);
        mLoginFormView1 = (ScrollView) v.findViewById(R.id.scroll);
        layout = (RelativeLayout) v.findViewById(R.id.progressbar);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (listaServicios == null || listaServicios.size() == 0) {
            showProgress(true);
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            activity.getSpiceManager().execute(
                    serviciosDestinosGratuitosRequest.consultar(linea),
                    new ServiciosRequestListener()
            );
        }
    }

    /**
     * Carga los servicios de destinos gratuitos.
     */
    public final class ServiciosRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                listaServicios = null;
                cargarEstado();
                showProgress(false);
                Toast.makeText(getActivity(), MensajesDeUsuario.ERROR_SERVICIOS_DESTINOS_GRATUITOS, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List lista) {
            if (getActivity() != null) {
                if (lista != null) {
                    if (lista.size() > 0) {
                        listaServicios = lista;
                        cargarListaServicios();
                        cargarEstado();
                    } else {
                        listaServicios = null;
                        cargarEstado();
                        showProgress(false);
                    }
                } else {
                    listaServicios = null;
                    cargarEstado();
                    showProgress(false);
                    Toast.makeText(getActivity(), MensajesDeUsuario.ERROR_SERVICIOS_DESTINOS_GRATUITOS, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cargarEstado() {
        if (listaServicios != null && listaServicios.size() > 0) {
            if (stub != null && !cargado ) {
                if (stub.getParent() != null) {
                    stub.setLayoutResource(R.layout.linea_activada);
                    stub.inflate();
                    ((TextView) getView().findViewById(R.id.activado)).setTypeface(tf1);
                }
                cardLoading.setVisibility(View.GONE);
                cargado = true;
            }
            modificarDatosBtn.setEnabled(true);
            modificarDatosBtn.setVisibility(View.VISIBLE);
        } else {
            if (stub != null && !cargado) {
                if (stub.getParent() != null) {
                    stub.setLayoutResource(R.layout.linea_desactivada);
                    stub.inflate();
                    ((TextView) getView().findViewById(R.id.desactivado)).setTypeface(tf1);
                }
                cardLoading.setVisibility(View.GONE);
                cargado = true;
            }
            modificarDatosBtn.setVisibility(View.GONE);
            (getView().findViewById(R.id.lista_servicios_content)).setVisibility(View.GONE);
            (getView().findViewById(R.id.lista_destinos_content)).setVisibility(View.GONE);
        }
    }

    // Cargamos el spinner de servicios
    private void cargarListaServicios() {

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (activity != null) {
            activity.getSpiceManager().execute(
                    destinosGratuitosRequest.consultar(linea),
                    new DestinosGratuitosRequestListener()
            );
            String servicios = "";
            if (listaServicios != null && listaServicios.size() > 0) {
                int total = listaServicios.size();
                for (int i = 0; i < (total - 1); i++) {
                    Servicio servicio = listaServicios.get(i);
                    servicios = servicios + servicio.getCodigo() + ", ";
                }
                servicios = servicios + listaServicios.get(total - 1).getCodigo();
            }
            serviciosDisponibles.setText(servicios);
        }
    }

    /**
     * Carga los destinos gratuitos de la linea.
     */
    public final class DestinosGratuitosRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showProgress(false);
                Toast.makeText(getActivity(), MensajesDeUsuario.ERROR_DESTINOS_GRATUITOS, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List lista) {
            if (getActivity() != null) {
                if (lista != null) {
                    listaDestinos = lista;
                    inicializarListaDestinos();
                } else {
                    Toast.makeText(getActivity(), MensajesDeUsuario.ERROR_DESTINOS_GRATUITOS, Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        }
    }

    // Cargamos la lista de destinos gratuitos
    private void inicializarListaDestinos() {
        BaseDrawerActivity context = (BaseDrawerActivity) getActivity();
        if (context != null) {
            if (listaDestinos != null && listaServicios != null) {
                // Cargamos los destinos actuales de acuerdo al tipo de servicio
                mAdapter = new AdapterDestinosGratuitos(getActivity(), lineasDestinos);
                lineasDestinosView.setAdapter(mAdapter);
                for (Destino destino : listaDestinos) {
                    lineasDestinos.add(destino.getNumeroLinea());
                }
            }
        }
    }

    // Guardar lista de destinos actual
    private void guardarListaActual() {
        /*DestinosGratuitosAdapter adapter = (DestinosGratuitosAdapter) listaDestinosView.getAdapter();
        if (adapter != null) {
            int cantidad = adapter.getCount();
            if (cantidad > 0) {
                String codigoServicio = adapter.getItem(0).getCodigoServicio();
                String codigoGrupo = adapter.getItem(0).getCodigoGrupo();
                List<Destino> listaDestinosGratuitos = new ArrayList<Destino>();
                for (int i = 0; i < cantidad; i++) {
                    View view = adapter.getView(i, null, null);
                    if (view != null) {
                        EditText editText = (EditText) view.findViewById(R.id.linea_destino_field);
                        String lineaDestino = editText.getText().toString();
                        Destino destino = new Destino();
                        destino.setNumeroLinea(lineaDestino);
                        destino.setCodigoServicio(codigoServicio);
                        destino.setCodigoGrupo(codigoGrupo);
                        listaDestinosGratuitos.add(destino);
                    }
                }
                adapter = new DestinosGratuitosAdapter(getActivity(), listaDestinosGratuitos);
                String clave = codigoServicio + "-" + codigoGrupo;
                destinosPorServicio.put(clave, adapter);
            }
        }*/
    }

    private void guardarCambios() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        guardarDestinosGratuitos = new GuardarDestinosGratuitos(getActivity(), new JacksonConverter(objectMapper));

        try {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            String title = MensajesDeUsuario.TITULO_DIALOGO_MODIFICAR_DESINOS;
            String msg = MensajesDeUsuario.MENSAJE_DIALOGO_MODIFICAR_DESTINOS;
            progress = ProgressDialog.show(activity, title, msg, true);
            activity.getSpiceManager().execute(
                    guardarDestinosGratuitos.ejecutar(linea, listaDestinos),
                    new GuardarDestinosGratuitosListener()
            );
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            e.printStackTrace();
        }
    }

    public final class GuardarDestinosGratuitosListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();
                Toast.makeText(getActivity(), MensajesDeUsuario.ERROR_MODIFICAR_DESTINOS_GRATUITOS, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List lista) {
            if (getActivity() != null) {
                progress.dismiss();
                if (lista != null) {
                    List<Resultado> resultados = lista;
                    boolean exitoso = true;
                    String errorMsg = "";
                    for (Resultado r : resultados) {
                        if (!r.isExitoso()) {
                            exitoso = false;
                            errorMsg = r.getMensaje();
                        }
                    }
                    showProgress(false);

                    if (exitoso) {
                        String msg = getResources().getString(R.string.operacion_exitosa);
                        msg = msg + ". Su solicitud será procesada, en breve recibirá un mensaje de confirmación.";
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    } else {
                        String msg = "Error al actualizar servicio de destinos gratuitos. " + errorMsg;
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
                    }
                } else {
                    Toast.makeText(getActivity(), MensajesDeUsuario.ERROR_MODIFICAR_DESTINOS_GRATUITOS, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void showProgress(final boolean show) {
        layout.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView1.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case DESTINOS_GRATUITOS_CREAR_DIALOG_1:
                if (resultCode == Activity.RESULT_OK) {
                    guardarListaActual();
                    guardarCambios();
                }
                break;
        }
    }
}
