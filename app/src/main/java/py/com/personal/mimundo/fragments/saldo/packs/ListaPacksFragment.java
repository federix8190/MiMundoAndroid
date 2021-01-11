package py.com.personal.mimundo.fragments.saldo.packs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineasUsuarioRequest;
import py.com.personal.mimundo.services.cache.lineas.ObtenerPacksLineaRequest;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.packs.NodoPack;
import py.com.personal.mimundo.services.lineas.packs.RespuestaListaArbolPack;
import py.com.personal.mimundo.utils.StringUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 16/09/2014.
 */
public class ListaPacksFragment extends Fragment {

    // Selector de lineas
    private static final String OPERACION = "COMPRAR_PACKS";
    private static final String FRAGMENT_NAME = "ListaPacksFragment";
    private ObtenerLineasUsuarioRequest lineasService;

    private static final String CODIGO_RESPUESTA_OK = "0";

    private ObtenerPacksLineaRequest packsLineaRequest;

    private String linea;
    private String canal;

    private GridView listaPacksView;
    private PackItemAdapter listaPacksAdapter;
    private RespuestaListaArbolPack listaArbolPack;
    private List<NodoPack> listaPacks;
    private List<String> items;

    private LinearLayout mLoginFormView;
    private ProgressBar mProgressView;
    private RelativeLayout layout;
    private ViewStub stub;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonObject = savedInstanceState.getString("listaArbolPack");
            listaArbolPack = new Gson().fromJson(jsonObject, RespuestaListaArbolPack.class);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("listaArbolPack", new Gson().toJson(listaArbolPack));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_lista_packs, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setTitle("Packs");

        mProgressView = (ProgressBar) v.findViewById(R.id.progressbar_login);
        mLoginFormView = (LinearLayout) v.findViewById(R.id.form_login);
        layout = (RelativeLayout) v.findViewById(R.id.progressbar);
        stub = (ViewStub) v.findViewById(R.id.stub);
        showProgress(true);

        activity.setActionBar(true, FRAGMENT_NAME);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (items != null && items.size() > 0) {
                    String titulo = items.get(position);
                    NodoPack nodoPack = listaPacks.get(position);
                    if (nodoPack != null) {
                        Fragment fragment;
                        if (nodoPack.getHijos() == null || nodoPack.getHijos().length == 0) {
                            fragment = DetallePackFragment.newInstance(titulo, nodoPack.getPacks());
                        } else {
                            fragment = ListaDetallePackFragment.newInstance(titulo, nodoPack);
                        }
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        };

        listaPacksView = (GridView) v.findViewById(R.id.lista_packs);
        listaPacksView.setOnItemClickListener(listener);

        linea = ((BaseDrawerActivity) getActivity()).obtenerLineaSeleccionada();
        canal = "MIMUNDO_WEB";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineasService = new ObtenerLineasUsuarioRequest(getActivity(), new JacksonConverter(objectMapper), OPERACION);
        packsLineaRequest = new ObtenerPacksLineaRequest(getActivity(), new JacksonConverter(objectMapper));

       return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (activity.esOperacionActual(OPERACION)) {
            activity.setActionBar(true, FRAGMENT_NAME);
            linea = ((BaseDrawerActivity) getActivity()).obtenerLineaSeleccionada();
            cargarDatosLinea();
        } else {
            activity.setActionBar(false, null);
            activity.getSpiceManager().execute(lineasService.listar(), new LineasRequestListener());
        }
    }

    public final class LineasRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showProgress(false);
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List lista) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (activity != null) {
                if (lista != null && lista.size() > 0) {
                    activity.cargarListaLineas(lista, OPERACION);
                    activity.setActionBar(true, FRAGMENT_NAME);
                    List<Linea> lineas = lista;
                    System.err.println("Obtener packs Lineas : " + lista.size());
                    linea = lineas.get(0).getNumeroLinea();
                    System.err.println("Obtener packs Linea : " + linea);
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
                    cargarDatosLinea();
                } else {
                    showProgress(false);
                    mLoginFormView.setVisibility(View.GONE);
                    if (stub != null && stub.getParent() != null) {
                        stub.setLayoutResource(R.layout.widget_sin_datos);
                        stub.inflate();
                    }
                    Toast.makeText(activity, MensajesDeUsuario.MENSAJE_SIN_LINEAS_OPERACION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cargarDatosLinea() {
        Linea datosLinea = ((BaseDrawerActivity)getActivity()).obtenerDatosLineaSeleccionada();
        if (datosLinea.getTipoLinea().equals("BAM_3G")) {
            listaArbolPack = null;
            canal = "portal_cautivo";
        } else {
            canal = "MIMUNDO_WEB";
        }
        if (listaArbolPack == null) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            System.err.println("Obtener packs : " + linea + " - " + canal);
            activity.getSpiceManager().execute(packsLineaRequest.packsLinea(linea, canal), new PacksLineaRequestListener());
        } else {
            showProgress(false);
            cargarListaPacks(listaArbolPack);
        }
    }

    public final class PacksLineaRequestListener implements RequestListener<RespuestaListaArbolPack> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                System.err.println("Error al Obtener packs");
                showProgress(false);
                String msg = getResources().getString(R.string.error_obtener_packs);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                mLoginFormView.setVisibility(View.GONE);
                if (stub != null && stub.getParent() != null) {
                    stub.setLayoutResource(R.layout.widget_sin_servicio);
                    stub.inflate();
                }
            }
        }

        @Override
        public void onRequestSuccess(final RespuestaListaArbolPack datos) {
            if (getActivity() != null) {
                showProgress(false);
                System.err.println("Respuesta Obtener packs : " + datos);
                if (datos != null) {
                    if (!datos.getCodigo().equals(CODIGO_RESPUESTA_OK)) {
                        mLoginFormView.setVisibility(View.GONE);
                        if (stub != null && stub.getParent() != null) {
                            stub.setLayoutResource(R.layout.widget_sin_datos);
                            stub.inflate();
                        }
                    } else {
                        listaArbolPack = datos;
                        cargarListaPacks(datos);
                    }
                } else {
                    mLoginFormView.setVisibility(View.GONE);
                    if (stub != null && stub.getParent() != null) {
                        stub.setLayoutResource(R.layout.widget_sin_datos);
                        stub.inflate();
                    }
                    String msg = getResources().getString(R.string.error_obtener_packs);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void showProgress(final boolean show) {
        layout.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void cargarListaPacks(RespuestaListaArbolPack datos) {
        if (datos.getItems().length != 0) {
            items = new ArrayList<String>();
            listaPacks = new ArrayList<NodoPack>();
            for (NodoPack nodoPack : datos.getItems()) {
                if (!nodoPack.getDescripcion().equals("Mis suscripciones")) {
                    items.add(nodoPack.getDescripcion());
                    listaPacks.add(nodoPack);
                }
            }
            listaPacksAdapter = new PackItemAdapter(getActivity());
            listaPacksAdapter.addCollection(listaPacks);
            listaPacksView.setAdapter(listaPacksAdapter);
            listaPacksAdapter.notifyDataSetChanged();
        } else {
            mLoginFormView.setVisibility(View.GONE);
            if (stub != null && stub.getParent() != null) {
                stub.setLayoutResource(R.layout.widget_sin_datos);
                stub.inflate();
            }
        }
    }
}
