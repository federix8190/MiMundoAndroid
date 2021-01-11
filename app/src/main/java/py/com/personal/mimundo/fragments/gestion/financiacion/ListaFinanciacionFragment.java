package py.com.personal.mimundo.fragments.gestion.financiacion;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
//import py.com.personal.mimundo.disenhos.GarageDoorItemAnimator;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.financiaciones.FinanciacionesRequest;
import py.com.personal.mimundo.services.financiaciones.models.Financiacion;
import py.com.personal.mimundo.services.financiaciones.models.ListaFinanciaciones;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class ListaFinanciacionFragment extends Fragment {

    private RecyclerView listaFinanciacionesView;
    private FinanciacionesRequest financiacionesRequest;
    private List<Financiacion> financiaciones;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonObject = savedInstanceState.getString("listaFinanciaciones");
            ListaFinanciaciones lista = new Gson().fromJson(jsonObject, ListaFinanciaciones.class);
            financiaciones = lista.getLista();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListaFinanciaciones lista = new ListaFinanciaciones();
        lista.setLista(financiaciones);
        outState.putString("listaFinanciaciones", new Gson().toJson(lista));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_financiacion, container, false);
        getActivity().setTitle("Financiación");
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setActionBar(false, "ListaFinanciacionFragment");

        // Implementacion del recycler view.
        listaFinanciacionesView = (RecyclerView) v.findViewById(R.id.lista_financiaciones);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaFinanciacionesView.setLayoutManager(mLayoutManager);
//        listaFinanciacionesView.setItemAnimator(new GarageDoorItemAnimator());
        listaFinanciacionesView.setItemAnimator(new DefaultItemAnimator());

        progressBar = (ProgressBar)v.findViewById(R.id.progressbar_Financiacion);

        // Invocacion de servicios.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        financiacionesRequest = new FinanciacionesRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (financiaciones == null) {
            showProgress(true);
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            activity.getSpiceManager().execute(financiacionesRequest.financiaciones(),
                    new FinanciacionesRequestListener());
        } else {
            showProgress(false);
            cargarLista(financiaciones);
        }
    }

    public final class FinanciacionesRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Activity context = getActivity();
            if (context != null) {
                showProgress(false);
                Toast.makeText(context, MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
                View view = getView();
                if (view != null) {
                    ViewStub stub = (ViewStub) view.findViewById(R.id.stub);
                    if (stub != null && stub.getParent() != null) {
                        listaFinanciacionesView.setVisibility(View.GONE);
                        stub.setLayoutResource(R.layout.widget_sin_servicio);
                        stub.inflate();
                    }
                }
            }
        }

        @Override
        public void onRequestSuccess(final List datos) {
            Activity context = getActivity();
            if (context != null) {
                showProgress(false);
                if (datos != null) {
                    cargarLista(datos);
                    financiaciones = datos;
                } else {
                    Toast.makeText(context, MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (financiaciones != null) {
            cargarLista(financiaciones);
        }
    }

    public void showProgress(final boolean show) {
        progressBar.setVisibility(show? View.VISIBLE: View.GONE);
        listaFinanciacionesView.setVisibility(show? View.GONE: View.VISIBLE);
    }

    private void cargarLista(final List<Financiacion> lista) {
        financiaciones = lista;
        if (!lista.isEmpty()) {
            ListaFinanciacionAdapter mAdapter = new ListaFinanciacionAdapter(getActivity());
            mAdapter.removeAll();
            if (listaFinanciacionesView.getAdapter() == null) {
                listaFinanciacionesView.setAdapter(mAdapter);
            } else {
                listaFinanciacionesView.swapAdapter(mAdapter, false);
            }
            for (Financiacion financiacion: lista) {
                mAdapter.addItem(financiacion);
            }
        } else {
            // poner un widget que simbolice datos vacios
            View view = getView();
            if (view != null) {
                ViewStub stub = (ViewStub) view.findViewById(R.id.stub);
                if (stub != null && stub.getParent() != null) {
                    listaFinanciacionesView.setVisibility(View.GONE);
                    stub.setLayoutResource(R.layout.widget_sin_datos);
                    stub.inflate();
                }
            }
        }
    }
}
