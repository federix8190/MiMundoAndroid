package py.com.personal.mimundo.fragments.home;

import android.app.Activity;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.AdapterCaracteristicasEquipo;
import py.com.personal.mimundo.adapters.CaracteristicasEquipo;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.lineas.ObtenerTerminalLineaRequest;
import py.com.personal.mimundo.services.terminales.models.Terminal;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 7/23/2014.
 */
public class EquipoFragment extends Fragment {

    private RecyclerView listaCaracteristicasEquipo;
    private AdapterCaracteristicasEquipo adapter;
    private List<CaracteristicasEquipo> listaDeCaracteristicas;
    private ObtenerTerminalLineaRequest terminalLineaRequest;
    private Terminal datosTerminal;
    private String linea;
    private ProgressBar progressBar;
    private ViewStub stub;
    private boolean datosCargados = false;

    public static EquipoFragment newInstance() {
        EquipoFragment f = new EquipoFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonObject = savedInstanceState.getString("datosTerminal");
            datosTerminal = new Gson().fromJson(jsonObject, Terminal.class);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("datosTerminal", new Gson().toJson(datosTerminal));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_equipo, container, false);

        // Referencias a elementos del layout.
        listaCaracteristicasEquipo = (RecyclerView) v.findViewById(R.id.lista_datos_equipo);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaCaracteristicasEquipo.setLayoutManager(mLayoutManager);
//        listaCaracteristicasEquipo.setItemAnimator(new GarageDoorItemAnimator());
        listaCaracteristicasEquipo.setItemAnimator(new DefaultItemAnimator());
        linea = ((BaseDrawerActivity)getActivity()).obtenerLineaSeleccionada();
        stub = (ViewStub) v.findViewById(R.id.stub);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbar_Equipo);

        // Servicios.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        terminalLineaRequest = new ObtenerTerminalLineaRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!datosCargados) {
            datosCargados = true;
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            activity.getSpiceManager().execute(terminalLineaRequest.datosTerminalLinea(linea), new DatosTerminalRequestListener());
        }
    }

    public final class DatosTerminalRequestListener implements RequestListener<Terminal> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showProgress(false);
                Toast.makeText(getActivity(), getResources().getString(R.string.error_obtener_datos_terminal),
                        Toast.LENGTH_SHORT).show();
                listaCaracteristicasEquipo.setVisibility(View.GONE);
                if (stub != null && stub.getParent() != null) {
                    stub.setLayoutResource(R.layout.widget_sin_servicios);
                    stub.inflate();
                }
            }
        }

        @Override
        public void onRequestSuccess(final Terminal datos) {
            Activity context = getActivity();
            if(context != null) {
                showProgress(false);
                if (datos != null) {
                    cargarDatosTerminal(datos);
                } else {
                    mostrarMensajeDeFalla();
                    String msg = getResources().getString(R.string.error_obtener_datos_terminal);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        listaCaracteristicasEquipo.setVisibility(show? View.GONE: View.VISIBLE);
    }

    private void cargarDatosTerminal(Terminal datos) {
        if (datos != null) {
            String datoDesconocido = getResources().getString(R.string.dato_desconocido);
            listaDeCaracteristicas =  new ArrayList<CaracteristicasEquipo>();
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Marca", datos.getMarca() == null?
                    datoDesconocido : datos.getMarca(), R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Modelo", datos.getModelo() == null?
                    datoDesconocido : datos.getModelo(), R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Imei", datos.getImei() == null?
                    datoDesconocido : datos.getImei(), R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Tecnología", datos.getTecnologia() == null?
                    datoDesconocido : datos.getTecnologia(), R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Banda GSM", datos.getBandaGsm() == null?
                    datoDesconocido : datos.getBandaGsm(), R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("GPRS", datos.getGprs() == null?
                    datoDesconocido : datos.getGprs()? MensajesDeUsuario.SI : MensajesDeUsuario.NO, R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("MMS", datos.getMms() == null?
                    datoDesconocido : datos.getMms()? MensajesDeUsuario.SI : MensajesDeUsuario.NO, R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("JAVA", datos.getJava() == null?
                    datoDesconocido : datos.getJava(), R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("WAP", datos.getWap() == null?
                    datoDesconocido : datos.getWap()? MensajesDeUsuario.SI : MensajesDeUsuario.NO, R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Streaming", datos.getStreaming() == null?
                    datoDesconocido : datos.getStreaming()? MensajesDeUsuario.SI : MensajesDeUsuario.NO, R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Pantalla a color", datos.getPantallaColor() == null?
                    datoDesconocido : datos.getPantallaColor()? MensajesDeUsuario.SI : MensajesDeUsuario.NO, R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Cámara", datos.getCamara() == null?
                    datoDesconocido : datos.getCamara(), R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("MP3", datos.getMp3() == null?
                    datoDesconocido : datos.getMp3()? MensajesDeUsuario.SI : MensajesDeUsuario.NO, R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Reproductor AAC", datos.getReproductorAac() == null?
                    datoDesconocido : datos.getReproductorAac()? MensajesDeUsuario.SI : MensajesDeUsuario.NO, R.drawable.ic_info));
            listaDeCaracteristicas.add(new CaracteristicasEquipo("Tono polifónico", datos.getPolifonico() == null?
                    datoDesconocido : datos.getPolifonico()? MensajesDeUsuario.SI : MensajesDeUsuario.NO, R.drawable.ic_info));

            AdapterCaracteristicasEquipo mAdapter = new AdapterCaracteristicasEquipo(getActivity());
            listaCaracteristicasEquipo.setAdapter(mAdapter);
            for (CaracteristicasEquipo caracteristicasEquipo: listaDeCaracteristicas) {
                mAdapter.addItem(caracteristicasEquipo);
            }
        } else {
            listaCaracteristicasEquipo.setVisibility(View.GONE);
            if (stub != null && stub.getParent() != null) {
                stub.setLayoutResource(R.layout.widget_sin_datos);
                stub.inflate();
            }
        }
    }

    private void mostrarMensajeDeFalla() {
        listaCaracteristicasEquipo.setVisibility(View.GONE);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_sin_servicio);
            stub.inflate();
        }
    }
}