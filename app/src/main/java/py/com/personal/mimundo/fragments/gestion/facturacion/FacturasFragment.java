package py.com.personal.mimundo.fragments.gestion.facturacion;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.cache.facturacion.FacturasGrupoRequest;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineaRequest;
import py.com.personal.mimundo.services.grupos.models.Factura;
import py.com.personal.mimundo.services.grupos.models.ListaFacturas;
import py.com.personal.mimundo.services.lineas.models.Linea;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 06/08/2014.
 */
public class FacturasFragment extends Fragment {

    // Codigo de grupo.
    private String codigoGrupo;
    private String linea;
    private static final String FRAGMENT_NAME = "FacturasFragment";

    private static final int CANTIDAD_REGISTROS = 10;
    private int cantidadTotal;
    private int inicio = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    // Lista de facturas.
    private RecyclerView listaFacturasGruposFacturacionView;
    private FacturasGrupoAdapter mAdapter;
    private List<Factura> facturas;

    private ProgressBar progressBar;

    private ViewStub stub;

    // Peticion de facturas de grupo de facturacion.
    private FacturasGrupoRequest facturasGrupoRequest;
    private ObtenerLineaRequest lineasRequest;

    public static FacturasFragment newInstance(String codigoGrupo) {
        FacturasFragment f = new FacturasFragment();
        //f.codigoGrupo = codigoGrupo;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicio = 0;
        cantidadTotal = 0;
        isLoading = false;
        isLastPage = false;
        facturas = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.err.println("onSaveInstanceState facturas : " + facturas);
        /*ListaFacturas lista = new ListaFacturas();
        lista.setLista(facturas);
        //outState.putString("codigoGrupo", codigoGrupo);
        outState.putString("listaFacturas", new Gson().toJson(lista));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.lista_facturas, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        linea = activity.obtenerLineaSeleccionada();
        activity.salirApp = true;
        System.err.println("onCreateView facturas : " + facturas);

        // Lista de facturas.
        listaFacturasGruposFacturacionView = (RecyclerView) v.findViewById(R.id.listaFacturasGruposFacturacion);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaFacturasGruposFacturacionView.setLayoutManager(mLayoutManager);
        listaFacturasGruposFacturacionView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new FacturasGrupoAdapter(getActivity());
        listaFacturasGruposFacturacionView.setAdapter(mAdapter);

        progressBar = (ProgressBar)v.findViewById(R.id.progressbar_UltimasFacturas);
        stub = (ViewStub) v.findViewById(R.id.stub);

        listaFacturasGruposFacturacionView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= CANTIDAD_REGISTROS) {
                        loadMoreItems();
                    }
                }
            }
        });

        // Se solicitan los datos.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        facturasGrupoRequest = new FacturasGrupoRequest(getActivity(), new JacksonConverter(objectMapper));
        lineasRequest = new ObtenerLineaRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        setearMensajeLoading();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.lineaSeleccionada = false;
        activity.setTitle("Facturas");
        activity.setActionBar(true, FRAGMENT_NAME);
        if (facturas.size() == 0) {
            activity.getSpiceManager().execute(lineasRequest.datosLinea(linea), new DatosLineaRequestListener());
        } else {
            showProgress(false);
            listaFacturasGruposFacturacionView.setVisibility(View.VISIBLE);
            if (mAdapter.getItemCount() == 0) {
                cargarListaFacturas(facturas);
            }
        }
    }

    private void loadMoreItems() {

        showProgress(true);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        inicio += CANTIDAD_REGISTROS;
        isLoading = true;
        activity.getSpiceManager().execute(facturasGrupoRequest.listar(codigoGrupo, inicio, CANTIDAD_REGISTROS), new FacturasGruposRequestListener());
    }

    public final class DatosLineaRequestListener implements RequestListener<Linea> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showProgress(false);
                System.err.println("Facturas Error !!!");
                Toast.makeText(getActivity(), MensajesDeUsuario.SIN_DATOS_LINEA, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Linea datos) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (activity != null) {
                if (datos != null) {
                    showProgress(false);
                    codigoGrupo = datos.getGrupoFacturacion();
                    System.err.println("Facturas cargando grupo : " + codigoGrupo);
                    activity.getSpiceManager().execute(facturasGrupoRequest.listar(codigoGrupo, inicio, CANTIDAD_REGISTROS), new FacturasGruposRequestListener());
                } else {
                    showProgress(false);
                    System.err.println("Facturas cargando grupo : " + datos);
                    setearMensajeDeFalla();
                }
            }
        }
    }

    public final class FacturasGruposRequestListener implements RequestListener<ListaPaginada> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Context context = getActivity();
            System.err.println("Listar Facturas error : " + spiceException.getMessage());
            if(context != null) {
                removerMensajeLoading();
                setearMensajeDeFalla();
                Toast.makeText(context, MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final ListaPaginada datos) {
            Context context = getActivity();
            if(context != null) {
                removerMensajeLoading();
                if (datos != null) {
                    cantidadTotal = datos.getCantidad();
                    List<Factura> lista = datos.getLista();
                    if (lista.size() == 0) {
                        cargarListaFacturas();
                    } else {
                        cargarListaFacturas(lista);
                    }
                    facturas.addAll(lista);
                    isLoading = false;
                    if (facturas.size() >= cantidadTotal) {
                        isLastPage = true;
                    }
                } else {
                    setearMensajeDeFalla();
                    Toast.makeText(context, MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void showProgress(final boolean show) {
        progressBar.setVisibility(show? View.VISIBLE: View.GONE);

    }
    private void cargarListaFacturas(List<Factura> lista) {
        showProgress(false);
        for (Factura factura: lista) {
            mAdapter.addItem(factura);
        }
        listaFacturasGruposFacturacionView.setVisibility(View.VISIBLE);
        stub.setVisibility(View.GONE);
    }

    private void setearMensajeLoading() {
        showProgress(true);
        listaFacturasGruposFacturacionView.setVisibility(View.GONE);
        stub.setVisibility(View.GONE);
    }

    private void removerMensajeLoading() {
        showProgress(false);
        listaFacturasGruposFacturacionView.setVisibility(View.GONE);
        stub.setVisibility(View.GONE);
    }

    private void setearMensajeDeFalla(){
        showProgress(false);
        listaFacturasGruposFacturacionView.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_sin_servicio);
            stub.inflate();
            stub.setVisibility(View.VISIBLE);
        }
    }

    private void cargarListaFacturas() {
        showProgress(false);
        System.err.println("Listar Facturas cargarListaFacturas ");
        listaFacturasGruposFacturacionView.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_sin_datos);
            stub.inflate();
            stub.setVisibility(View.VISIBLE);
        }
    }
}
