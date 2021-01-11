package py.com.personal.mimundo.fragments;

/**
 * Created by Konecta on 21/10/2014.
 */

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.PuntosCercanos.models.PuntoCercano;
import py.com.personal.mimundo.services.PuntosCercanos.service.PuntosCercanosRequest;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class MapaFragment extends SupportMapFragment implements
        //GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener, OnMapReadyCallback, LocationListener {

    private static final String LATITUD_INICIAL = "-25.286562";
    private static final String LONGITUD_INICIAL = "-57.636266";
    //-25.286562, -57.636266

    List<String> idMarkers;

    //GoogleApi mGoogleApiClient;
    Location mLastLocation;

    private static final int RC_SIGN_IN = 9001;
    //private GoogleSignInClient mSignInClient;

    SectionsPagerAdapter mSectionsPagerAdapter;
    static ViewPager mViewPager;

    private static View view;
    private GoogleMap mMap;

    private int marcadoActual;
    private LinearLayout contenedorCheck;

    private PuntosCercanosRequest mPuntosCercanosRequest;
    private List<PuntoCercano> listaPuntosCercanosTotal;
    private List<PuntoCercano> listaPuntosCercanosActual;

    private ProgressBar progresbar;

    private static Typeface tf = null;
    private Activity context = null;

    //    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mLastLocation = null;
        //buildGoogleApiClient(activity);
    }

    /*protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApi.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onInflate(Activity arg0, AttributeSet arg1, Bundle arg2) {
        super.onInflate(arg0, arg1, arg2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        System.err.println("Cargar puntos onCreateView");

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setTitle("Puntos Cercanos");
        activity.lineaSeleccionada = false;
        activity.setActionBar(false, "MapaFragment");

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = (FrameLayout) inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }

        progresbar = (ProgressBar) view.findViewById(R.id.progressbar_Map);
        //progresbar.setVisibility(View.VISIBLE);
        progresbar.bringToFront();

        //setHasOptionsMenu(true);

        listaPuntosCercanosTotal = null;
        listaPuntosCercanosActual = null;
        mSectionsPagerAdapter = null;
        mViewPager = null;

        if (getActivity() != null) {
            this.context = getActivity();
        }
        if (context != null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        mPuntosCercanosRequest = new PuntosCercanosRequest(getActivity(), new JacksonConverter(objectMapper));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMap = null;
        marcadoActual = 0;
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (status == ConnectionResult.SUCCESS) {
            setUpMapIfNeeded();
        } else {
            String error = GooglePlayServicesUtil.getErrorString(status);
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            SupportMapFragment mapaContenedor = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
            if (mapaContenedor == null) {
                mapaContenedor = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            }
            mapaContenedor.getMapAsync(this);
        }
        setUpMap();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.puntos_cercanos, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        listaPuntosCercanosActual = null;
        switch (item.getItemId()) {
            case R.id.todosPuntosMapa:
                //newGame();
                listaPuntosCercanosActual = listaPuntosCercanosTotal;
                //metodo de filtrado por parametro
                cargar_pager();
                setUpMapIfNeeded();
                break;
            case R.id.sucursalesMapa:
                //showHelp();
                listaPuntosCercanosActual = filtradoPuntosCercanos("Oficina");
                cargar_pager();
                setUpMapIfNeeded();
                break;

            case R.id.puntosVentaMapa:
                listaPuntosCercanosActual = filtradoPuntosCercanos("Maxicarga");
                cargar_pager();
                setUpMapIfNeeded();
                break;
            case R.id.Envios:
                listaPuntosCercanosActual = filtradoPuntosCercanos("Envios Personal");
                cargar_pager();
                setUpMapIfNeeded();
                break;
        }

        item.setChecked(true);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.err.println("Cargar puntos onConnected");
        mMap = googleMap;
        cargar_puntos_cercanos();

        /*LatLng asuncion = new LatLng(-25.2984145, -57.637604);
        mMap.addMarker(new MarkerOptions().position(asuncion).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(asuncion));
        UiSettings settings = mMap.getUiSettings();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(asuncion).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
    }

    private void cargar_puntos_cercanos() {

        System.err.println("Cargar puntos cercanos");

        if (listaPuntosCercanosTotal == null) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (mLastLocation != null) {
                String latitudString = new Double(mLastLocation.getLatitude()).toString();
                String longitudString = new Double(mLastLocation.getLongitude()).toString();
                activity.getSpiceManager().execute(mPuntosCercanosRequest.getPuntosCercanos(latitudString, longitudString),
                        new PuntosRequestListener());
            } else {
                activity.getSpiceManager().execute(
                        mPuntosCercanosRequest.getPuntosCercanos(LATITUD_INICIAL, LONGITUD_INICIAL),
                        new PuntosRequestListener()
                );
            }
        }
    }

    private void cargar_pager() {
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (mViewPager != null) {
            mViewPager.removeAllViewsInLayout();
        }
        mViewPager = null;
        mSectionsPagerAdapter = null;

        if (mViewPager == null) {
            mViewPager = (ViewPager) view.findViewById(R.id.pager);
        }

        if (mSectionsPagerAdapter == null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(activity.getSupportFragmentManager());
        }
        // Set up the ViewPager with the sections adapter
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int posicion) {
                LatLng position = dibujarPuntosMapa(posicion);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        position).zoom(16).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.bringToFront();
    }

    public static void MoveTo(int position) {
        //it doesn't matter if you're already in the first item
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (idMarkers != null && idMarkers.size() > 0) {
            int i = idMarkers.indexOf(marker.getId());
            MoveTo(i);
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public final class PuntosRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {

                progresbar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List datos) {
            if (getActivity() != null) {
                progresbar.setVisibility(View.GONE);
                if (datos != null) {
                    if (datos.size() == 0) {
                        Toast.makeText(getActivity(), "No existen puntos disponibles para su posición actual", Toast.LENGTH_SHORT).show();
                    }

                    idMarkers = new ArrayList<String>();
                    for (Object p : datos) {
                        PuntoCercano punto = (PuntoCercano) p;
                        System.err.println("Cargar puntos calle : " + punto.getCallePrincipal());
                        addMarker(punto);
                    }

                    listaPuntosCercanosTotal = filtradoPuntosCercanos(datos);
                    listaPuntosCercanosActual = listaPuntosCercanosTotal;

                    LatLng posicion = getUbicacionActual();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
                    UiSettings settings = mMap.getUiSettings();
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(16).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    //setUpMapIfNeeded();
                    //cargar_pager();

                } else {
//                    setearEstadoSinServicio();
                    Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private LatLng getUbicacionActual() {

        LatLng posicion = new LatLng(-25.286562, -57.636266);
        try {
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            String provider = LocationManager.NETWORK_PROVIDER;
            float minDistanceM = 0.0f;
            LocationListener listener = MapaFragment.this;
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                System.err.println("getUbicacionActual - no tiene permisos ");
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                return posicion;
            }
            lm.requestLocationUpdates(provider, 0, minDistanceM, listener);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            posicion = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            System.err.println("getUbicacionActual error : " + e.getMessage());
            posicion = new LatLng(-25.286562, -57.636266);
        }
        return posicion;
    }

    private void setUpMap() {

        if (mMap == null) {
            return;
        }
        // change some settings
        UiSettings settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(false);
        settings.setCompassEnabled(true);
        settings.setTiltGesturesEnabled(true);

        // show the current position
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.clear();

        if (listaPuntosCercanosActual != null && listaPuntosCercanosActual.size() > 0) {
            LatLng position = dibujarPuntosMapa(marcadoActual);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    position).zoom(18).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void addMarker(PuntoCercano punto) {

        String tipo = clasificadorPuntos(punto);
        LatLng posicion = new LatLng(punto.getLatitud(), punto.getLongitud());

        int icon = R.drawable.ic_todos_map;
        if (tipo.compareTo("Maxicarga") == 0 ) {
            icon = R.drawable.ic_puntosventa_map;
        } else if (tipo.compareTo("Oficina") == 0 ) {
            icon = R.drawable.ic_sucursales_map;
        } else if (tipo.compareTo("Envios") == 0 ) {
            icon = R.drawable.ic_envios_map;
        }

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(posicion)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
        idMarkers.add(marker.getId());
        marker.showInfoWindow();
        //mMap.addMarker(new MarkerOptions().position(posicion).title(punto.getRazonSocial()));

    }

    public List<PuntoCercano> filtradoPuntosCercanos(List<PuntoCercano> listaFiltrar) {
        List<PuntoCercano> listaFiltrada = new ArrayList<PuntoCercano>();
        if (listaFiltrar != null && listaFiltrar.size() > 0) {
            for (PuntoCercano punto : listaFiltrar) {
                String clasificador = clasificadorPuntos(punto);
                if (clasificador.compareTo("desconocido") != 0) {
                    listaFiltrada.add(punto);
                }
            }
        }
        return listaFiltrada;
    }

    public List<PuntoCercano> filtradoPuntosCercanos(String tipo) {
        List<PuntoCercano> listaFiltrada = new ArrayList<PuntoCercano>();
        if (listaPuntosCercanosTotal != null && listaPuntosCercanosTotal.size() > 0) {
            for (PuntoCercano punto : listaPuntosCercanosTotal) {
                for (String tipos : punto.getTipos()) {
                    if (tipos.compareTo(tipo) == 0) {
                        listaFiltrada.add(punto);
                    }
                }
            }
        }
        return listaFiltrada;
    }

    /**
     * tres tipos posibles: Maxicarga, Oficina, Envios Personal
     * @param datoPosicion
     * @return
     */
    private String clasificadorPuntos(PuntoCercano datoPosicion) {

        String clasificador = "desconocido";
        int contadorMaxicarga = 0;
        int contadorOficinas = 0;
        int contadorEnvios = 0;

        for (String tipo : datoPosicion.getTipos()) {
            if (tipo.compareTo("Maxicarga") == 0) {
                contadorMaxicarga++;
            } else if (tipo.compareTo("Oficina") == 0) {
                contadorOficinas++;
            } else if (tipo.compareTo("Envios Personal") == 0) {
                contadorEnvios++;
            }
        }

        int sumatoria = contadorEnvios + contadorMaxicarga + contadorOficinas;
        switch (sumatoria) {

            case 1:
                if (contadorEnvios > 0) {
                    clasificador = "Envios";
                } else if (contadorOficinas > 0) {
                    clasificador = "Oficina";
                } else if (contadorMaxicarga > 0) {
                    clasificador = "Maxicarga";
                }
                break;
            case 2:
                if (contadorEnvios > 0 && contadorMaxicarga > 0) {
                    clasificador = "Envios-Maxicarga";
                } else if (contadorOficinas > 0 && contadorMaxicarga > 0) {
                    clasificador = "Oficina-Maxicarga";
                } else if (contadorEnvios > 0 && contadorOficinas > 0) {
                    clasificador = "Envios-Oficinas";
                }
                break;

            case 3:
                clasificador = "Envios-Oficinas-Maxicarga";
                break;

            default:
                clasificador = "desconocido";
                break;
        }
        return clasificador;
    }

    private LatLng dibujarPuntosMapa(int puntoActual) {
        mMap.clear();
        mMap.setOnMarkerClickListener(this);
        int contador = 0;
        LatLng returnPosition = null;
        idMarkers = new ArrayList<String>();
        for (PuntoCercano datoPosicion : listaPuntosCercanosActual) {

            String stringTipo = clasificadorPuntos(datoPosicion);
            LatLng position = new LatLng(datoPosicion.getLatitud(), datoPosicion.getLongitud());
            //mMarker = mMap.addMarker(new MarkerOptions().position(position).draggable(true));
            //   MarkerOptions markerOptions = new MarkerOptions();

//            markerOptions.draggable(true);
//            markerOptions.position(position);
//            markerOptions.title("Mi marca");

            if (contador == puntoActual) {
                //  markerOptions.snippet("No se que es");
                //  markerOptions.flat(true);
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                returnPosition = position;

                if (stringTipo.compareTo("Maxicarga") == 0) {

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_puntosventa_map)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();

                } else if (stringTipo.compareTo("Oficina") == 0) {

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_sucursales_map)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();
                } else if (stringTipo.compareTo("Envios") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_envios_map)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();
                } else if (stringTipo.compareTo("Envios-Maxicarga") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)

//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_todos_map)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();


                } else if (stringTipo.compareTo("Oficina-Maxicarga") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_todos_map)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();


                } else if (stringTipo.compareTo("Envios-Oficinas") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_todos_map)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();


                } else if (stringTipo.compareTo("Envios-Oficinas-Maxicarga") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_todos_map)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();
                }

//                    Marker marker = mMap.addMarker(new MarkerOptions()
//                            .position(position)
////                            .title(datoPosicion.getDescripcion())
////                            .snippet(datoPosicion.getDireccion())
//                            .icon(BitmapDescriptorFactory.defaultMarker()));
//                    idMarkers.add(marker.getId());
//                    marker.showInfoWindow();

            } else {

                if (stringTipo.compareTo("Maxicarga") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
                            //                            .title(datoPosicion.getDescripcion())
                            //                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_puntosventa_map_sel)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();
                } else if (stringTipo.compareTo("Oficina") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
                            //                            .title(datoPosicion.getDescripcion())
                            //                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_sucursales_map_sel)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();
                } else if (stringTipo.compareTo("Envios") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_envios_map_sel)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();


                } else if (stringTipo.compareTo("Envios-Maxicarga") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_todos_map_sel)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();


                } else if (stringTipo.compareTo("Oficina-Maxicarga") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_todos_map_sel)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();


                } else if (stringTipo.compareTo("Envios-Oficinas") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_todos_map_sel)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();


                } else if (stringTipo.compareTo("Envios-Oficinas-Maxicarga") == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
//                            .title(datoPosicion.getDescripcion())
//                            .snippet(datoPosicion.getDireccion())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_todos_map_sel)));
                    idMarkers.add(marker.getId());
                    marker.showInfoWindow();


                }

////                markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
//                    Marker marker =mMap.addMarker(new MarkerOptions()
//                            .position(position)
////                            .title(datoPosicion.getDescripcion())
//
//                            .icon(BitmapDescriptorFactory.defaultMarker()));
//                    idMarkers.add(marker.getId());
//                    marker.showInfoWindow();

            }
//            mMap.addMarker(markerOptions);
            contador++;
        }

//        String primerString = listaPosiciones.get(0);
//        String[] spliStringPosicion = primerString.split("\\|");
//        LatLng position = new LatLng(Double.parseDouble(spliStringPosicion[0]), Double.parseDouble(spliStringPosicion[1]));
        return returnPosition;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
            args.putString(DummySectionFragment.ARG_SECTION_DATA_TIT, listaPuntosCercanosActual.get(position).getRazonSocial());

            args.putString(DummySectionFragment.ARG_SECTION_DATA_DESC, "");
            args.putString(DummySectionFragment.ARG_SECTION_DATA_PUNTO, listaPuntosCercanosActual.get(position).getCallePrincipal());
            args.putString(DummySectionFragment.ARG_SECTION_DATA_TIPO, clasificadorPuntos(listaPuntosCercanosActual.get(position)));
            fragment.setArguments(args);
            return fragment;
        }

        //para poner la cantidad de paginas..
        @Override
        public int getCount() {
            // Show 3 total pages.
            int tamanio = 0;
            if(listaPuntosCercanosActual != null){
                tamanio = listaPuntosCercanosActual.size();
            }
            return tamanio;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            if (position <= getCount()) {
//                getActivity().getSupportFragmentManager()
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commit();
            }
        }
        @Override
        public int getItemPosition(Object object){
            return FragmentPagerAdapter.POSITION_NONE;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";
        public static final String ARG_SECTION_DATA_DESC = "section_data_desc";
        public static final String ARG_SECTION_DATA_TIT = "section_data_tit";
        public static final String ARG_SECTION_DATA_PUNTO = "section_data_punto";
        public static final String ARG_SECTION_DATA_TIPO = "section_data_tipo";

        public static DummySectionFragment newInstance() {
            return new DummySectionFragment();
        }

        //lugar en donde se maneja la lista inferior
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_swipe_mapa, container, false);
            TextView titulo = (TextView) rootView.findViewById(R.id.titulo_map);
            TextView direccionMapa = (TextView) rootView.findViewById(R.id.direccion_mapa);
            TextView tipoPunto1 = (TextView) rootView.findViewById(R.id.tipo_punto1);
            TextView tipoPunto2 = (TextView) rootView.findViewById(R.id.tipo_punto2);
            TextView tipoPunto3 = (TextView) rootView.findViewById(R.id.tipo_punto3);

            ImageView imagenTipoPunto1 = (ImageView) rootView.findViewById(R.id.imagenPunto1);
            ImageView imagenTipoPunto2 = (ImageView) rootView.findViewById(R.id.imagenPunto2);
            ImageView imagenTipoPunto3 = (ImageView) rootView.findViewById(R.id.imagenPunto3);

            int posicion = getArguments().getInt(ARG_SECTION_NUMBER);

            SpannableString content = new SpannableString(getArguments().getString(ARG_SECTION_DATA_TIT));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            titulo.setText(content);
            titulo.setTypeface(tf);
            direccionMapa.setText(getArguments().getString(ARG_SECTION_DATA_PUNTO));
            direccionMapa.setTypeface(tf);
//            punto.setText(getArguments().getString(ARG_SECTION_DATA_PUNTO));

            String tipoPunto = getArguments().getString(ARG_SECTION_DATA_TIPO);
            //desconocido
            //Envios-Oficinas-Maxicarga
            //Envios-Oficinas
            //Oficina-Maxicarga
            //Envios-Maxicarga
            //Envios
            //Oficina
            //Maxicarga
            if(tipoPunto.compareTo("Envios-Oficinas-Maxicarga")==0){
//                imagenPunto.setImageResource(R.drawable.ic_multiple_mapa_transparente);
                tipoPunto1.setText("Envios");
                tipoPunto1.setTypeface(tf);
                tipoPunto2.setText("Oficinas");
                tipoPunto2.setTypeface(tf);
                tipoPunto3.setText("Maxicarga");
                tipoPunto3.setTypeface(tf);
                imagenTipoPunto1.setImageResource(R.drawable.ic_envios_map_transparente);
                imagenTipoPunto2.setImageResource(R.drawable.ic_sucursales_map_transparente);
                imagenTipoPunto3.setImageResource(R.drawable.ic_puntosventa_map_transparente);

            }
            else if(tipoPunto.compareTo("Envios-Oficinas")==0){
//                imagenPunto.setImageResource(R.drawable.ic_oficina_envios_mapa_transparente);
                tipoPunto1.setText("Envios");
                tipoPunto1.setTypeface(tf);
                tipoPunto2.setText("Oficinas");
                tipoPunto2.setTypeface(tf);
//                tipoPunto3.setText("Maxicarga");
                tipoPunto3.setVisibility(View.GONE);
                imagenTipoPunto1.setImageResource(R.drawable.ic_envios_map_transparente);
                imagenTipoPunto2.setImageResource(R.drawable.ic_sucursales_map_transparente);
//                imagenTipoPunto3.setImageResource(R.drawable.ic_puntosventa_map);
                imagenTipoPunto3.setVisibility(View.GONE);
            }
            else if(tipoPunto.compareTo("Oficina-Maxicarga")==0){
//                imagenPunto.setImageResource(R.drawable.ic_maxicarga_oficina_mapa_transparente);
                tipoPunto1.setText("Oficina");
                tipoPunto1.setTypeface(tf);
                tipoPunto2.setText("Maxicarga");
                tipoPunto2.setTypeface(tf);
//                tipoPunto3.setText("Maxicarga");
                tipoPunto3.setVisibility(View.GONE);
                imagenTipoPunto1.setImageResource(R.drawable.ic_sucursales_map_transparente);
                imagenTipoPunto2.setImageResource(R.drawable.ic_puntosventa_map_transparente);
//                imagenTipoPunto3.setImageResource(R.drawable.ic_puntosventa_map);
                imagenTipoPunto3.setVisibility(View.GONE);
            }
            else if(tipoPunto.compareTo("Envios-Maxicarga")==0){
//                imagenPunto.setImageResource(R.drawable.ic_maxicarga_envios_mapa_transparente);
                tipoPunto1.setText("Envios");
                tipoPunto1.setTypeface(tf);
                tipoPunto2.setText("Maxicarga");
                tipoPunto2.setTypeface(tf);
//                tipoPunto3.setText("Maxicarga");
                tipoPunto3.setVisibility(View.GONE);
                imagenTipoPunto1.setImageResource(R.drawable.ic_envios_map_transparente);
                imagenTipoPunto2.setImageResource(R.drawable.ic_puntosventa_map_transparente);
//                imagenTipoPunto3.setImageResource(R.drawable.ic_puntosventa_map);
                imagenTipoPunto3.setVisibility(View.GONE);
            }
            else if(tipoPunto.compareTo("Envios")==0){
//                imagenPunto.setImageResource(R.drawable.ic_envios_map_transparente);
                tipoPunto1.setText("Envios");
                tipoPunto1.setTypeface(tf);
//                tipoPunto2.setText("Oficinas");
//                tipoPunto3.setText("Maxicarga");
                tipoPunto2.setVisibility(View.GONE);
                tipoPunto3.setVisibility(View.GONE);
                imagenTipoPunto1.setImageResource(R.drawable.ic_envios_map_transparente);
//                imagenTipoPunto2.setImageResource(R.drawable.ic_sucursales_map);
//                imagenTipoPunto3.setImageResource(R.drawable.ic_puntosventa_map);
                imagenTipoPunto2.setVisibility(View.GONE);
                imagenTipoPunto3.setVisibility(View.GONE);
            }
            else if(tipoPunto.compareTo("Oficina")==0){
//                imagenPunto.setImageResource(R.drawable.ic_sucursales_map_transparente);
                tipoPunto1.setText("Oficina");
                tipoPunto1.setTypeface(tf);
//                tipoPunto2.setText("Oficinas");
//                tipoPunto3.setText("Maxicarga");
                tipoPunto2.setVisibility(View.GONE);
                tipoPunto3.setVisibility(View.GONE);
                imagenTipoPunto1.setImageResource(R.drawable.ic_sucursales_map_transparente);
//                imagenTipoPunto2.setImageResource(R.drawable.ic_sucursales_map);
//                imagenTipoPunto3.setImageResource(R.drawable.ic_puntosventa_map);
                imagenTipoPunto2.setVisibility(View.GONE);
                imagenTipoPunto3.setVisibility(View.GONE);
            }
            else if(tipoPunto.compareTo("Maxicarga")==0){
//                imagenPunto.setImageResource(R.drawable.ic_puntosventa_map_transparente);
                tipoPunto1.setText("Maxicarga");
                tipoPunto1.setTypeface(tf);
//                tipoPunto2.setText("Oficinas");
//                tipoPunto3.setText("Maxicarga");
                tipoPunto2.setVisibility(View.GONE);
                tipoPunto3.setVisibility(View.GONE);
                imagenTipoPunto1.setImageResource(R.drawable.ic_puntosventa_map_transparente);
//                imagenTipoPunto2.setImageResource(R.drawable.ic_sucursales_map);
//                imagenTipoPunto3.setImageResource(R.drawable.ic_puntosventa_map);
                imagenTipoPunto2.setVisibility(View.GONE);
                imagenTipoPunto3.setVisibility(View.GONE);
            }

            return rootView;
        }
    }

}


