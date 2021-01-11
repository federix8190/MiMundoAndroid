package py.com.personal.mimundo.fragments.home;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.disenhos.Roles;

import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.fragments.SuperAwesomeCardFragment;
import py.com.personal.mimundo.utils.PreferenceUtils;
import py.com.personal.mimundo.utils.StringUtils;
import py.com.personal.mimundo.widget.tab.SlidingTabLayout;

public class HomeFragment extends Fragment {

    private static final String SCREEN_NAME = "Home";
    //private Tracker traker;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    String tipoPersona;
    String tipoUsuario;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(true);
        }
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        tipoPersona = activity.obtenerTipoPersona();
        tipoUsuario = activity.obtenerTipoUsuario();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES;// = {"MI PERFIL ", "PLANES", "SERVICIOS", "EQUIPO"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();

            if (tipoPersona.equals("PERJUR") && (tipoUsuario.equals("TMP") || tipoUsuario.equals("RES"))) {

                TITLES = new String[4];
                TITLES[0] = "DASHBOARD";
                TITLES[1] = "PACKS";
                TITLES[2] = "SERVICIOS";
                TITLES[3] = "EQUIPO";

            } else {

                if (activity.obtenerRolesUsuario().contains(Roles.DATOS_LINEA)) {
                    TITLES = new String[5];
                    TITLES[0] = "DASHBOARD";
                    TITLES[1] = "PACKS";
                    TITLES[2] = "PLANES";
                    TITLES[3] = "SERVICIOS";
                    TITLES[4] = "EQUIPO";
                } else {
                    TITLES = new String[1];
                    TITLES[0] = "DASHBOARD";
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
                    return new DashboardFragment();
                case 1:
                    return PacksDeSaldoFragment.newInstance();
                case 2:
                    if (tipoPersona.equals("PERJUR") && (tipoUsuario.equals("TMP") || tipoUsuario.equals("RES"))) {
                        return ServiciosFragment.newInstance();
                    } else {
                        return PlanesFragment.newInstance();
                    }
                case 3:
                    if (tipoPersona.equals("PERJUR") && (tipoUsuario.equals("TMP") || tipoUsuario.equals("RES"))) {
                        return EquipoFragment.newInstance();
                    } else {
                        return ServiciosFragment.newInstance();
                    }
                case 4:
                    return EquipoFragment.newInstance();
                default:
                    return SuperAwesomeCardFragment.newInstance(position);
            }
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
        pager.setOffscreenPageLimit(4);

        SlidingTabLayout tabs = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
        tabs.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.celeste_institucional));
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.cargarLineasUsuario();
        activity.setTitle("mi mundo");
        activity.salirApp = true;
        activity.cargarLineasUsuario();
        activity.setActionBar(true, "HomeFragment");

        TextView lineaSeleccionadaView = (TextView)activity.getmDrawerList().findViewById(R.id.reservedNamedId);
        if (lineaSeleccionadaView != null) {
            String nroLinea = activity.obtenerLineaSeleccionada();
            if (nroLinea != null && StringUtils.esLineaTelefonia(nroLinea)) {
                lineaSeleccionadaView.setText("0" + nroLinea);
            } else {
                lineaSeleccionadaView.setText(nroLinea);
            }
        }

        MyApplication app = (MyApplication) getActivity().getApplication();
        //traker = app.getTracker(MyApplication.TrackerName.APP_TRACKER);
        //traker.setScreenName("Home");
        // Send a screen view.
        //traker.send(new HitBuilders.AppViewBuilder().build());
        //traker.send(new HitBuilders.ScreenViewBuilder().build());
        return v;
    }

}
