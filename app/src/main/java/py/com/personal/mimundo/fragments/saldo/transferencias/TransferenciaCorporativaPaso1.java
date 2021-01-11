package py.com.personal.mimundo.fragments.saldo.transferencias;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.AdapterLineasTransferencia;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.cache.LineasRequest;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineaRequest;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineasUsuarioRequest;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.models.ListaLineas;
import py.com.personal.mimundo.utils.ListUtils;
import py.com.personal.mimundo.utils.PreferenceUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class TransferenciaCorporativaPaso1 extends Fragment {

    private static final String ARG_PARAM1 = "parametro_lineas_origen_transferencia";
    private static final String ARG_PARAM3 = "tipo_lineas_origen_transferencia";

    private ListaLineaAdapter lineasAdapter;
    private List<Linea> listaLineas;
    private int total;
    private ListView listaLineasView;
    private List<ElementoCheck> listaCheck;
    private List<Linea> listaLineas_other;

    private ObtenerLineasUsuarioRequest lineaRequest;
    private String linea;
    private LinearLayout seleccionContent;
    private LinearLayout ingresarLineaContent;

    private RecyclerView lineasOrigenView;
    private EditText nroLineaOrigenView;
    AdapterLineasTransferencia mAdapter;

    private List<String> listaLineasSeleccionadas;
    private List<String> tipoLineasSeleccionadas;

    private ScrollView mLoginFormView1;
    private ImageView mLoginFormView2;
    private ProgressBar mProgressView;
    private RelativeLayout layout;

    public static TransferenciaCorporativaPaso1 newInstance() {
        TransferenciaCorporativaPaso1 f = new TransferenciaCorporativaPaso1();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonObject = savedInstanceState.getString("listaLinea");
            ListaLineas lista = new Gson().fromJson(jsonObject, ListaLineas.class);
            listaLineas = lista.getLista();
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
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_origenes_transferencia, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setActionBar(false, "TransferenciaCorporativaPaso1");
        activity.setTitle("Transferencia de saldo");
        linea = activity.obtenerLineaSeleccionada();

        System.err.println("Transferencia Paso 1");

        seleccionContent = (LinearLayout) v.findViewById(R.id.seleccion_content);
        ingresarLineaContent = (LinearLayout) v.findViewById(R.id.ingresar_linea_content);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineaRequest = new ObtenerLineasUsuarioRequest(getActivity(), new JacksonConverter(objectMapper), "TRANSFERENCIA_SALDO");

        listaLineasView = (ListView) v.findViewById(R.id.lista_lineas_transferencia);
        listaLineasSeleccionadas = new ArrayList<String>();
        tipoLineasSeleccionadas = new ArrayList<String>();
        listaLineasView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ElementoCheck seleccionado = lineasAdapter.getItem(position);
                if(seleccionado.getCheckBox().isChecked()){
                      seleccionado.getCheckBox().setChecked(false);
                }else{
                    seleccionado.getCheckBox().setChecked(true);
                }
            }
        });

        ImageView siguientePaso1 = (ImageView)v.findViewById(R.id.button_transferencia_paso1);
        siguientePaso1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < lineasAdapter.getCount(); i++) {
                    ElementoCheck seleccionado = lineasAdapter.getItem(i);
                    if (seleccionado.getCheckBox().isChecked()) {
                        if (listaLineasSeleccionadas.indexOf(seleccionado.getLinea().getNumeroLinea()) < 0) {
                            listaLineasSeleccionadas.add(seleccionado.getLinea().getNumeroLinea());
                            tipoLineasSeleccionadas.add(seleccionado.getLinea().getClaseComercial());
                        }
                    } else {
                        listaLineasSeleccionadas.remove(seleccionado.getLinea().getNumeroLinea());
                    }
                }

                if (listaLineasSeleccionadas.size() <= 0) {
                    String msgError = getResources().getString(R.string.seleccione_una_linea);
                    Toast.makeText(getActivity(), msgError, Toast.LENGTH_SHORT).show();
                } else if (listaLineasSeleccionadas.size() == 1) {
                    mostrarSiguientePantalla(new TransferenciaCorporativaPaso2_1());
                } else if (listaLineasSeleccionadas.size() > 1) {
                    mostrarSiguientePantalla(new TransferenciaCorporativaPaso2_2());
                }
            }
        });

        mProgressView = (ProgressBar) v.findViewById(R.id.progressbar_login);
        mLoginFormView1 = (ScrollView) v.findViewById(R.id.scroll_transferencia_origen);
        mLoginFormView2 = (ImageView) v.findViewById(R.id.button_transferencia_paso1);
        layout = (RelativeLayout) v.findViewById(R.id.progressbar);

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.selecionar_origenes_title)).setTypeface(tf1);

        lineasOrigenView = (RecyclerView) v.findViewById(R.id.lineas_origen);
        nroLineaOrigenView = (EditText) v.findViewById(R.id.nroLineaOrigen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lineasOrigenView.setLayoutManager(mLayoutManager);

        Button agregarBtn = v.findViewById(R.id.cargarLineaOrigen);
        agregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nroLinea = nroLineaOrigenView.getText().toString();
                if (nroLinea != null && !nroLinea.isEmpty()) {
                    validarLinea(nroLinea);
                }
            }
        });

        showProgress(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (listaLineas == null) {
            setearEstadoLoadingServ();
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            activity.getSpiceManager().execute(lineaRequest.getListaPaginada(), new LineaRequestListener());
        } else {
            showProgress(false);
            if (listaLineas.size() > 0) {
                cargarDatosServ(listaLineas, total);
            } else {
                setearSinServicios();
            }
        }
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

    private void mostrarSiguientePantalla(Fragment fragment) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, (ArrayList<String>) listaLineasSeleccionadas);
        args.putStringArrayList(ARG_PARAM3, (ArrayList<String>) tipoLineasSeleccionadas);
        args.putInt("totalLineas ", total);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    private void cargarListaCheck(List<Linea> listaLienasCargar){
        listaCheck = new ArrayList<>();
        for(Linea linea : listaLienasCargar){
            ElementoCheck elemento = new ElementoCheck();
            elemento.setLinea(linea);
            listaCheck.add(elemento);
        }
    }

    private void setearEstadoLoadingServ() {
        Linea loading = new Linea();
        loading.setNumeroLinea(MensajesDeUsuario.TITULO_LOADING);
        listaLineas_other =  new ArrayList<>();
        listaLineas_other.add(loading);
        cargarListaCheck(listaLineas_other);
        lineasAdapter = new ListaLineaAdapter(getActivity(), listaCheck);
        listaLineasView.setAdapter(lineasAdapter);
        lineasAdapter.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaLineasView, 0);
    }

    private void removerEstadoLoading() {
        if (listaLineas_other.size() > 0) {
            listaLineas_other.remove(0);
        }
        lineasAdapter.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaLineasView, 0);
    }

    private void setearMensajeDeFalla() {
        Linea loading = new Linea();
        loading.setNumeroLinea(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
        listaLineas_other =  new ArrayList<>();
        listaLineas_other.add(loading);
        cargarListaCheck(listaLineas_other);
        lineasAdapter = new ListaLineaAdapter(getActivity(), listaCheck);
        listaLineasView.setAdapter(lineasAdapter);
        lineasAdapter.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaLineasView, 0);
    }

    private void cargarDatosServ(List<Linea> datos, int total) {
        if (total > 10) {
            System.err.println("Mas de 10 lineas");
            ingresarLineaContent.setVisibility(View.VISIBLE);
            //mAdapter = new AdapterLineasTransferencia(getActivity());
            //lineasOrigenView.setAdapter(mAdapter);
        } else {
            System.err.println("Menos de 10 lineas");
            seleccionContent.setVisibility(View.VISIBLE);
            listaLineas_other = datos;
            cargarListaCheck(listaLineas_other);
            lineasAdapter = new ListaLineaAdapter(getActivity(), listaCheck);
            listaLineasView.setAdapter(lineasAdapter);
            lineasAdapter.notifyDataSetChanged();
            ListUtils.setListViewHeightBasedOnChildren(listaLineasView, 0);
        }
    }

    private void setearSinServicios() {
        Linea vacio = new Linea();
        vacio.setNumeroLinea(MensajesDeUsuario.TITULO_LINEA_SIN_SERVICIOS);
        listaLineas_other =  new ArrayList<>();
        listaLineas_other.add(vacio);
        cargarListaCheck(listaLineas_other);
        lineasAdapter = new ListaLineaAdapter(getActivity(), listaCheck);
        listaLineasView.setAdapter(lineasAdapter);
        lineasAdapter.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaLineasView, 0);
    }

    public void validarLinea(String nroLinea) {
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.getSpiceManager().execute(lineaRequest.datosLinea(nroLinea), new DatosLineaRequestListener());
    }

    public final class DatosLineaRequestListener implements RequestListener<Linea> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "No puede ver los datos de la linea de otro titular", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Linea datos) {
            if (getActivity() != null) {
                removerEstadoLoading();
                showProgress(false);
                if (datos != null) {
                    listaLineasSeleccionadas.add(datos.getNumeroLinea());
                    tipoLineasSeleccionadas.add(datos.getClaseComercial());
                    mAdapter = new AdapterLineasTransferencia(getActivity(), listaLineasSeleccionadas);
                    lineasOrigenView.setAdapter(mAdapter);
                    nroLineaOrigenView.setText("");
                } else {
                    Toast.makeText(getActivity(), "No puede ver los datos de la linea de otro titular", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void showProgress(final boolean show) {
        layout.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView1.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView2.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
