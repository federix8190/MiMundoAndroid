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
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.facturacion.FacturasGrupoRequest;
import py.com.personal.mimundo.services.grupos.models.Factura;
import py.com.personal.mimundo.services.grupos.models.ListaFacturas;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 06/08/2014.
 */
public class UltimasFacturasFragment extends Fragment {

    // Codigo de grupo.
    private String codigoGrupo;

    // Lista de facturas.
    private List<Factura> listaFacturas;
    private RecyclerView listaFacturasGruposFacturacionView;
    private List<Factura> facturas;

    private ProgressBar progressBar;

    private ViewStub stub;

    // Peticion de facturas de grupo de facturacion.
    private FacturasGrupoRequest facturasGrupoRequest;

    public static UltimasFacturasFragment newInstance(String codigoGrupo) {
        UltimasFacturasFragment f = new UltimasFacturasFragment();
        f.codigoGrupo = codigoGrupo;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            codigoGrupo = savedInstanceState.getString("codigoGrupo");
            String jsonObject = savedInstanceState.getString("listaFacturas");
            ListaFacturas lista = new Gson().fromJson(jsonObject, ListaFacturas.class);
            facturas = lista.getLista();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListaFacturas lista = new ListaFacturas();
        lista.setLista(facturas);
        outState.putString("codigoGrupo", codigoGrupo);
        outState.putString("listaFacturas", new Gson().toJson(lista));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_ultimas_facturas, container, false);

        // Lista de facturas.
        listaFacturasGruposFacturacionView = (RecyclerView) v.findViewById(R.id.listaFacturasGruposFacturacion);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaFacturasGruposFacturacionView.setLayoutManager(mLayoutManager);
        listaFacturasGruposFacturacionView.setItemAnimator(new DefaultItemAnimator());

        progressBar = (ProgressBar)v.findViewById(R.id.progressbar_UltimasFacturas);
        stub = (ViewStub) v.findViewById(R.id.stub);

        // Se solicitan los datos.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        facturasGrupoRequest = new FacturasGrupoRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (facturas == null) {
            setearMensajeLoading();
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            activity.getSpiceManager().execute(facturasGrupoRequest.facturasGrupo(codigoGrupo), new FacturasGruposRequestListener());
        } else {
            cargarListaFacturas(facturas);
        }
    }

    public final class FacturasGruposRequestListener implements RequestListener<List> {

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
        public void onRequestSuccess(final List datos) {
            Context context = getActivity();
            System.err.println("Listar Facturas OK : " + datos.size());
            if(context != null) {
                removerMensajeLoading();
                if (datos != null) {
                    if (datos.size() == 0) {
                        cargarListaFacturas();
                    } else {
                        cargarListaFacturas(datos);
                    }
                    facturas = datos;
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
        listaFacturas = lista;
        System.err.println("Listar Facturas cargarListaFacturas : " + lista.size());
        FacturasGrupoAdapter mAdapter = new FacturasGrupoAdapter(getActivity());
        mAdapter.removeAll();

        if (listaFacturasGruposFacturacionView.getAdapter() == null) {
            listaFacturasGruposFacturacionView.setAdapter(mAdapter);
        } else {
            listaFacturasGruposFacturacionView.swapAdapter(mAdapter, false);
        }
        
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
