package py.com.personal.mimundo.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.octo.android.robospice.SpiceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import py.com.personal.mimundo.adapters.AdapterSpinnerActionBar;
import py.com.personal.mimundo.adapters.SlideMenuAdapter;

import py.com.personal.mimundo.disenhos.Roles;
import py.com.personal.mimundo.disenhos.Constantes;
import py.com.personal.mimundo.fragments.BilleteraPersonalFragment;
import py.com.personal.mimundo.fragments.CambioSimFragment;
import py.com.personal.mimundo.fragments.ClubPersonalFragment;
import py.com.personal.mimundo.fragments.MapaFragment;
//import py.com.personal.mimundo.fragments.backtones.BacktonesFragment;
import py.com.personal.mimundo.fragments.configuracion.ConfiguracionFragment;
import py.com.personal.mimundo.fragments.configuracion.NumerosGratisFragment;
import py.com.personal.mimundo.fragments.gestion.EditarPerfilFragment;
import py.com.personal.mimundo.fragments.gestion.facturacion.DetalleGrupoFacturacionFragment;
import py.com.personal.mimundo.fragments.gestion.facturacion.FacturasFragment;
import py.com.personal.mimundo.fragments.gestion.facturacion.ListaGruposFacturacionFragment;
import py.com.personal.mimundo.fragments.gestion.financiacion.ListaFinanciacionFragment;
import py.com.personal.mimundo.fragments.home.HomeFragment;
import py.com.personal.mimundo.fragments.saldo.PedirSaldoFragment;
import py.com.personal.mimundo.fragments.saldo.PinSesionFragment;
import py.com.personal.mimundo.fragments.saldo.packs.ListaPacksFragment;
import py.com.personal.mimundo.fragments.saldo.prestamo.PrestamoSaldoFragment;
import py.com.personal.mimundo.fragments.saldo.recarga.RecargaContraFacturaFragment;
import py.com.personal.mimundo.fragments.saldo.transferencias.TransferenciaCorporativaPaso1;
import py.com.personal.mimundo.fragments.saldo.transferencias.TransferenciaSaldoIndividualFragment;
import py.com.personal.mimundo.models.NavDrawerItem;
import py.com.personal.mimundo.services.SampleRetrofitSpiceService;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.models.ListaLineas;
import py.com.personal.mimundo.utils.PreferenceUtils;
import py.com.personal.mimundo.utils.StringUtils;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class BaseDrawerActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private SlideMenuAdapter adapter;
    public String fragmentActual;

    // Selector de lineas
    private Spinner selectorLineas;
    private EditText numeroLineaSelector;
    private AdapterSpinnerActionBar adapterSelectorLineas;
    private static List<Linea> lineasUsuario;
    private List<Linea> listaLineas;
    public int totalLineas;
    private String codigoOperacion;
    private Linea datosLineaSeleccionda;
    public boolean lineaSeleccionada = false;
    public boolean salirApp = true;
    private int posicionPedirSaldo;
    private int posicionLineaSeleccionada;

    @Inject
    private PreferenceUtils preferenceUtils;

    // used to store app title
    private static CharSequence mTitle;

    // Slide menu items
    private String[] MENU_GENERAL_TITLES;
    private String[] CODIGO_MENU_TITLES;
    private String[] ICONOS_MENU_GENERAL;
    private List<String> navMenuTitles;
    private List<String> navMenuIcons;
    private List<String> codigoNavMenuTitles;
    private List<Fragment> pantallasMenuGeneral;

    // SubMenu de saldos
    private String[] MENU_SALDO_TITLES;
    private String[] CODIGO_MENU_SALDO;
    private String[] ICONOS_MENU_SALDO;
    private List<String> navSubMenuSaldoTitles;
    private List<String> codigoNavSubMenuSaldoTitles;
    private List<String> navSubMenuSaldoIcons;
    private List<Fragment> pantallasSaldos;

    // SubMenu de Gestion
    String[] MENU_GESTION_TITLES;
    String[] CODIGO_MENU_GESTION;
    String[] ICONOS_MENU_GESTION;
    private List<String> navSubMenuGestionTitles;
    private List<String> codigoNavSubMenuGestionTitles;
    private List<String> navSubMenuMenuIcons;
    private List<Fragment> pantallasGestion;

    public String tipoRoaming;
    public Toolbar mToolbar;

    private SpiceManager spiceManager = new SpiceManager(SampleRetrofitSpiceService.class);

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
        //GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
        //GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }

    public ExpandableListView getmDrawerList() {
        return mDrawerList;
    }

    public List<String> getNavMenuTitles() {
        return navMenuTitles;
    }

    public List<String> getNavSubMenuSaldoTitles() {
        return navSubMenuSaldoTitles;
    }

    public List<String> getNavSubMenuGestionTitles() {
        return navSubMenuGestionTitles;
    }

    protected void inicializarVista(Bundle savedInstanceState) {

        // Carga las lineas del usuario
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        Bundle bundle = getIntent().getExtras();
        listaLineas = new ArrayList<Linea>();
        if (bundle != null) {
            String jsonLineas = (String) bundle.get("lineas");
            System.err.println("lineas : " + jsonLineas);
            totalLineas = (Integer) bundle.get("totalLineas");
            ListaLineas lista = new Gson().fromJson(jsonLineas, ListaLineas.class);
            lineasUsuario = lista.getLista();
            listaLineas = lista.getLista();

            String lineaBuscada = obtenerLineaSeleccionada();

            int i = 0;
            while (i < listaLineas.size()) {
                if (lineaBuscada != null && listaLineas != null && listaLineas.get(i) != null) {
                    if (listaLineas.get(i).toString().compareTo(lineaBuscada)==0) {
                        datosLineaSeleccionda = listaLineas.get(i);
                        lineaSeleccionada = false;
                        break;
                    }
                }
                i++;
            }
        }

        // Define los items que son fragments
        NavDrawerItem.itemsFragments = Arrays.asList(getResources().getStringArray(R.array.items_fragments));

        // nav drawer icons from resources
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.list_slidermenu);

        cargarMenuSaldos();
        cargarMenuGestion();
        cargarMenuPrincipal();
        fragmentActual = "HomeFragment";

        // Listener para el menu lateral
        OnGroupClickListener onGroupClickListener = new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Fragment fragment;
                int salirPosition = navMenuTitles.size() - 1;
                String codigo = codigoNavMenuTitles.get(groupPosition);

                if (groupPosition == salirPosition) {
                    new LogOutTask().execute();
                    return true;
                } else if (codigo.equals(CODIGO_MENU_TITLES[1]) || codigo.equals(CODIGO_MENU_TITLES[2])) {
                    return false;
                } else {
                    fragmentActual = CODIGO_MENU_TITLES[groupPosition];
                    fragment = pantallasMenuGeneral.get(groupPosition);
                    lineaSeleccionada = false;
                    NavDrawerItem.padreSeleccionado = groupPosition;
                    NavDrawerItem.hijoSeleccionado = -1;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                        fragmentManager.popBackStack();
                    }
                    fragmentManager.beginTransaction().replace(R.id.container, fragment, "").commit();
                    mDrawerList.setItemChecked(groupPosition, true);
                    mDrawerList.setSelection(groupPosition);
                    setTitle(navMenuTitles.get(groupPosition));
                    mDrawerLayout.closeDrawer(mDrawerList);
                    return true;
                }
                return false;
            }
        };

        // Listener para los items de los sub-menus
        OnChildClickListener onChildClickListener = new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Fragment fragment = null;
                List<String> titulos = null;
                String codigo = codigoNavMenuTitles.get(groupPosition);

                // Menu de Saldo
                if (codigo.equals(CODIGO_MENU_TITLES[1])) {

                    //si ya tiene el pin validado se muestra directo,
                    // sino pasa al nuevo fragment PinSesionFragment de validacion de pin

                    String nroLinea = obtenerLineaSeleccionada();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);

                    if (pref.contains("pinSesion") && pref.contains("TipoUsuario")) {
                        String tipoUsuario = pref.getString("TipoUsuario", "");

                        if ((tipoUsuario.compareTo(preferenceUtils.TIPO_USUARIO_NIVEL_1) != 0
                                && tipoUsuario.compareTo(preferenceUtils.TIPO2_USUARIO_NIVEL_1) != 0)
                                || childPosition == posicionPedirSaldo || pref.getBoolean("pinSesion", false) != false) {
                            titulos = navSubMenuSaldoTitles;
                            fragmentActual = CODIGO_MENU_SALDO[groupPosition];
                            fragment = pantallasSaldos.get(childPosition);
                        } else {
                            fragment = new PinSesionFragment().newInstance(childPosition, nroLinea, groupPosition);
                        }

                    } else {
                        fragmentActual = "PinSesionFragment";
                        fragment = new PinSesionFragment().newInstance(childPosition, nroLinea,groupPosition);
                        pantallasSaldos.get(childPosition);
                    }
                }
                // Menu de Gestion
                else if (codigo.equals(CODIGO_MENU_TITLES[2])) {
                    titulos = navSubMenuGestionTitles;
                    System.out.println("groupPosition : " + groupPosition + ", " + childPosition);
                    fragmentActual = CODIGO_MENU_GESTION[childPosition];
                    fragment = pantallasGestion.get(childPosition);
                }

                lineaSeleccionada = false;
                NavDrawerItem.padreSeleccionado = groupPosition;
                NavDrawerItem.hijoSeleccionado = childPosition;

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                        fragmentManager.popBackStack();
                    }
                    fragmentManager.beginTransaction().replace(R.id.container, fragment, "").commit();
                    mDrawerList.setItemChecked(childPosition, true);
                    mDrawerList.setSelection(childPosition);
                    if (codigo.equals(CODIGO_MENU_TITLES[2])) {
                        setTitle(titulos.get(childPosition));
                    }
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
                return true;
            }
        };

        mDrawerList.setOnChildClickListener(onChildClickListener);
        mDrawerList.setOnGroupClickListener(onGroupClickListener);

        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup) {
                    mDrawerList.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(BaseDrawerActivity.this,
                BaseDrawerActivity.this.mDrawerLayout,
                BaseDrawerActivity.this.mToolbar,
                R.string.app_name,
                R.string.app_name) {
            @Override
            public void onDrawerClosed(View view) {
                if (selectorLineas != null) {
                    selectorLineas.setVisibility(View.VISIBLE);
                }
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Mi mundo");
                supportInvalidateOptionsMenu();
                if (selectorLineas != null) {
                    selectorLineas.setVisibility(View.GONE);
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // On first time display view for first nav item
        if (savedInstanceState == null) {
            displayView(0);
        }
        setTitle(mTitle);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new HomeFragment();
        lineaSeleccionada = false;

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                fragmentManager.popBackStack();
            }
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(MENU_GENERAL_TITLES[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void setActionBar(boolean mostrarSelector, final String nombreFragment) {

        fragmentActual = nombreFragment;
        if (!mostrarSelector) {
            ActionBar actionBar = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customActionBarView = inflater.inflate(R.layout.layout_vacio, null);
            actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(Gravity.RIGHT));
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            return;
        }
        if (!lineaSeleccionada) {
            desplegarSelector(nombreFragment);
        } else {
            lineaSeleccionada = true;
        }
    }

    private void desplegarSelector(final String nombreFragment) {

        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.selector_lineas, null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                | ActionBar.DISPLAY_SHOW_TITLE
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(Gravity.RIGHT));

        // Listener para el selector de lineas
        OnItemSelectedListener listener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (lineaSeleccionada) {
                    datosLineaSeleccionda = listaLineas.get(i);
                    String numeroLinea = datosLineaSeleccionda.getNumeroLinea();
                    guardarLineaSeleccionada(numeroLinea);
                    if (StringUtils.esLineaTelefonia(numeroLinea)) {
                        numeroLinea = "0" + numeroLinea;
                    }
                    TextView header = (TextView) findViewById(R.id.reservedNamedId);
                    if (header != null) {
                        header.setText(numeroLinea);
                    }
                    crearFragment(nombreFragment, numeroLinea);
                } else {
                    lineaSeleccionada = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };

        if (listaLineas != null) {
            if (totalLineas <= 10) {
                if (listaLineas.size() > 0) {
                    String lineaBuscada = obtenerLineaSeleccionada();
                    int i = 0;
                    while (i < listaLineas.size()) {
                        if (listaLineas.get(i).toString().compareTo(lineaBuscada) == 0) {
                            datosLineaSeleccionda = listaLineas.get(i);
                            lineaSeleccionada = false;
                            posicionLineaSeleccionada = i;
                            break;
                        }
                        i++;
                    }
                    String numeroLinea = datosLineaSeleccionda.getNumeroLinea();
                    guardarLineaSeleccionada(numeroLinea);
                }

                adapterSelectorLineas = new AdapterSpinnerActionBar(true, this);
                for (Linea linea : listaLineas) {
                    if (StringUtils.esLineaTelefonia(linea.getNumeroLinea())) {
                        adapterSelectorLineas.addItem("0" + linea.getNumeroLinea());
                    } else {
                        adapterSelectorLineas.addItem(linea.getNumeroLinea());
                    }
                }
                // Agregamos el selector de lineas al ActionBar
                selectorLineas = (Spinner) actionBar.getCustomView().findViewById(R.id.selector_lineas);
                selectorLineas.setAdapter(adapterSelectorLineas);
                selectorLineas.setOnItemSelectedListener(listener);
                selectorLineas.setSelection(posicionLineaSeleccionada);
                selectorLineas.setVisibility(View.VISIBLE);

            } else {

                numeroLineaSelector = (EditText) actionBar.getCustomView().findViewById(R.id.numero_linea_selector_field);
                numeroLineaSelector.setVisibility(View.VISIBLE);
                if (!nombreFragment.equals("ListaGruposFacturacionFragment") && !nombreFragment.equals("DetalleGrupoFacturacionFragment")) {
                    String lineaBuscada = obtenerLineaSeleccionada();
                    numeroLineaSelector.setText(lineaBuscada);
                    guardarBusquedaFacturacion("");
                    guardarLineaFacturacion("");
                } else {
                    if (nombreFragment.equals("ListaGruposFacturacionFragment")) {
                        String lineaBuscada = obtenerBusquedaFacturacion();
                        numeroLineaSelector.setText(lineaBuscada);
                    } else if (nombreFragment.equals("DetalleGrupoFacturacionFragment")) {
                        String lineaBuscada = obtenerLineaFacturacion();
                        numeroLineaSelector.setText(lineaBuscada);
                    }
                }

                ImageView buscarLineaBtn = (ImageView) actionBar.getCustomView().findViewById(R.id.button_buscar_linea);
                buscarLineaBtn.setVisibility(View.VISIBLE);
                buscarLineaBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nroLinea = numeroLineaSelector.getText().toString();
                        if (nombreFragment.equals("ListaGruposFacturacionFragment")) {
                            Fragment fragment = ListaGruposFacturacionFragment.newInstance(nroLinea);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            guardarBusquedaFacturacion(nroLinea);
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        } else if (nombreFragment.equals("DetalleGrupoFacturacionFragment")) {
                            Fragment fragment = DetalleGrupoFacturacionFragment.newInstance(nroLinea);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            guardarLineaFacturacion(nroLinea);
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        } else {
                            guardarLineaSeleccionada(nroLinea);
                            lineaSeleccionada = true;
                            crearFragment(nombreFragment, nroLinea);
                        }
                    }
                });
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void crearFragment(String nombreFragment, String numeroLinea) {

        Fragment fragment = null;
        if (nombreFragment.equals("HomeFragment")) {
            fragment = new HomeFragment();
            fragmentActual = "HomeFragment";
        } else if (nombreFragment.equals("ListaPacksFragment")) {
            fragment = new ListaPacksFragment();
            fragmentActual = "ListaPacksFragment";
        } else if (nombreFragment.equals("PedirSaldoFragment")) {
            fragment = new PedirSaldoFragment();
            fragmentActual = "PedirSaldoFragment";
        } else if (nombreFragment.equals("PrestamoSaldoFragment")) {
            fragment = new PrestamoSaldoFragment();
            fragmentActual = "PrestamoSaldoFragment";
        } else if (nombreFragment.equals("RecargaContraFacturaFragment")) {
            fragment = new RecargaContraFacturaFragment();
            fragmentActual = "RecargaContraFacturaFragment";
        } else if (nombreFragment.equals("TransferenciaSaldoIndividualFragment")) {
            fragment = new TransferenciaSaldoIndividualFragment();
            fragmentActual = "TransferenciaSaldoIndividualFragment";
        } else if (nombreFragment.equals("ConfiguracionFragment")) {
            fragment = new ConfiguracionFragment();
            fragmentActual = "ConfiguracionFragment";
        } /*else if (nombreFragment.equals("AumentoLimiteConsumoFragment")) {
            fragment = new AumentoLimiteConsumoFragment();
            fragmentActual = "AumentoLimiteConsumoFragment";
        } else if (nombreFragment.equals("BacktonesFragment")) {
            fragment = new BacktonesFragment();
            fragmentActual = "BacktonesFragment";
        }*/ else if (nombreFragment.equals("DetalleGrupoFacturacionFragment")) {
            fragment = new DetalleGrupoFacturacionFragment();
            fragmentActual = "DetalleGrupoFacturacionFragment";
        } else if (nombreFragment.equals("ListaGruposFacturacionFragment")) {
            fragment = new ListaGruposFacturacionFragment();
            fragmentActual = "ListaGruposFacturacionFragment";
        } else if (nombreFragment.equals("NumerosGratisFragment")) {
            fragment = new NumerosGratisFragment();
            fragmentActual = "NumerosGratisFragment";
        } else if (nombreFragment.equals("FacturasFragment")) {
            String tipoPersona = obtenerTipoPersona();
            String tipoUsuario = obtenerTipoUsuario();
            if (tipoPersona.equals("PERJUR") && (tipoUsuario.equals("TMP") || tipoUsuario.equals("RES"))) {
                Toast.makeText(this, getResources().getString(R.string.error_acceso), Toast.LENGTH_SHORT).show();
            } else {
                fragment = new FacturasFragment();
                fragmentActual = "FacturasFragment";
            }
        } else if (nombreFragment.equals("AtencionPersonalFragment")) {
            ((EditText) findViewById(R.id.nro_linea_field)).setText(numeroLinea);
            return;
        } else if (nombreFragment.equals("CambioSimFragment")) {
            TextView lineaSeleccionadaTextView = (TextView) findViewById(R.id.numro_linea_seleccionada);
            lineaSeleccionadaTextView.setText("Linea Seleccionada : " + numeroLinea);
            fragment = new CambioSimFragment();
            fragmentActual = "CambioSimFragment";
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void showBuscadorGrupo(String codigo) {

        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.selector_lineas, null);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                | ActionBar.DISPLAY_SHOW_TITLE
                | ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(Gravity.RIGHT));
        ImageView buscarLineaBtn = (ImageView) actionBar.getCustomView().findViewById(R.id.button_buscar_linea);
        buscarLineaBtn.setVisibility(View.VISIBLE);
        numeroLineaSelector = (EditText) actionBar.getCustomView().findViewById(R.id.numero_linea_selector_field);
        numeroLineaSelector.setVisibility(View.VISIBLE);
        //String lineaBuscada = obtenerBusquedaFacturacion();
        numeroLineaSelector.setText(codigo);
        selectorLineas = (Spinner) actionBar.getCustomView().findViewById(R.id.selector_lineas);
        selectorLineas.setVisibility(View.GONE);
        buscarLineaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nroLinea = numeroLineaSelector.getText().toString();
                Fragment fragment = ListaGruposFacturacionFragment.newInstance(nroLinea);
                FragmentManager fragmentManager = getSupportFragmentManager();
                guardarBusquedaFacturacion(nroLinea);
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
    }

    public void seleccionarPrimerItem() {
        if (selectorLineas != null) {
            selectorLineas.setSelection(0);
        }
    }

    public void seleccionarItem(int posicion) {
        if (selectorLineas != null) {
            selectorLineas.setSelection(posicion);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (salirApp) {
            if (fragmentActual != null && !fragmentActual.equals("HomeFragment")) {
                goToHome();
            } else {
                mostrarDialogo();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void goToHome() {
        fragmentActual = "HomeFragment";
        NavDrawerItem.padreSeleccionado = 0;
        NavDrawerItem.hijoSeleccionado = -1;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (navMenuTitles.contains("Saldo")) {
            mDrawerList.collapseGroup(1);
        }
        if (navMenuTitles.contains("Gestión")) {
            if (navMenuTitles.contains("Saldo")) {
                mDrawerList.collapseGroup(2);
            } else {
                mDrawerList.collapseGroup(1);
            }
        }
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        cargarLineasUsuario();
        String lineaBuscada = obtenerLineaSeleccionada();
        guardarLineaSeleccionada(lineaBuscada);
        TextView lineaSeleccionadaView = (TextView) findViewById(R.id.reservedNamedId);
        String nroLinea = obtenerLineaSeleccionada();
        if (StringUtils.esLineaTelefonia(nroLinea)) {
            nroLinea = "0" + nroLinea;
        }
        if (lineaSeleccionadaView != null) {
            lineaSeleccionadaView.setText(nroLinea);
        }
        lineaSeleccionada = false;
        setActionBar(true, "HomeFragment");
        fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment(), "").commit();
        mDrawerList.setItemChecked(0, true);
        mDrawerList.setSelection(0);
        setTitle(navMenuTitles.get(0));
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    /**
     * Carga todas las lineas del usuario
     */
    public void cargarLineasUsuario() {
        codigoOperacion = null;
        listaLineas = lineasUsuario;
    }

    public String obtenerLineaSeleccionada() {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        return preferenceUtils.getLineaSeleccionada();
    }

    public void guardarBusquedaFacturacion(String numeroLinea) {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        preferenceUtils.guardarBusquedaFacturacion(numeroLinea);
    }

    public String obtenerBusquedaFacturacion() {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        return preferenceUtils.getBusquedaFacturacion();
    }

    public void guardarLineaFacturacion(String numeroLinea) {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        preferenceUtils.guardarLineaFacturacion(numeroLinea);
    }

    public String obtenerLineaFacturacion() {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        return preferenceUtils.getLineaFacturacion();
    }

    public String obtenerNombreUsuario() {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        return preferenceUtils.getNombreUsuario();
    }

    public String obtenerTipoPersona() {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        return preferenceUtils.getTipoPersona();
    }

    public String obtenerTipoUsuario() {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        return preferenceUtils.getTipoUsuario();
    }

    public Linea obtenerDatosLineaSeleccionada() {
        return datosLineaSeleccionda;
    }

    public String obtenerCodigoUsuario() {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        return preferenceUtils.getCodigoUsuario();
    }

    public List<String> obtenerRolesUsuario() {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        return preferenceUtils.getRolesUsuario();
    }

    public boolean esOperacionActual(String operacion) {
        if (codigoOperacion != null && codigoOperacion.equals(operacion)) {
            return true;
        }
        return false;
    }

    public void cargarListaLineas(List<Linea> lista, String operacion) {
        if (lista != null) {
            codigoOperacion = operacion;
            listaLineas = lista;

            String lineaBuscada = obtenerLineaSeleccionada();

            int i = 0;
            while (i < listaLineas.size()) {
                if(listaLineas.get(i).toString().compareTo(lineaBuscada)==0) {
                    datosLineaSeleccionda = listaLineas.get(i);
                    lineaSeleccionada = false;
                    posicionLineaSeleccionada = i;
                    break;
                }
                i++;
            }
            String numeroLinea = datosLineaSeleccionda.getNumeroLinea();
            guardarLineaSeleccionada(numeroLinea);
        }
    }

    public String getUserName() {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
        String user = pref.getString("user", "");
        return user;
    }

    public void guardarLineaSeleccionada(String numeroLinea) {
        preferenceUtils = new PreferenceUtils(BaseDrawerActivity.this);
        preferenceUtils.guardarLineaSeleccionada(numeroLinea);
    }

    public List<Linea> getListaLineas() {
        return listaLineas;
    }

    public int getPosicionLinea(String nro) {
        int i = 0;
        for (Linea linea : listaLineas) {
            if (linea.getNumeroLinea().equals(nro)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    /**
     * Carga los items del Menu principal
     */
    private void cargarMenuPrincipal() {

        MENU_GENERAL_TITLES = getResources().getStringArray(R.array.nav_drawer_items);
        CODIGO_MENU_TITLES = getResources().getStringArray(R.array.codigo_nav_drawer_items);
        ICONOS_MENU_GENERAL = getResources().getStringArray(R.array.nav_drawer_icons);
        navMenuTitles = new ArrayList<String>();
        navMenuIcons = new ArrayList<String>();
        codigoNavMenuTitles = new ArrayList<String>();
        pantallasMenuGeneral = new ArrayList<Fragment>();

        // Home
        agregarItemMenuPrincipal(0);
        pantallasMenuGeneral.add(new HomeFragment());

        // Saldos
        if (pantallasSaldos.size() > 0) {
            agregarItemMenuPrincipal(1);
            pantallasMenuGeneral.add(null);
        }

        // Gestion
        if (pantallasGestion.size() > 0) {
            agregarItemMenuPrincipal(2);
            pantallasMenuGeneral.add(null);
        }

        // Configuraciones
        boolean tieneSuspensionRestitucion = obtenerRolesUsuario().contains(Roles.SUSPENDER_RESTITUIR);
        boolean tieneDestinosGratuitos = obtenerRolesUsuario().contains(Roles.DESTINOS_GRATUITOS);
        boolean tieneRecargaContraFactura = obtenerRolesUsuario().contains(Roles.ACTIVAR_RECARGA_FACTURA);
        boolean tieneRoaming = obtenerRolesUsuario().contains(Roles.ACTIVAR_ROAMING);
        if (tieneSuspensionRestitucion || tieneDestinosGratuitos || tieneRoaming || tieneRecargaContraFactura) {
            agregarItemMenuPrincipal(3);
            pantallasMenuGeneral.add(new ConfiguracionFragment());
        }

        // Puntos cercanos
        agregarItemMenuPrincipal(5);
        pantallasMenuGeneral.add(new MapaFragment());

        // Club Personal
        String tipoPersona = preferenceUtils.getTipoPersona();
        boolean tieneRolClubPersonal = obtenerRolesUsuario().contains(Roles.CLUB_PERSONAL);
        if (tipoPersona.equals(Constantes.TIPO_PERSONA_FISICA) && tieneRolClubPersonal) {
            agregarItemMenuPrincipal(6);
            pantallasMenuGeneral.add(new ClubPersonalFragment());
        }

        // Billetera Personal
        agregarItemMenuPrincipal(7);
        pantallasMenuGeneral.add(new BilleteraPersonalFragment());

        // Backtones
        /*boolean tieneRolBacktones = obtenerRolesUsuario().contains(Roles.MODIFICAR_BACKTONES);
        if (tieneRolBacktones) {
            agregarItemMenuPrincipal(9);
            pantallasMenuGeneral.add(new BacktonesFragment());
        }*/

        // Cambio de Sim
        if (tipoPersona.equals(Constantes.TIPO_PERSONA_JURIDICA)
                && preferenceUtils.getTipoUsuario().equals(Constantes.USUARIO_TITULAR)) {
            agregarItemMenuPrincipal(10);
            pantallasMenuGeneral.add(new CambioSimFragment());
        }

        // Salir
        agregarItemMenuPrincipal(11);
        pantallasMenuGeneral.add(null);

        // adding nav drawer items to array
        NavDrawerItem itemDelSlider;
        navDrawerItems = new ArrayList<NavDrawerItem>();
        for (int posicion = 0; posicion < navMenuTitles.size(); posicion++) {
            itemDelSlider = new NavDrawerItem(navMenuTitles.get(posicion), navMenuIcons.get(posicion), codigoNavMenuTitles.get(posicion));
            navDrawerItems.add(itemDelSlider);

            if (itemDelSlider.getNombreCLase().equals(CODIGO_MENU_TITLES[1])) {
                List<NavDrawerItem> subItems = new ArrayList<NavDrawerItem>();
                for (int i = 0; i < navSubMenuSaldoTitles.size(); i++) {
                    subItems.add(new NavDrawerItem(
                            navSubMenuSaldoTitles.get(i),
                            navSubMenuSaldoIcons.get(i),
                            codigoNavSubMenuSaldoTitles.get(i)));
                }
                navDrawerItems.get(posicion).setHijos(subItems);
            } else if (itemDelSlider.getNombreCLase().equals(CODIGO_MENU_TITLES[2])) {
                List<NavDrawerItem> subItems = new ArrayList<NavDrawerItem>();
                for (int i = 0; i < navSubMenuGestionTitles.size(); i++) {
                    subItems.add(new NavDrawerItem(
                            navSubMenuGestionTitles.get(i),
                            navSubMenuMenuIcons.get(i),
                            codigoNavSubMenuGestionTitles.get(i)));
                }
                navDrawerItems.get(posicion).setHijos(subItems);
            }
        }

        // setting the nav drawer list adapter
        adapter = new SlideMenuAdapter(this, navDrawerItems);

        View.OnClickListener editarPerfilListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarPerfilFragment fragment = new EditarPerfilFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                mDrawerLayout.closeDrawer(mDrawerList);
                //Intent intent = new Intent(getBaseContext(), EditarPerfilActivity.class);
                //startActivity(intent);
            }
        };

        // Nombre del usuario en la Cabecera del menu
        TextView userName = new TextView(this);
        userName.setText(obtenerNombreUsuario());
        userName.setId(R.id.reservedNamedId_2);
        userName.setTextColor(getResources().getColor(R.color.white));
        userName.setTextSize(14.0f);
        LinearLayout.LayoutParams paramsUserName = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        userName.setLayoutParams(paramsUserName);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/CarroisGothic-Regular.ttf");
        userName.setTypeface(tf);

        // Linea seleccioanda en la Cabecera del menu
        TextView lineaSeleccionadaView = new TextView(this);
        lineaSeleccionadaView.setId(R.id.reservedNamedId);
        String nroLinea = obtenerLineaSeleccionada();
        if (StringUtils.esLineaTelefonia(nroLinea)) {
            nroLinea = "0" + nroLinea;
        }
        lineaSeleccionadaView.setText(nroLinea);
        lineaSeleccionadaView.setTextSize(16);
        lineaSeleccionadaView.setTextSize(16.0f);
        lineaSeleccionadaView.setTextColor(getResources().getColor(R.color.white));
        RelativeLayout.LayoutParams paramsLineaSeleccionada = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLineaSeleccionada.addRule(RelativeLayout.BELOW, R.id.reservedNamedId_2);
        lineaSeleccionadaView.setLayoutParams(paramsLineaSeleccionada);
        lineaSeleccionadaView.setTypeface(Typeface.create("", Typeface.BOLD));

        // Nombre del usuario y linea seleccionada
        RelativeLayout.LayoutParams paramsLayout2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLayout2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        RelativeLayout layout2 = new RelativeLayout(this);
        layout2.setLayoutParams(paramsLayout2);
        layout2.addView(userName);
        layout2.addView(lineaSeleccionadaView);
        RelativeLayout.LayoutParams paramsLayout1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout layout1 = new RelativeLayout(this);
        layout1.setLayoutParams(paramsLayout1);
        layout1.addView(layout2);

        // Imagen de la Cabecera del menu
        LinearLayout.LayoutParams paramsLogo = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLogo.gravity = Gravity.CENTER_VERTICAL;
        ImageView userLogo = new ImageView(this);
        userLogo.setLayoutParams(paramsLogo);
        userLogo.setPadding(8, 8, 8, 8);
        userLogo.setImageResource(R.drawable.avatar);
        userLogo.setOnClickListener(editarPerfilListener);

        // Agregamos la Cabecera al Menu Principal
        LinearLayout layoutCabecera = new LinearLayout(this);
        layoutCabecera.setOrientation(LinearLayout.HORIZONTAL);
        layoutCabecera.setBackgroundColor(getResources().getColor(R.color.menu_lateral_header));
        layoutCabecera.addView(userLogo);
        layoutCabecera.addView(layout1);
        mDrawerList.addHeaderView(layoutCabecera);
        mDrawerList.setAdapter(adapter);
    }

    private void agregarItemMenuPrincipal(int i) {
        navMenuTitles.add(MENU_GENERAL_TITLES[i]);
        codigoNavMenuTitles.add(CODIGO_MENU_TITLES[i]);
        navMenuIcons.add(ICONOS_MENU_GENERAL[i]);
    }

    /**
     * Carga los items del Subenu de saldos
     */
    private void cargarMenuSaldos() {
        MENU_SALDO_TITLES = getResources().getStringArray(R.array.nav_drawer_items_saldo);

        for (int i = 0; i < MENU_SALDO_TITLES.length; i++) {
            if (MENU_SALDO_TITLES[i].toString().compareTo("Pedir Saldo") == 0) {
                posicionPedirSaldo = i;
                break;
            };
        }

        int restarAlMenu = 0;

        CODIGO_MENU_SALDO = getResources().getStringArray(R.array.codigo_nav_drawer_items_saldo);
        ICONOS_MENU_SALDO = getResources().getStringArray(R.array.nav_saldo_icons);
        pantallasSaldos = new ArrayList<Fragment>();
        navSubMenuSaldoTitles = new ArrayList<String>();
        codigoNavSubMenuSaldoTitles = new ArrayList<String>();
        navSubMenuSaldoIcons = new ArrayList<String>();

        int i = 0;
        if (obtenerRolesUsuario().contains(Roles.COMPRA_PACKS)) {
            pantallasSaldos.add(new ListaPacksFragment());
            agregarItemMenuSaldo(i);
        } else {
            restarAlMenu++;
        }
        if (obtenerRolesUsuario().contains(Roles.PEDIR_SALDO)) {
            pantallasSaldos.add(new PedirSaldoFragment());
            agregarItemMenuSaldo(i+1);
        }
        if (obtenerRolesUsuario().contains(Roles.PRESTAR_SALDO)) {
            pantallasSaldos.add(new PrestamoSaldoFragment());
            agregarItemMenuSaldo(i+2);
        }
        if (obtenerRolesUsuario().contains(Roles.RECARGA_CONTRA_FACTURA)) {
            pantallasSaldos.add(new RecargaContraFacturaFragment());
            agregarItemMenuSaldo(i+3);
        }
        if (obtenerRolesUsuario().contains(Roles.TRANSFERENCIA_SALDO)) {
            if (preferenceUtils.getTipoPersona().equals(Constantes.TIPO_PERSONA_FISICA)) {
                pantallasSaldos.add(TransferenciaSaldoIndividualFragment.newInstance());
            } else if (preferenceUtils.getTipoPersona().equals(Constantes.TIPO_PERSONA_JURIDICA)) {
                pantallasSaldos.add(TransferenciaCorporativaPaso1.newInstance());
            }
            agregarItemMenuSaldo(i+4);
        }
        if (restarAlMenu > 0) {
            posicionPedirSaldo = posicionPedirSaldo - restarAlMenu;
        }
    }

    /**
     * Carga los items del Subenu de gestion
     */
    private void cargarMenuGestion() {
        MENU_GESTION_TITLES = getResources().getStringArray(R.array.nav_drawer_items_gestion);
        CODIGO_MENU_GESTION = getResources().getStringArray(R.array.codigo_nav_drawer_items_gestion);
        ICONOS_MENU_GESTION = getResources().getStringArray(R.array.nav_gestion_icons);
        pantallasGestion = new ArrayList<Fragment>();
        navSubMenuGestionTitles = new ArrayList<String>();
        codigoNavSubMenuGestionTitles = new ArrayList<String>();
        navSubMenuMenuIcons = new ArrayList<String>();

        // Facturacion
        if (obtenerRolesUsuario().contains(Roles.FACTURACION)) {
            String tipoPersona = obtenerTipoPersona();
            String tipoUsuario = obtenerTipoUsuario();
            if (tipoPersona.equals("PERJUR") && (tipoUsuario.equals("TMP") || tipoUsuario.equals("RES"))) {
                ;
            } else {
                pantallasGestion.add(new FacturasFragment());
                agregarItemMenuGestion(0);
            }
        }

        if (obtenerRolesUsuario().contains(Roles.FACTURACION)) {
            pantallasGestion.add(new ListaGruposFacturacionFragment());
            agregarItemMenuGestion(1);
        }

        // Financiacion
        if (obtenerRolesUsuario().contains(Roles.FINANCIACION)) {
            pantallasGestion.add(new ListaFinanciacionFragment());
            agregarItemMenuGestion(2);
        }
    }

    private void agregarItemMenuSaldo(int i) {
        navSubMenuSaldoTitles.add(MENU_SALDO_TITLES[i]);
        codigoNavSubMenuSaldoTitles.add(CODIGO_MENU_SALDO[i]);
        navSubMenuSaldoIcons.add(ICONOS_MENU_SALDO[i]);
    }

    private void agregarItemMenuGestion(int i) {
        navSubMenuGestionTitles.add(MENU_GESTION_TITLES[i]);
        codigoNavSubMenuGestionTitles.add(CODIGO_MENU_GESTION[i]);
        navSubMenuMenuIcons.add(ICONOS_MENU_GESTION[i]);
    }

    /**
     * Tarea para cerrar la sesion del Usuario logueado actualmente.
     */
    public class LogOutTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            preferenceUtils.borrarDatosSession();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            finish();
        }
    }

    public void mostrarDialogo() {
        DialogFragment newFragment = SalirDialogFragment.newInstance("Confirmar", getString(R.string.salir_app));
        newFragment.show(getSupportFragmentManager(), "Confirmar");
    }

    public void salirPositiveClick() {
        finish();
    }

    public void salirNegativeClick() {
    }

    public static class SalirDialogFragment extends DialogFragment {

        public static SalirDialogFragment newInstance(String title, String message) {
            SalirDialogFragment frag = new SalirDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.alta_usuario_btn_aceptar,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((BaseDrawerActivity)getActivity()).salirPositiveClick();
                                }
                            }
                    )
                    .setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((BaseDrawerActivity)getActivity()).salirNegativeClick();
                                }
                            }
                    ).create();
        }
    }

    public void mostrarMensaje(final String titulo, final String msg, boolean volver) {

        DialogFragment newFragment = MostrarMensajeDialogFragment.newInstance(titulo, msg, volver);
        newFragment.show(getSupportFragmentManager(), "Dialogo-Mensaje");
    }

    public void mostrarMensajeNeutralClick(boolean volver) {

        if (volver) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public static class MostrarMensajeDialogFragment extends DialogFragment {

        public static MostrarMensajeDialogFragment newInstance(String title, String message, final boolean volver) {
            MostrarMensajeDialogFragment frag = new MostrarMensajeDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            args.putBoolean("volver",volver);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");
            final Boolean volver = getArguments().getBoolean("volver");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((BaseDrawerActivity)getActivity()).mostrarMensajeNeutralClick(volver);
                                }
                            }
                    )
                    .create();
        }
    }

    public static List<Linea> getLineasUsuario() {
        if (lineasUsuario != null) {
            return lineasUsuario;
        }
        return new ArrayList<Linea>();
    }

}


