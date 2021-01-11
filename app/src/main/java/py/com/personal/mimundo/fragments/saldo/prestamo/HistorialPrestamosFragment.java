package py.com.personal.mimundo.fragments.saldo.prestamo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.recargas.HistorialRecargaSosRequest;
import py.com.personal.mimundo.services.lineas.models.DetalleRecargaSos;
import py.com.personal.mimundo.services.lineas.models.RecargaSos;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 7/22/2014.
 */
public class HistorialPrestamosFragment extends Fragment {

    private static final String CODIGO_CONSULTA_EXITOSA = "0";

    private HistorialRecargaSosRequest historialRecargaRequest;
    private RelativeLayout pantalla;
    private ProgressBar progressBar;

    // Historial de prestamo saldo (recarga SOS).
    private RecyclerView listaHistorialrecargaSos;
    private List<DetalleRecargaSos> hitorialRecargaSos;
    private ViewStub stub;
    private FrameLayout v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = (FrameLayout) inflater.inflate(R.layout.fragment_historial_presta_saldo, container, false);

        pantalla = (RelativeLayout)v.findViewById(R.id.pantalla);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbar);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        historialRecargaRequest = new HistorialRecargaSosRequest(getActivity(), new JacksonConverter(objectMapper));

        listaHistorialrecargaSos = (RecyclerView) v.findViewById(R.id.lista_recarga_sos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaHistorialrecargaSos.setLayoutManager(mLayoutManager);
        listaHistorialrecargaSos.setItemAnimator(new DefaultItemAnimator());
        stub = (ViewStub) v.findViewById(R.id.stub);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (activity.getListaLineas() != null && activity.getListaLineas().size() > 0) {
            showLoader(true);
            String linea = activity.obtenerLineaSeleccionada();
            activity.getSpiceManager().execute(historialRecargaRequest.consultar(linea), new HistorialRequestListener());
        } else {
            setearMensajeFalla(R.layout.widget_mensaje_generico, MensajesDeUsuario.MENSAJE_SIN_LINEAS);
        }
    }

    public final class HistorialRequestListener implements RequestListener<RecargaSos> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showLoader(false);
                setearMensajeFalla(R.layout.widget_sin_servicio, null);
                Toast.makeText(getActivity(), getResources().getString(R.string.error_obtener_historial_prestamos),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final RecargaSos datos) {
            if (getActivity() != null) {
                showLoader(false);
                if (datos != null) {
                    if (datos.getCodigo().equals(CODIGO_CONSULTA_EXITOSA)) {
                        if (datos.getDetalles() != null && !datos.getDetalles().isEmpty()) {
                            cargarLista(datos.getDetalles());
                        } else {
                            setearMensajeFalla(R.layout.widget_sin_datos, null);
                        }
                    } else {
                        setearMensajeFalla(R.layout.widget_mensaje_generico, datos.getMensaje());
                    }
                } else {
                    setearMensajeFalla(R.layout.widget_sin_servicio, null);
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_obtener_historial_prestamos),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Metodo que carga la lista en caso de que posea prestamos pendientes, en este caso el detalle
     * no es vacio.
     * @param listaDeRecargas
     */
    private void cargarLista(List<DetalleRecargaSos> listaDeRecargas) {
        hitorialRecargaSos = listaDeRecargas;
        RecargaSosAdapter mAdapter = new RecargaSosAdapter(getActivity());
        listaHistorialrecargaSos.setAdapter(mAdapter);
        for (DetalleRecargaSos detalleRecargaSos: listaDeRecargas) {
            mAdapter.addItem(detalleRecargaSos);
        }
        listaHistorialrecargaSos.setVisibility(View.VISIBLE);
        stub.setVisibility(View.GONE);
    }

    private void setearMensajeFalla(int layout, String mensaje) {
        hitorialRecargaSos = new ArrayList<>();
        RecargaSosAdapter mAdapter = new RecargaSosAdapter(getActivity());
        listaHistorialrecargaSos.setAdapter(mAdapter);
        for (DetalleRecargaSos detalleRecargaSos: hitorialRecargaSos) {
            mAdapter.addItem(detalleRecargaSos);
        }
        listaHistorialrecargaSos.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(layout);
            View inflatedView = stub.inflate();
            stub.setVisibility(View.VISIBLE);
            if (mensaje != null) {
                TextView mensajeTextView = (TextView) inflatedView.findViewById(R.id.mensaje_falla);
                mensajeTextView.setText(mensaje);
            }
        }
    }

    public void showLoader(final boolean show) {
        pantalla.setVisibility(show? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show? View.VISIBLE : View.GONE);
    }

}
