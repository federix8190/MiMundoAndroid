package py.com.personal.mimundo.fragments.saldo.prestamo;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.SuperAwesomeCardFragment;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineasUsuarioRequest;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.utils.StringUtils;
import py.com.personal.mimundo.widget.tab.SlidingTabLayout;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 17/02/2015.
 */
public class PrestamoSaldoFragment extends Fragment {

    private static final String OPERACION = "RECARGA_SOS";
    private static final String FRAGMENT_NAME = "PrestamoSaldoFragment";

    private ViewPager pager;
    private MyPagerAdapter adapter;

    private ObtenerLineasUsuarioRequest lineasService;
    private List<Linea> lineas;
    private LinearLayout pantalla;
    private RelativeLayout progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(true);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Préstamo", "Historial"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RecargaSosFragment();
                case 1:
                    return new HistorialPrestamosFragment();
                default:
                    return SuperAwesomeCardFragment.newInstance(position);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prestamo_saldo, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setTitle("Préstamo de Saldo");

        int unit = TypedValue.COMPLEX_UNIT_DIP;
        final int pageMargin = (int) TypedValue.applyDimension(unit, 4, getResources().getDisplayMetrics());

        adapter = new MyPagerAdapter(getChildFragmentManager());
        pager = (ViewPager) v.findViewById(R.id.paginador);
        pager.setAdapter(adapter);
        pager.setPageMargin(pageMargin);

        SlidingTabLayout tabs = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
        tabs.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.celeste_institucional));
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);

        pantalla = (LinearLayout) v.findViewById(R.id.pantalla);
        progressBar = (RelativeLayout)v.findViewById(R.id.progressbar);
        showLoader(true);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineasService = new ObtenerLineasUsuarioRequest(getActivity(), new JacksonConverter(objectMapper), OPERACION);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (activity.esOperacionActual(OPERACION)) {
            showLoader(false);
            activity.cargarListaLineas(lineas, OPERACION);
            activity.setActionBar(true, FRAGMENT_NAME);
        } else {
            activity.setActionBar(false, null);
            activity.getSpiceManager().execute(lineasService.listar(), new LineasRequestListener());
        }
    }

    public final class LineasRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showLoader(false);
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List lista) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (activity != null) {
                showLoader(false);
                if (lista != null && lista.size() > 0) {
                    activity.cargarListaLineas(lista, OPERACION);
                    activity.setActionBar(true, FRAGMENT_NAME);
                    lineas = lista;
                    String linea = lineas.get(0).getNumeroLinea();
                    activity.guardarLineaSeleccionada(linea);

                    if (activity.totalLineas <= 10) {
                        activity.seleccionarPrimerItem();
                    }
                    
                    TextView lineaSeleccionadaView = (TextView)activity.getmDrawerList().findViewById(R.id.reservedNamedId);
                    if (lineaSeleccionadaView != null) {
                        String nroLinea = activity.obtenerLineaSeleccionada();
                        if (nroLinea != null && StringUtils.esLineaTelefonia(nroLinea)) {
                            lineaSeleccionadaView.setText("0" + nroLinea);
                        } else {
                            lineaSeleccionadaView.setText(nroLinea);
                        }
                    }
                } else {
                    activity.cargarListaLineas(new ArrayList<Linea>(), OPERACION);
                    Toast.makeText(activity, MensajesDeUsuario.MENSAJE_SIN_LINEAS_OPERACION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void showLoader(final boolean show) {
        pantalla.setVisibility(show? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show? View.VISIBLE : View.GONE);
    }

}
