package py.com.personal.mimundo.fragments.gestion.facturacion;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import py.com.personal.mimundo.services.cache.facturacion.ObtenerGruposFacturacionRequest;
import py.com.personal.mimundo.services.grupos.models.GrupoFacturacion;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 05/08/2014.
 */
public class ListaGruposFacturacionFragment extends Fragment {

    private static final int CANTIDAD_REGISTROS = 10;
    private int cantidadTotal;
    private int inicio = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private ObtenerGruposFacturacionRequest mGruposRequest;
    private RecyclerView viewListaGruposDeFacturacion;
    private ListaGrupoFacturacionAdapter mAdapter;
    private List<GrupoFacturacion> grupos;
    private LinearLayout listaGruposContainer;
    private ViewStub stub;
    private ProgressBar progressBar;
    private String codigo;

    public static ListaGruposFacturacionFragment newInstance(String codigo) {
        ListaGruposFacturacionFragment f = new ListaGruposFacturacionFragment();
        f.codigo = codigo;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicio = 0;
        cantidadTotal = 0;
        isLoading = false;
        isLastPage = false;
        grupos = new ArrayList<GrupoFacturacion>();
        if (grupos != null) {
            System.err.println("ListaGruposFacturacionFragment onCreate : " + grupos.size());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*ListaGrupos lista = new ListaGrupos();
        lista.setLista(grupos);
        outState.putString("listaGruposDeFacturacion", new Gson().toJson(lista));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Grupos de Facturación");

        if (grupos == null) {
            grupos = new ArrayList<GrupoFacturacion>();
            //System.err.println("ListaGruposFacturacionFragment onCreateView : " + grupos.size());
        }

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.setActionBar(false, "ListaGruposFacturacionFragment");
        activity.salirApp = true;

        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_lista_grupos_facturacion, container, false);
        viewListaGruposDeFacturacion = (RecyclerView) v.findViewById(R.id.lista_grupos);
        listaGruposContainer = (LinearLayout) v.findViewById(R.id.lista_grupos_container);
        //final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        viewListaGruposDeFacturacion.setLayoutManager(mLayoutManager);
        viewListaGruposDeFacturacion.setItemAnimator(new DefaultItemAnimator());

        stub = (ViewStub) v.findViewById(R.id.stub);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar_GrupoFacturacion);

        mAdapter = new ListaGrupoFacturacionAdapter(getActivity());
        viewListaGruposDeFacturacion.setAdapter(mAdapter);

        viewListaGruposDeFacturacion.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.grupo_facturacion_title)).setTypeface(tf);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        mGruposRequest = new ObtenerGruposFacturacionRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (grupos == null || grupos.size() == 0) {
            //System.err.println("ListaGruposFacturacionFragment onStart : " + grupos.size());
            if (this.codigo != null && !this.codigo.isEmpty()) {
                activity.getSpiceManager().execute(mGruposRequest.gruposFacturacion(this.codigo), new GrupoRequestListener());
            } else {
                activity.getSpiceManager().execute(mGruposRequest.listar(inicio, CANTIDAD_REGISTROS), new GruposRequestListener());
            }
        } else {
            showProgress(false);
            if (codigo != null || cantidadTotal > 10) {
                activity.showBuscadorGrupo(codigo);
            }
            cargarListaDeGrupos(grupos);
        }

    }

    private void loadMoreItems() {
        showProgress(true);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        inicio += CANTIDAD_REGISTROS;
        isLoading = true;
        activity.getSpiceManager().execute(mGruposRequest.listar(inicio, CANTIDAD_REGISTROS), new GruposRequestListener());
    }

    public final class GruposRequestListener implements RequestListener<ListaPaginada> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                isLoading = false;
                removerEstadoLoading();
                setearEstadoSinServicio();
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final ListaPaginada datos) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (activity != null) {
                removerEstadoLoading();
                if (datos != null) {
                    cantidadTotal = datos.getCantidad();
                    if (cantidadTotal > 10) {
                        //activity.setActionBar(true, "ListaGruposFacturacionFragment");
                        activity.showBuscadorGrupo(codigo);
                    } else {
                        activity.setActionBar(false, "ListaGruposFacturacionFragment");
                    }
                    isLoading = false;
                    if (datos.getLista().size() == 0) {
                        cargarListaDeGrupos();
                    } else {
                        cargarListaDeGrupos(datos.getLista());
                    }
                    grupos.addAll(datos.getLista());
                    if (grupos.size() >= cantidadTotal) {
                        isLastPage = true;
                    }
                } else {
                    setearEstadoSinServicio();
                    Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public final class GrupoRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                removerEstadoLoading();
                setearEstadoSinServicio();
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List datos) {
            if(getActivity() != null) {
                removerEstadoLoading();
                if (datos != null) {
                    if (datos.size() == 0) {
                        cargarListaDeGrupos();
                    } else {
                        cargarListaDeGrupos(datos);
                    }
                    grupos = datos;
                } else {
                    setearEstadoSinServicio();
                    Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void showProgress(final boolean show) {
        progressBar.setVisibility(show? View.VISIBLE: View.GONE);
        //listaGruposContainer.setVisibility(show? View.GONE: View.VISIBLE);
    }

    private void cargarListaDeGrupos(final List<GrupoFacturacion> lista) {
        showProgress(false);
        //ListaGrupoFacturacionAdapter mAdapter = new ListaGrupoFacturacionAdapter(getActivity());
        //mAdapter.removeAll();

        /*if (viewListaGruposDeFacturacion.getAdapter() == null) {
            viewListaGruposDeFacturacion.setAdapter(mAdapter);
        } else {
            viewListaGruposDeFacturacion.swapAdapter(mAdapter, false);
        }*/

        /*if (mAdapter.getItemCount() > 0) {
            return;
        }*/

        for (GrupoFacturacion grupoFacturacion: lista) {
            if (this.codigo != null && !this.codigo.isEmpty()) {
                String codigoGrupo = grupoFacturacion.getCodigo();
                if (codigoGrupo.equals(this.codigo)) {
                    mAdapter.addItem(grupoFacturacion);
                }
            } else {
                mAdapter.addItem(grupoFacturacion);
            }
        }
        mAdapter.notifyDataSetChanged();
        listaGruposContainer.setVisibility(View.VISIBLE);

        stub.setVisibility(View.GONE);

    }

    private void cargarListaDeGrupos() {
        showProgress(false);
        listaGruposContainer.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_sin_datos);
            stub.inflate();
        }
    }

    private void setearEstadoLoading() {
        showProgress(true);
        listaGruposContainer.setVisibility(View.GONE);
        stub.setVisibility(View.GONE);
    }

    private void removerEstadoLoading() {
        showProgress(false);
        listaGruposContainer.setVisibility(View.GONE);
    }

    private void setearEstadoSinServicio() {
        showProgress(false);
        listaGruposContainer.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_sin_servicio);
            stub.inflate();
        }
    }
}
