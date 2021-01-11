package py.com.personal.mimundo.fragments.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.AdapterInfoPacks;
import py.com.personal.mimundo.services.cache.lineas.ObtenerSaldosLineaRequest;
import py.com.personal.mimundo.services.lineas.models.DetalleSaldoPack;
import py.com.personal.mimundo.services.lineas.models.SaldoPack;
import py.com.personal.mimundo.services.lineas.models.Saldos;
import py.com.personal.mimundo.utils.DateUtils;
import py.com.personal.mimundo.utils.NumbersUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class PacksDeSaldoFragment extends Fragment {

    private boolean realizoPeticionDeSaldo = false;
    private ObtenerSaldosLineaRequest saldosRequest;
    public static final String SALDO = "saldo";

    private String linea;
    private Saldos saldos;
    private AdapterInfoPacks mAdapter;
    private ViewStub stub;
    private View progressbar;

    private List<AdapterInfoPacks.ClaveValor> listDataHeader;
    private HashMap<String, List<AdapterInfoPacks.DetallePack>> listDataChild;
    private ExpandableListView listaPacks;

    public static PacksDeSaldoFragment newInstance() {
        PacksDeSaldoFragment f = new PacksDeSaldoFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_packs_de_saldo, container, false);
        stub = (ViewStub) v.findViewById(R.id.stub);
        progressbar = v.findViewById(R.id.progressbar);
        listaPacks = (ExpandableListView) v.findViewById(R.id.lista_packs_de_saldo);

        final BaseDrawerActivity activity = ((BaseDrawerActivity) getActivity());
        linea = activity.obtenerLineaSeleccionada();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        saldosRequest = new ObtenerSaldosLineaRequest(getActivity(), new JacksonConverter(objectMapper));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (!realizoPeticionDeSaldo) {
            activity.getSpiceManager().execute(saldosRequest.saldosLinea(linea), new SaldosLineaRequestListener());
        }
    }

    public final class SaldosLineaRequestListener implements RequestListener<Saldos> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showProgress(false);
                Toast.makeText(getActivity(), getResources().getString(R.string.sin_datos_saldo), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Saldos datos) {
            Activity context = getActivity();
            if (context != null) {
                showProgress(false);
                saldos = datos;
                realizoPeticionDeSaldo = true;
                cargarDatos();
            }
        }
    }

    public void showProgress(final boolean show) {
        progressbar.setVisibility(show? View.VISIBLE: View.GONE);
        listaPacks.setVisibility(show? View.GONE: View.VISIBLE);
    }

    private void cargarDatos() {
        if (saldos != null && saldos.getPacks() != null && saldos.getPacks().size() > 0) {
            listDataHeader = new ArrayList();
            listDataChild = new HashMap();
            for (SaldoPack s : saldos.getPacks()) {
                Integer cantidadDeItems = s.getDetalle().size();
                listDataHeader.add(new AdapterInfoPacks.ClaveValor(s.getNombre(), "Cantidad de packs: " + cantidadDeItems));
                List<AdapterInfoPacks.DetallePack> detalle = new ArrayList();
                for (DetalleSaldoPack ds : s.getDetalle()) {
                    if (ds != null) {
                        String vigencia = "Sin datos";
                        String monto = "Sin datos";
                        if (ds.getVencimiento() != null) {
                            vigencia = DateUtils.convertirADate(new Date(ds.getVencimiento()), DateUtils.Formato.DD_MM_YYYY_TIME);
                        }
                        if (ds.getMonto() != null && ds.getUnidad() != null) {
                            //monto = NumbersUtils.formatear(ds.getMonto()) + " " + ds.getUnidad();
                            monto = getMonto(ds.getMonto(), ds.getUnidad());
                        }
                        detalle.add(new AdapterInfoPacks.DetallePack(vigencia, monto));
                    }
                }
                listDataChild.put(s.getNombre(), detalle);
            }
            mAdapter = new AdapterInfoPacks(getActivity(), listDataHeader, listDataChild);
            listaPacks.setAdapter(mAdapter);
        } else {
            listaPacks.setVisibility(View.GONE);
            if (stub != null && stub.getParent() != null) {
                stub.setLayoutResource(R.layout.widget_sin_packs);
                stub.inflate();
            }
        }
    }

    private String getMonto(String monto, String unidad) {

        try {

            if (unidad.equalsIgnoreCase("Bytes")) {
                Long bytes = new Long(monto);
                if (bytes > 1024) {
                    Double kiloBytes = bytes / 1024.0;
                    if (kiloBytes > 1024) {
                        Double megaBytes = kiloBytes / 1024.0;
                        if (megaBytes > 1024) {
                            Double gigaBytes = megaBytes / 1024.0;
                            return NumbersUtils.formatear(gigaBytes.longValue()) + "  GB";
                        } else {
                            return NumbersUtils.formatear(megaBytes.longValue()) + "  MB";
                        }
                    } else {
                        return NumbersUtils.formatear(kiloBytes.longValue()) + "  KB";
                    }
                }
            }
        } catch (Exception e) {
        }
        String res = NumbersUtils.formatear(monto) + "  " + unidad;
        System.err.println("packs saldo : " + res);
        return res;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void configurarActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE
                | ActionBar.DISPLAY_SHOW_CUSTOM);
    }*/
}
