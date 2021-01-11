package py.com.personal.mimundo.fragments.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.AdapterPlanes;
import py.com.personal.mimundo.services.cache.lineas.ObtenerPlanesLineaRequest;
import py.com.personal.mimundo.services.lineas.models.ListaPlanesLinea;
import py.com.personal.mimundo.services.lineas.models.Plan;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class PlanesFragment extends Fragment {

    private RecyclerView listaPlanesView;
    private ObtenerPlanesLineaRequest planesLineaRequest;
    private List<Plan> planesLinea;
    private String linea;
    private boolean datosCargados = false;
    private ViewStub stub;
    private ProgressBar progressBar;

    public static PlanesFragment newInstance() {
        PlanesFragment f = new PlanesFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonObject = savedInstanceState.getString("planesLinea");
            ListaPlanesLinea lista = new Gson().fromJson(jsonObject, ListaPlanesLinea.class);
            planesLinea = lista.getPlanes();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListaPlanesLinea lista = new ListaPlanesLinea();
        lista.setPlanes(planesLinea);
        outState.putString("planesLinea", new Gson().toJson(lista));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_planes, container, false);

        // Referencia a elementos del layout.
        listaPlanesView = (RecyclerView) v.findViewById(R.id.lista_planes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaPlanesView.setLayoutManager(mLayoutManager);
//        listaPlanesView.setItemAnimator(new GarageDoorItemAnimator());
        listaPlanesView.setItemAnimator(new DefaultItemAnimator());
        linea = ((BaseDrawerActivity)getActivity()).obtenerLineaSeleccionada();
        stub = (ViewStub) v.findViewById(R.id.stub);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbar_planes);

        // Servicios.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        planesLineaRequest = new ObtenerPlanesLineaRequest(getActivity(), new JacksonConverter(objectMapper));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!datosCargados) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            activity.getSpiceManager().execute(
                    planesLineaRequest.planesLinea(linea),
                    new PlanesLineaRequestListener()
            );
        }
    }

    public final class PlanesLineaRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showProgress(false);
                Toast.makeText(getActivity(), getResources().getString(R.string.error_obtener_planes),
                        Toast.LENGTH_SHORT).show();
                listaPlanesView.setVisibility(View.GONE);
                if (stub != null && stub.getParent() != null) {
                    stub.setLayoutResource(R.layout.widget_sin_servicio);
                    stub.inflate();
                }
                datosCargados = true;
            }
        }

        @Override
        public void onRequestSuccess(final List datos) {
            if (getActivity() != null) {
                showProgress(false);
                if (datos != null) {
                    if (datos.isEmpty()) {
                        listaPlanesView.setVisibility(View.GONE);
                        if (stub != null && stub.getParent() != null) {
                            stub.setLayoutResource(R.layout.widget_sin_datos);
                            stub.inflate();
                        }
                    } else {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cargarDatos(datos);
                            }
                        }, 700);

                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_obtener_planes),
                            Toast.LENGTH_SHORT).show();
                    listaPlanesView.setVisibility(View.GONE);
                    if (stub != null && stub.getParent() != null) {
                        stub.setLayoutResource(R.layout.widget_sin_servicio);
                        stub.inflate();
                    }
                }
                datosCargados = true;
            }
        }
    }

    public void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        listaPlanesView.setVisibility(show? View.GONE: View.VISIBLE);
    }

    private void cargarDatos(List<Plan> datos) {
        AdapterPlanes mAdapter = new AdapterPlanes(getActivity());
        listaPlanesView.setAdapter(mAdapter);
        for (Plan plan: datos) {
            mAdapter.addItem(plan);
        }
    }
}
