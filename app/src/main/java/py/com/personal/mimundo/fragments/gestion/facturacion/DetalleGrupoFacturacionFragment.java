package py.com.personal.mimundo.fragments.gestion.facturacion;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.disenhos.Roles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.fragments.SuperAwesomeCardFragment;
import py.com.personal.mimundo.services.cache.facturacion.ObtenerDatosGrupoRequest;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineaRequest;
import py.com.personal.mimundo.services.grupos.models.GrupoFacturacion;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.widget.tab.SlidingTabLayout;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 19/08/2014.
 */
public class DetalleGrupoFacturacionFragment extends Fragment {

    private GrupoFacturacion datosGrupo;

    private ViewPager pager;
    private MyPagerAdapter adapter;
    private ObtenerLineaRequest lineasRequest;
    private ObtenerDatosGrupoRequest datosGrupoRequest;
    private String linea;
    private ViewStub stub;

    String tipoPersona;
    String tipoUsuario;

    private ProgressBar progressBar;
    private LinearLayout layout;
    private String codigo;

    public static DetalleGrupoFacturacionFragment newInstance(GrupoFacturacion datosGrupo) {
        DetalleGrupoFacturacionFragment f = new DetalleGrupoFacturacionFragment();
        f.datosGrupo = datosGrupo;
        return f;
    }

    public static DetalleGrupoFacturacionFragment newInstance(String codigo) {
        DetalleGrupoFacturacionFragment f = new DetalleGrupoFacturacionFragment();
        f.codigo = codigo;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        String nroLinea = activity.obtenerLineaFacturacion();

        ActionBar mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(true);
        }
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonDatosGrupo = savedInstanceState.getString("datosGrupo");
            datosGrupo = new Gson().fromJson(jsonDatosGrupo, GrupoFacturacion.class);
        }
        linea = activity.obtenerLineaSeleccionada();
        System.err.println("guardarLineaSeleccionada linea : " + linea);
        activity.lineaSeleccionada = false;
        activity.setActionBar(false, "DetalleGrupoFacturacionFragment");

        tipoPersona = activity.obtenerTipoPersona();
        tipoUsuario = activity.obtenerTipoUsuario();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("datosGrupo", new Gson().toJson(datosGrupo));
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] TITLES;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (tipoPersona.equals("PERJUR") && (tipoUsuario.equals("TMP") || tipoUsuario.equals("RES"))) {
                TITLES = new String[1];
                TITLES[0] = "info ";
            } else {
                if (activity.obtenerRolesUsuario().contains(Roles.CONSULTAR_FACTURAS)) {
                    TITLES = new String[2];
                    TITLES[0] = "info ";
                    TITLES[1] = "facturas";
                } else {
                    TITLES = new String[1];
                    TITLES[0] = "info ";
                }
            }
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
                    return InfoGrupoFragment.newInstance(datosGrupo);
                case 1:
                    return UltimasFacturasFragment.newInstance(datosGrupo.getCodigo());
                default:
                    return SuperAwesomeCardFragment.newInstance(position);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_datos_grupo, container, false);
        //getActivity().setTitle(getResources().getString(R.string.detalle_grupo_title));
        getActivity().setTitle("Facturación");
        System.err.println("Facturas onCreateView ...");

        int unit = TypedValue.COMPLEX_UNIT_DIP;
        final int pageMargin = (int) TypedValue.applyDimension(unit, 4, getResources().getDisplayMetrics());
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar_login);
        layout = (LinearLayout) v.findViewById(R.id.form_login);
        stub = (ViewStub) v.findViewById(R.id.stub);

        if (datosGrupo != null) {
            adapter = new MyPagerAdapter(getChildFragmentManager());
            pager = (ViewPager) v.findViewById(R.id.paginador);
            pager.setAdapter(adapter);
            pager.setPageMargin(pageMargin);
            SlidingTabLayout tabs = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
            tabs.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
            tabs.setSelectedIndicatorColors(getResources().getColor(R.color.celeste_institucional));
            tabs.setDistributeEvenly(true);
            tabs.setViewPager(pager);
        } else {
            obtenerDatosGrupo();
        }
        return v;
    }

    private void setearMensajeDeFalla() {
        showProgress(false);
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_linea_invalida);
            stub.inflate();
            stub.setVisibility(View.VISIBLE);
        }
    }

    private void obtenerDatosGrupo() {

        showProgress(true);
        System.err.println("Facturas cargando : " + linea);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineasRequest = new ObtenerLineaRequest(getActivity(), new JacksonConverter(objectMapper));
        datosGrupoRequest = new ObtenerDatosGrupoRequest(getActivity(), new JacksonConverter(objectMapper));
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        System.err.println("guardarLineaSeleccionada obtenerDatosGrupo : " + linea);
        activity.getSpiceManager().execute(lineasRequest.datosLinea(linea), new DatosLineaRequestListener());
    }


    public final class DatosLineaRequestListener implements RequestListener<Linea> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if(getActivity() != null) {
                System.err.println("Facturas Error !!!");
                Toast.makeText(getActivity(), MensajesDeUsuario.SIN_DATOS_LINEA, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Linea datos) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (activity != null) {
                if (datos != null) {
                    String grupo = datos.getGrupoFacturacion();
                    System.err.println("Facturas cargando grupo : " + grupo);
                    activity.getSpiceManager().execute(datosGrupoRequest.get(grupo), new DatosGrupoRequestListener());
                } else {
                    setearMensajeDeFalla();
                }
            }
        }
    }

    public final class DatosGrupoRequestListener implements RequestListener<GrupoFacturacion> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if(getActivity() != null) {
                System.err.println("Facturas Error !!!");
                Toast.makeText(getActivity(), MensajesDeUsuario.SIN_DATOS_LINEA, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final GrupoFacturacion datos) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (activity != null) {
                System.err.println("Facturas listo !!!");
                int unit = TypedValue.COMPLEX_UNIT_DIP;
                final int pageMargin = (int) TypedValue.applyDimension(unit, 4, getResources().getDisplayMetrics());
                datosGrupo = datos;
                adapter = new MyPagerAdapter(getChildFragmentManager());
                pager = (ViewPager) getView().findViewById(R.id.paginador);
                pager.setAdapter(adapter);
                pager.setPageMargin(pageMargin);
                SlidingTabLayout tabs = (SlidingTabLayout) getView().findViewById(R.id.sliding_tabs);
                tabs.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
                tabs.setSelectedIndicatorColors(getResources().getColor(R.color.celeste_institucional));
                tabs.setDistributeEvenly(true);
                tabs.setViewPager(pager);
                showProgress(false);
            }
        }
    }

    public void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        layout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}
