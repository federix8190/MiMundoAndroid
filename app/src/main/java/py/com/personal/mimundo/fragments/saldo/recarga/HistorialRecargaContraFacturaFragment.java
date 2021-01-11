package py.com.personal.mimundo.fragments.saldo.recarga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
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
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.recargas.HistorialRecargaContraFacturaRequest;
import py.com.personal.mimundo.services.lineas.models.DetallesRecargaContraFactura;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 7/22/2014.
 */
public class HistorialRecargaContraFacturaFragment extends Fragment {

    // Historial de recargas.
    private HistorialRecargaContraFacturaRequest historialRequest;
    private List<DetallesRecargaContraFactura> hitorialRecargaContraFactura;
    private RecyclerView listaHistorialrecargaContraFactura;
    private ViewStub stub;

    private RelativeLayout pantalla;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_historial_recarga_saldo, container, false);

        pantalla = (RelativeLayout) v.findViewById(R.id.pantalla);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);

        // Historial de recarga.
        listaHistorialrecargaContraFactura = (RecyclerView) v.findViewById(R.id.lista_recarga_contra_factura);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaHistorialrecargaContraFactura.setLayoutManager(mLayoutManager);
        listaHistorialrecargaContraFactura.setItemAnimator(new DefaultItemAnimator());
        stub = (ViewStub) v.findViewById(R.id.stub);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        historialRequest = new HistorialRecargaContraFacturaRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (activity.getListaLineas() != null && activity.getListaLineas().size() > 0) {
            showLoader(true);
            String linea = activity.obtenerLineaSeleccionada();
            activity.getSpiceManager().execute(historialRequest.obtenerLista(linea), new HistorialRequestListener());
        } else {
            setearMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_LINEAS);
        }
    }

    public final class HistorialRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showLoader(false);
                if (spiceException.getCause().getMessage().contains("400")) {
                    setearMensajeSinServicio(getResources().getString(R.string.error_linea_sin_recarga));
                } else {
                    setearMensajeSinServicio(null);
                }
            }
        }

        @Override
        public void onRequestSuccess(final List datos) {
            if (getActivity() != null) {
                showLoader(false);
                if (datos != null) {
                    cargarLista(datos);
                } else {
                    setearMensajeSinServicio(getResources().getString(R.string.no_hay_datos));
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_obtener_historial_recargas),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cargarLista(List<DetallesRecargaContraFactura> listaDeRecargas) {
        if (!listaDeRecargas.isEmpty()) {
            hitorialRecargaContraFactura = listaDeRecargas;
        } else {
            hitorialRecargaContraFactura = new ArrayList<>();
            setearMensajeSinServicio(getResources().getString(R.string.no_hay_datos));
            return;
        }
        MontosRecargaAdapter mAdapter = new MontosRecargaAdapter(getActivity());
        listaHistorialrecargaContraFactura.setAdapter(mAdapter);
        for (DetallesRecargaContraFactura detallesRecargaContraFactura: listaDeRecargas) {
            mAdapter.addItem(detallesRecargaContraFactura);
        }
        listaHistorialrecargaContraFactura.setVisibility(View.VISIBLE);
        stub.setVisibility(View.GONE);
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

    public void setearMensajeSinServicio(String msg) {
        hitorialRecargaContraFactura = new ArrayList<>();
        MontosRecargaAdapter mAdapter = new MontosRecargaAdapter(getActivity());
        listaHistorialrecargaContraFactura.setAdapter(mAdapter);
        for (DetallesRecargaContraFactura detallesRecargaContraFactura: hitorialRecargaContraFactura) {
            mAdapter.addItem(detallesRecargaContraFactura);
        }
        listaHistorialrecargaContraFactura.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            if (msg != null) {
                stub.setLayoutResource(R.layout.widget_sin_datos);
            } else {
                stub.setLayoutResource(R.layout.widget_sin_servicio);
            }
            stub.inflate();
            stub.setVisibility(View.VISIBLE);
        }
    }

    public void showLoader(final boolean show) {
        pantalla.setVisibility(show? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show? View.VISIBLE : View.GONE);
    }

}
