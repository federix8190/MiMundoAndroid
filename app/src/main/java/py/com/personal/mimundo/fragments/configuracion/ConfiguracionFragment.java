package py.com.personal.mimundo.fragments.configuracion;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import py.com.personal.mimundo.disenhos.Roles;
import py.com.personal.mimundo.widget.tab.SlidingTabLayout;

/**
 * Created by Konecta on 06/10/2014.
 */
public class ConfiguracionFragment extends Fragment {

    private ViewPager pager;
    private MyPagerAdapter adapter;

    public static ConfiguracionFragment newInstance() {
        return new ConfiguracionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(true);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private boolean tieneSuspensionRestitucion = false;
        private boolean tieneDestinosGratuitos = false;
        private boolean tieneRecargaContraFactura = false;
        private boolean tieneRoaming = false;

        private final List<String> TITLES;
        private final List<Fragment> FRAGMENTS;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            TITLES = new ArrayList<String>();
            FRAGMENTS = new ArrayList<Fragment>();

            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            tieneSuspensionRestitucion = activity.obtenerRolesUsuario().contains(Roles.SUSPENDER_RESTITUIR);
            tieneDestinosGratuitos = activity.obtenerRolesUsuario().contains(Roles.DESTINOS_GRATUITOS);
            tieneRecargaContraFactura = activity.obtenerRolesUsuario().contains(Roles.ACTIVAR_RECARGA_FACTURA);
            tieneRoaming = activity.obtenerRolesUsuario().contains(Roles.ACTIVAR_ROAMING);

            if (tieneSuspensionRestitucion) {
                TITLES.add("suspensión temporal");
                FRAGMENTS.add(SuspencionTemporalFragment.newInstance());
            }
            if (tieneDestinosGratuitos) {
                TITLES.add("destinos gratuitos");
                FRAGMENTS.add(DestinosGratuitosFragment.newInstance());
            }
            /*if (tieneRoaming) {
                TITLES.add("roaming");
                FRAGMENTS.add(RoamingFragment.newInstance());
            }*/
            if (tieneRecargaContraFactura) {
                TITLES.add("recarga contra factura");
                FRAGMENTS.add(RecargaContraFacturaFragment.newInstance());
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES.get(position);
        }

        @Override
        public int getCount() {
            return TITLES.size();
        }

        @Override
        public Fragment getItem(int position) {
            return FRAGMENTS.get(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        int unit = TypedValue.COMPLEX_UNIT_DIP;
        final int pageMargin = (int) TypedValue.applyDimension(unit, 2, getResources().getDisplayMetrics());
        adapter = new MyPagerAdapter(getChildFragmentManager());
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setPageMargin(pageMargin);
        pager.setOffscreenPageLimit(3);

        SlidingTabLayout tabs = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
        tabs.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.celeste_institucional));
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.cargarLineasUsuario();
        activity.setTitle("Configuración");
        if (!activity.lineaSeleccionada) {
            activity.setActionBar(true, "ConfiguracionFragment");
        } else {
            activity.lineaSeleccionada = true;
        }

       return v;
    }

}
