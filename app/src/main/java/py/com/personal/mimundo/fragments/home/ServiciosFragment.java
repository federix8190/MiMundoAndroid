package py.com.personal.mimundo.fragments.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import py.com.personal.mimundo.adapters.AdapterServicios;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.lineas.ObtenerServiciosLineaRequest;
import py.com.personal.mimundo.services.lineas.models.ListaServiciosLinea;
import py.com.personal.mimundo.services.lineas.models.Servicio;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class ServiciosFragment extends Fragment {

    private LinearLayout listaServiciosContent;
    private RecyclerView listaServiciosView;
    private List<Servicio> serviciosLinea;
    private ObtenerServiciosLineaRequest serviciosLineaRequest;
    private String linea;
    private ViewStub stub;
    private ProgressBar progressBar;
    private boolean datosCargados = false;

    public static ServiciosFragment newInstance() {
        ServiciosFragment f = new ServiciosFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonObject = savedInstanceState.getString("serviciosLinea");
            ListaServiciosLinea lista = new Gson().fromJson(jsonObject, ListaServiciosLinea.class);
            serviciosLinea = lista.getServicios();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListaServiciosLinea lista = new ListaServiciosLinea();
        lista.setServicios(serviciosLinea);
        outState.putString("serviciosLinea", new Gson().toJson(lista));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_servicios, container, false);

        // Referencias a elementos del layout.
        listaServiciosContent = (LinearLayout) v.findViewById(R.id.lista_servicios_content);
        listaServiciosView = (RecyclerView) v.findViewById(R.id.lista_servicios);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaServiciosView.setLayoutManager(mLayoutManager);
        listaServiciosView.setItemAnimator(new DefaultItemAnimator());
        linea = ((BaseDrawerActivity)getActivity()).obtenerLineaSeleccionada();
        stub = (ViewStub) v.findViewById(R.id.stub);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbar_Servicios);

        // Servicios.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        serviciosLineaRequest = new ObtenerServiciosLineaRequest(getActivity(), new JacksonConverter(objectMapper));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!datosCargados) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            activity.getSpiceManager().execute(serviciosLineaRequest.serviciosLinea(linea), new ServiciosLineaRequestListener());
        }
    }

    public final class ServiciosLineaRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showProgress(false);
                setearSinServicios();
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
                datosCargados = true;
            }
        }

        @Override
        public void onRequestSuccess(final List datos) {
            if (getActivity() != null) {
                showProgress(false);
                if (datos != null) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cargarDatosServ(datos);
                        }
                    }, 700);
                } else {
                    setearMensajeDeFalla();
                    Toast.makeText(getActivity(),
                            MensajesDeUsuario.MENSAJE_FALLO_PETICION,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
            datosCargados = true;
        }
    }

    private void cargarDatosServ(List<Servicio> datos) {
        if (!datos.isEmpty()) {
            AdapterServicios mAdapter = new AdapterServicios(getActivity());
            listaServiciosView.setAdapter(mAdapter);
            for (Servicio servicio: datos) {
                mAdapter.addItem(servicio);
            }
        } else {
            // poner un widget que simbolice datos vacios
            listaServiciosContent.setVisibility(View.GONE);
            if (stub != null && stub.getParent() != null) {
                stub.setLayoutResource(R.layout.widget_sin_datos);
                stub.inflate();
            }
        }
    }

    public void showProgress(final boolean show) {
        progressBar.setVisibility(show? View.VISIBLE: View.GONE);
        listaServiciosContent.setVisibility(show? View.GONE: View.VISIBLE);
    }

    private void setearMensajeDeFalla() {
        listaServiciosContent.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_sin_servicio);
            stub.inflate();
        }
    }

    private void setearSinServicios() {
        listaServiciosContent.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_sin_servicio);
            stub.inflate();
        }
    }
}
