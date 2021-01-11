package py.com.personal.mimundo.fragments.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import py.com.personal.mimundo.activities.AtencionPersonalActivity;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.BilleteraPersonalActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.DashboardAdapter;
import py.com.personal.mimundo.disenhos.Constantes;
import py.com.personal.mimundo.disenhos.ExpandableHeightGridView;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.disenhos.Roles;
import py.com.personal.mimundo.fragments.AtencionPersonalFragment;
import py.com.personal.mimundo.fragments.BilleteraPersonalFragment;
import py.com.personal.mimundo.fragments.ClubPersonalFragment;
import py.com.personal.mimundo.fragments.MapaFragment;
//import py.com.personal.mimundo.fragments.configuracion.DestinosGratuitosFragment;
//import py.com.personal.mimundo.fragments.configuracion.NumerosGratisFragment;
import py.com.personal.mimundo.fragments.configuracion.NumerosGratisFragment;
import py.com.personal.mimundo.fragments.gestion.facturacion.FacturasFragment;
import py.com.personal.mimundo.fragments.gestion.facturacion.ListaGruposFacturacionFragment;
import py.com.personal.mimundo.fragments.saldo.PinSesionFragment;
import py.com.personal.mimundo.fragments.saldo.transferencias.TransferenciaCorporativaPaso1;
import py.com.personal.mimundo.fragments.saldo.transferencias.TransferenciaSaldoIndividualFragment;
import py.com.personal.mimundo.fragments.saldo.packs.ListaPacksFragment;
import py.com.personal.mimundo.fragments.saldo.recarga.RecargaContraFacturaFragment;

import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.DashboardAdapter;
import py.com.personal.mimundo.disenhos.Constantes;
import py.com.personal.mimundo.disenhos.ExpandableHeightGridView;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.disenhos.Roles;
import py.com.personal.mimundo.models.NavDrawerItem;
import py.com.personal.mimundo.services.cache.lineas.HistorialConsumoLinea;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineaRequest;
import py.com.personal.mimundo.services.cache.lineas.ObtenerSaldosLineaRequest;
import py.com.personal.mimundo.services.lineas.consumo.DatosHistorialConsumo;
import py.com.personal.mimundo.services.lineas.consumo.HistorialConsumo;
import py.com.personal.mimundo.services.lineas.consumo.HistorialesConsumo;
import py.com.personal.mimundo.services.lineas.models.DetalleSaldo;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.models.SaldoPack;
import py.com.personal.mimundo.services.lineas.models.SaldoPcrf;
import py.com.personal.mimundo.services.lineas.models.Saldos;
import py.com.personal.mimundo.utils.NumbersUtils;
import py.com.personal.mimundo.utils.PreferenceUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 17/08/2015.
 */
public class DashboardFragment extends Fragment {

    private static final String MENU_GESTION_TITLE = "Gestión";
    private static final String MENU_SALDO_TITLE = "Saldos";
    private static final String MENU_BILLETERA_TITLE = "Billetera Personal";
    private static final String MENU_CLUB_PERSONAL_TITLE = "Club Personal";
    private static final String MENU_CONFIGURACION = "Configuración";
    private static final String MENU_PUNTOS_CERCANOS = "Puntos Cercanos";

    private static final String SUB_MENU_PACKS_TITLE = "Packs";
    private static final String SUB_MENU_TRANSFERENCIA_TITLE = "Transferencia de saldo";
    private static final String SUB_MENU_RECARGA_TITLE = "Recarga contra Factura";
    private static final String SUB_MENU_FACTURACION_TITLE = "Facturación";

    private static final String SALDO_NULL = "0";
    private BaseDrawerActivity activity;

    private DashboardAdapter mAdapter;
    private ArrayList<String> listaTitulos;
    private ArrayList<Integer> listaImagenes;
    private ExpandableHeightGridView gridView;

    private String linea;
    private Linea datosLineaSeleccionada;
    private boolean esLineaPospago = false;
    private boolean realizoPeticionDeSaldo = false;
    private boolean realizoPeticionDeConsumo = false;
    private ObtenerSaldosLineaRequest saldosRequest;
    private ObtenerLineaRequest lineaRequest;
    private HistorialConsumoLinea historialConsumo;
    private PreferenceUtils preferenceUtils;

    TextView nombreUsuario;
    TextView saldoGuaraniesView;
    TextView saldoPromocionalView;
    TextView saldoMinutosView;
    TextView saldoDatosView;
    TextView saldoMensajesView;
    TextView saldoPacksView;

    TextView saldoActualView;
    TextView saldoExcedenteView;
    TextView saldoDisponibleView;

    TextView consumollamadasEntranteView;
    TextView consumollamadasSalienteView;
    TextView consumoMensajesEntranteView;
    TextView consumoMensajesSalienteView;
    TextView consumoInternetEntranteView;
    TextView consumoInternetSalienteView;

    LinearLayout saldoLineaPrepago;
    LinearLayout saldoLineaPospago;
    LinearLayout datosUsuarioPrepago;
    LinearLayout datosUsuarioPospago;
    LinearLayout saldosLinea;
    LinearLayout resumenConsumo;
    RelativeLayout tipoLineaContentView;
    TextView tipoLineaView;

    private String tipoPersona;
    private String tipoUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_dashboard, container, false);
        activity = (BaseDrawerActivity) getActivity();

        listaTitulos = new ArrayList<String>();
        listaTitulos.add("packs");
        listaTitulos.add("transferencias");
        listaTitulos.add("recarga");
        listaTitulos.add("billetera");
        listaTitulos.add("club");
        listaTitulos.add("nros gratis");
        listaTitulos.add("factura");
        listaTitulos.add("Puntos Cercanos");
        listaTitulos.add("contacto");

        tipoPersona = activity.obtenerTipoPersona();
        tipoUsuario = activity.obtenerTipoUsuario();

        listaImagenes = new ArrayList<Integer>();
        listaImagenes.add(R.drawable.ic_dashboard_packs);
        listaImagenes.add(R.drawable.ic_dashboard_transferencia);
        listaImagenes.add(R.drawable.ic_dashboard_recarga);
        listaImagenes.add(R.drawable.ic_dashboard_billetera);
        listaImagenes.add(R.drawable.ic_dashboard_club);
        listaImagenes.add(R.drawable.ic_dashboard_planes);
        listaImagenes.add(R.drawable.ic_dashboard_factura);
        listaImagenes.add(R.drawable.ic_dashboard_localizacion);
        listaImagenes.add(R.drawable.ic_dashboard_contacto);

        mAdapter = new DashboardAdapter(getActivity(), listaTitulos, listaImagenes);
        gridView = (ExpandableHeightGridView) v.findViewById(R.id.gridView1);
        gridView.setExpanded(true);
        gridView.setFocusable(false);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                Fragment fragment = null;
                String tipoUsuario = pref.getString("TipoUsuario", "");
                BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
                switch (position) {
                    case 0 :
                        if (activity.obtenerRolesUsuario().contains(Roles.COMPRA_PACKS)) {
                            if (pref.contains("pinSesion") && pref.contains("TipoUsuario")) {
                                if ((tipoUsuario.compareTo(preferenceUtils.TIPO_USUARIO_NIVEL_1) != 0 &&
                                        tipoUsuario.compareTo(preferenceUtils.TIPO2_USUARIO_NIVEL_1) != 0)
                                        || pref.getBoolean("pinSesion", false)) {
                                    fragment = new ListaPacksFragment();
                                    activity.fragmentActual = "ListaPacksFragment";
                                    marcarSubMenuLateral(1, SUB_MENU_PACKS_TITLE, MENU_SALDO_TITLE);
                                } else {
                                    if (!pref.getBoolean("pinSesion", false)) { //si no está validado el pin
                                        String nroLinea = activity.obtenerLineaSeleccionada();
                                        fragment = new PinSesionFragment().newInstance(position, nroLinea, 1);
                                        activity.fragmentActual = "PinSesionFragment";
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.error_acceso), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1 :
                        if (activity.obtenerRolesUsuario().contains(Roles.TRANSFERENCIA_SALDO)) {
                            if (pref.contains("pinSesion") && pref.contains("TipoUsuario")) {
                                if ((tipoUsuario.compareTo(preferenceUtils.TIPO_USUARIO_NIVEL_1) != 0 &&
                                        tipoUsuario.compareTo(preferenceUtils.TIPO2_USUARIO_NIVEL_1) != 0)
                                        || pref.getBoolean("pinSesion", false)) {
                                    if (tipoPersona != null && tipoPersona.equals(Constantes.TIPO_PERSONA_FISICA)) {
                                        fragment = TransferenciaSaldoIndividualFragment.newInstance();
                                        activity.fragmentActual = "TransferenciaSaldoIndividualFragment";
                                    } else if (activity.obtenerTipoPersona().equals(Constantes.TIPO_PERSONA_JURIDICA)) {
                                        fragment = TransferenciaCorporativaPaso1.newInstance();
                                        activity.fragmentActual = "TansferenciaSaldoCorporativa";
                                    }
                                    marcarSubMenuLateral(1, SUB_MENU_TRANSFERENCIA_TITLE, MENU_SALDO_TITLE);
                                } else {
                                    if (!pref.getBoolean("pinSesion", false)) { //si no está validado el pin
                                        String nroLinea = activity.obtenerLineaSeleccionada();
                                        fragment = new PinSesionFragment().newInstance(position, nroLinea, 1);
                                        activity.fragmentActual = "PinSesionFragment";
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.error_acceso), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2 :
                        if (activity.obtenerRolesUsuario().contains(Roles.RECARGA_CONTRA_FACTURA)) {
                            if (pref.contains("pinSesion") && pref.contains("TipoUsuario")) {
                                if ((tipoUsuario.compareTo(preferenceUtils.TIPO_USUARIO_NIVEL_1) != 0 &&
                                        tipoUsuario.compareTo(preferenceUtils.TIPO2_USUARIO_NIVEL_1) != 0)
                                        || pref.getBoolean("pinSesion", false)) {
                                    fragment = new RecargaContraFacturaFragment();
                                    activity.fragmentActual = "RecargaContraFacturaFragment";
                                    marcarSubMenuLateral(1, SUB_MENU_RECARGA_TITLE, MENU_SALDO_TITLE);
                                } else {
                                    if (!pref.getBoolean("pinSesion", false)) { //si no está validado el pin
                                        String nroLinea = activity.obtenerLineaSeleccionada();
                                        fragment = new PinSesionFragment().newInstance(position, nroLinea, 1);
                                        activity.fragmentActual = "PinSesionFragment";
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.error_acceso), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3 :
                        fragment = new BilleteraPersonalFragment();
                        activity.fragmentActual = "BilleteraPersonalFragment";
                        marcarMenuLateral(MENU_BILLETERA_TITLE);
                        break;
                    case 4 :
                        boolean tieneRolClubPersonal = activity.obtenerRolesUsuario().contains(Roles.CLUB_PERSONAL);
                        if (tipoPersona.equals(Constantes.TIPO_PERSONA_FISICA) && tieneRolClubPersonal) {
                            fragment = new ClubPersonalFragment();
                            activity.fragmentActual = "ClubPersonalFragment";
                            marcarMenuLateral(MENU_CLUB_PERSONAL_TITLE);
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.error_acceso_club_personal), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 5 :
                        if (activity.obtenerRolesUsuario().contains(Roles.DESTINOS_GRATUITOS)) {
                            fragment = NumerosGratisFragment.newInstance();
                            activity.fragmentActual = "NumerosGratisFragment";
                            marcarMenuLateral(MENU_CONFIGURACION);
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.error_acceso), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 6 :
                        if (tipoPersona.equals("PERJUR") && (tipoUsuario.equals("TMP") || tipoUsuario.equals("RES"))) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.error_acceso), Toast.LENGTH_SHORT).show();
                        } else {
                            if (activity.obtenerRolesUsuario().contains(Roles.FACTURACION)) {
                                fragment = new FacturasFragment();
                                activity.fragmentActual = "FacturasFragment";
                                if (activity.getNavMenuTitles().contains(MENU_GESTION_TITLE)) {
                                    marcarSubMenuLateral(2, SUB_MENU_FACTURACION_TITLE, MENU_GESTION_TITLE);
                                } else {
                                    marcarSubMenuLateral(1, SUB_MENU_FACTURACION_TITLE, MENU_GESTION_TITLE);
                                }
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.error_acceso), Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case 7 :
                        fragment = new MapaFragment();
                        activity.fragmentActual = "MapaFragment";
                        marcarMenuLateral(MENU_PUNTOS_CERCANOS);
                        break;
                    case 8 :
                        fragment = new AtencionPersonalFragment();
                        activity.fragmentActual = "AtencionPersonalFragment";
                        break;
                }
                if (fragment != null) {
                    activity.lineaSeleccionada = false;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });

        BaseDrawerActivity activity = ((BaseDrawerActivity) getActivity());
        linea = activity.obtenerLineaSeleccionada();
        datosLineaSeleccionada = activity.obtenerDatosLineaSeleccionada();

        saldoLineaPrepago = (LinearLayout) v.findViewById(R.id.saldo_linea_prepago);
        saldoLineaPospago = (LinearLayout) v.findViewById(R.id.saldo_linea_pospago);
        saldosLinea = (LinearLayout) v.findViewById(R.id.saldos_linea);
        resumenConsumo = (LinearLayout) v.findViewById(R.id.resumen_consumo);
        tipoLineaContentView = (RelativeLayout) v.findViewById(R.id.TipoLineaView);
        tipoLineaView = (TextView) v.findViewById(R.id.TipoLinea);
        datosUsuarioPrepago = (LinearLayout) v.findViewById(R.id.datos_usuario_prepago);
        //TODO datosUsuarioPospago = (LinearLayout) v.findViewById(R.id.datos_usuario_pospago);
        if (datosLineaSeleccionada.getClaseComercial() != null && datosLineaSeleccionada.getClaseComercial().equals("POSPA")) {
            saldoLineaPospago.setVisibility(View.VISIBLE);
            //datosUsuarioPospago.setVisibility(View.VISIBLE);
            nombreUsuario = (TextView) v.findViewById(R.id.nombre_usuario_prepago);
            esLineaPospago = true;
        } else {
            saldoLineaPrepago.setVisibility(View.VISIBLE);
            datosUsuarioPrepago.setVisibility(View.VISIBLE);
            nombreUsuario = (TextView) v.findViewById(R.id.nombre_usuario_prepago);
        }

        System.err.println("Nombre usuario : " + activity.obtenerNombreUsuario());
        nombreUsuario.setText(activity.obtenerNombreUsuario());
        saldoGuaraniesView = (TextView) v.findViewById(R.id.saldo_guaranies);
        saldoGuaraniesView.setText("?");
        saldoPromocionalView = (TextView) v.findViewById(R.id.saldo_promocional);
        saldoPromocionalView.setText("?");
        saldoMinutosView = (TextView) v.findViewById(R.id.saldo_minutos);
        saldoDatosView = (TextView) v.findViewById(R.id.saldo_datos);
        saldoMensajesView = (TextView) v.findViewById(R.id.saldo_mensajes);
        saldoPacksView = (TextView) v.findViewById(R.id.saldo_packs);

        saldoActualView = (TextView) v.findViewById(R.id.saldo_actual);
        saldoExcedenteView = (TextView) v.findViewById(R.id.saldo_excedente);
        saldoDisponibleView = (TextView) v.findViewById(R.id.saldo_disponible);

        consumollamadasEntranteView = (TextView) v.findViewById(R.id.consumo_llamadas_entrante);
        consumollamadasSalienteView = (TextView) v.findViewById(R.id.consumo_llamadas_saliente);
        consumoMensajesEntranteView = (TextView) v.findViewById(R.id.consumo_mensajes_entrante);
        consumoMensajesSalienteView = (TextView) v.findViewById(R.id.consumo_mensajes_saliente);
        consumoInternetEntranteView = (TextView) v.findViewById(R.id.consumo_datos_entrante);
        consumoInternetSalienteView = (TextView) v.findViewById(R.id.consumo_datos_saliente);

        consumollamadasEntranteView.setText("?");
        consumollamadasSalienteView.setText("?");
        consumoMensajesEntranteView.setText("?");
        consumoMensajesSalienteView.setText("?");
        consumoInternetEntranteView.setText("?");
        consumoInternetSalienteView.setText("?");

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        nombreUsuario.setTypeface(tf);
        saldoGuaraniesView.setTypeface(tf);
        saldoPromocionalView.setTypeface(tf);
        saldoMinutosView.setTypeface(tf);
        saldoDatosView.setTypeface(tf);
        saldoMensajesView.setTypeface(tf);
        saldoPacksView.setTypeface(tf);
        saldoActualView.setTypeface(tf);
        saldoExcedenteView.setTypeface(tf);
        saldoDisponibleView.setTypeface(tf);
        consumollamadasEntranteView.setTypeface(tf);
        consumollamadasSalienteView.setTypeface(tf);
        consumoMensajesEntranteView.setTypeface(tf);
        consumoMensajesSalienteView.setTypeface(tf);
        consumoInternetEntranteView.setTypeface(tf);
        consumoInternetSalienteView.setTypeface(tf);
        ((TextView) v.findViewById(R.id.hola_view_prepago)).setTypeface(tf);
        //((TextView) v.findViewById(R.id.hola_view_pospago)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_actual_1)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_actual_2)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_actual_3)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.gs_view)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.minuto_view)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.gigas_view)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.mensajes_view)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.packs_view)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_actual_title)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_disponible_title)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_excedente_title)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_actual_gs)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_excedente_gs)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_disponible_gs)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.saldo_promocional_1)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.promocional_gs_view)).setTypeface(tf);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        saldosRequest = new ObtenerSaldosLineaRequest(getActivity(), new JacksonConverter(objectMapper));
        lineaRequest = new ObtenerLineaRequest(getActivity(), new JacksonConverter(objectMapper));
        historialConsumo = new HistorialConsumoLinea(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!realizoPeticionDeSaldo) {
            activity.getSpiceManager().execute(lineaRequest.datosLinea(linea), new LineaRequestListener());
            //activity.getSpiceManager().execute(saldosRequest.saldosLinea(linea), new SaldosLineaRequestListener());
        }
        if (!realizoPeticionDeConsumo) {
            activity.getSpiceManager().execute(historialConsumo.obtenerHistorial(linea), new HistorialConsumoListener());
        }
    }

    public final class LineaRequestListener implements RequestListener<Linea> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), MensajesDeUsuario.ERROR_OBTENER_LINEAS_USUARIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Linea datos) {
            if (datos != null) {

                datosLineaSeleccionada = datos;
                String claseComercial = datos.getClaseComercial();
                String codigoTecnologia = datos.getCodigoTecnologia();
                String tipoLinea = datos.getTipoLinea();
                if (tipoLinea == null) tipoLinea = "";

                if (claseComercial != null && claseComercial.equals("POSPA")) {
                    esLineaPospago = true;
                } else {
                    esLineaPospago = false;
                }

                if (codigoTecnologia.equals("DTH") || codigoTecnologia.equals("WIMAX")
                        || tipoLinea.equals(Constantes.TIPO_LINEA_INTERNET)
                        ||tipoLinea.equals(Constantes.TIPO_LINEA_TV)) {

                    saldoLineaPrepago.setVisibility(View.GONE);
                    saldoLineaPospago.setVisibility(View.GONE);
                    saldosLinea.setVisibility(View.GONE);
                    resumenConsumo.setVisibility(View.GONE);
                    tipoLineaContentView.setVisibility(View.VISIBLE);
                    tipoLineaView.setVisibility(View.VISIBLE);

                    if (codigoTecnologia.equals("DTH") ||tipoLinea.equals(Constantes.TIPO_LINEA_TV))
                        tipoLineaView.setText("Personal TV");
                    else
                        tipoLineaView.setText("Internet");

                } else {
                    tipoLineaContentView.setVisibility(View.GONE);
                    tipoLineaView.setVisibility(View.GONE);
                    activity.getSpiceManager().execute(saldosRequest.saldosLinea(linea), new SaldosLineaRequestListener());
                }
            }
        }
    }

    public final class SaldosLineaRequestListener implements RequestListener<Saldos> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.sin_datos_saldo), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Saldos datos) {
            Activity context = getActivity();
            if (context != null && datos != null) {
                cargarDatosSaldo(datos);
                realizoPeticionDeSaldo = true;
                if (datosLineaSeleccionada.getCodigoTecnologia() != null
                        && datosLineaSeleccionada.getTipoLinea().equals("BAM_3G")) {
                    ((BaseDrawerActivity) getActivity()).getSpiceManager().execute(
                            saldosRequest.saldoPcrf(linea),
                            new SaldoPcrfLineaRequestListener()
                    );
                }
            }
        }
    }

    public final class SaldoPcrfLineaRequestListener implements RequestListener<SaldoPcrf> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.sin_datos_saldo), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final SaldoPcrf datos) {
            Activity context = getActivity();
            if (context != null && datos != null) {
                saldoDatosView.setText(obtenerSaldoPcrf(datos));
            }
        }
    }

    private String obtenerSaldoPcrf(SaldoPcrf datos) {
        if (datos != null) {
            if (datos.getPlan() != null) {
                try {
                    Double monto = new Double(datos.getPlan().getCantidadDisponibleBytes());
                    if (monto < 1073741824) {
                        monto = monto / (1024 * 1024);
                        ((TextView) getView().findViewById(R.id.gigas_view)).setText("megas");
                    } else {
                        ((TextView) getView().findViewById(R.id.gigas_view)).setText("gigas");
                        monto = monto / (1024 * 1024 * 1024);
                    }
                    String montoString = NumbersUtils.round(monto, 2) + "";
                    return montoString.replace(".", ",");
                } catch (NumberFormatException e) {
                    return SALDO_NULL;
                }
            } else {
                return SALDO_NULL;
            }
        } else {
            return SALDO_NULL;
        }
    }

    private void cargarDatosSaldo(Saldos saldos) {

        List<DetalleSaldo> moneda = saldos.getMoneda();
        List<DetalleSaldo> minutos = saldos.getMinutos();
        List<DetalleSaldo> datos = saldos.getDatos();
        List<DetalleSaldo> mensajes = saldos.getMensajes();
        List<DetalleSaldo> pospago = saldos.getPospago();
        List<SaldoPack> packs = saldos.getPacks();

        if (esLineaPospago) {
            saldoLineaPospago.setVisibility(View.VISIBLE);
            //datosUsuarioPospago.setVisibility(View.VISIBLE);
            saldoLineaPrepago.setVisibility(View.GONE);
            //datosUsuarioPrepago.setVisibility(View.GONE);
            saldoActualView.setText(obtenerSaldoMonedaPospago(moneda, pospago));
            saldoExcedenteView.setText(obtenerSaldoPospago(pospago, Saldos.SALDO_PROMOCIONAL));
            saldoDisponibleView.setText(obtenerSaldoPospago(pospago, Saldos.SALDO_DISPONIBLE));
        } else {
            saldoLineaPrepago.setVisibility(View.VISIBLE);
            //datosUsuarioPrepago.setVisibility(View.VISIBLE);
            saldoLineaPospago.setVisibility(View.GONE);
            //datosUsuarioPospago.setVisibility(View.GONE);
            saldoGuaraniesView.setText(obtenerSaldoMoneda(moneda, Saldos.SALDO_NORMAL));
            saldoPromocionalView.setText(obtenerSaldoMoneda(moneda, Saldos.SALDO_PROMOCIONAL));
        }

        saldoMinutosView.setText(obtenerSaldoMinutos(minutos));
        saldoMensajesView.setText(obtenerSaldoMensajes(mensajes));
        saldoDatosView.setText(obtenerSaldoDatos(datos));
        saldoPacksView.setText(obtenerSaldoPacks(packs));
    }

    private String obtenerSaldoMoneda(List<DetalleSaldo> moneda, String tipo) {
        if (moneda != null && moneda.size() > 0) {
            for (DetalleSaldo datosSaldo : moneda) {
                if (datosSaldo.getTipo() != null
                        && datosSaldo.getTipo().equals(tipo)
                        && datosSaldo.getMonto() != null) {
                    return NumbersUtils.formatear(datosSaldo.getMonto());
                }
            }
        }
        return "0";
    }

    private String obtenerSaldoMonedaPospago(List<DetalleSaldo> moneda, List<DetalleSaldo> pospago) {
        if (moneda != null && moneda.size() > 0) {
            DetalleSaldo d = null;
            for (DetalleSaldo detalle : moneda) {
                if (detalle.getTipo().equals(Saldos.SALDO_NORMAL))
                    d = detalle;
            }
            if (d == null) return "0";
            Long montoMoneda = new Long(d.getMonto());
            /*if (pospago != null && pospago.size() > 0) {
                for (DetalleSaldo detallePospago : pospago) {
                    if (detallePospago.getTipo().contains("Disponible")) {
                        Long montoPospago = new Long(detallePospago.getMonto());
                        montoMoneda = montoMoneda - montoPospago;
                    }
                }
            }*/
            return NumbersUtils.formatear(montoMoneda);
        }
        return "0";
    }

    private String obtenerSaldoPospago(List<DetalleSaldo> pospago, String tipo) {
        if (pospago != null && pospago.size() > 0) {
            for (DetalleSaldo detalle : pospago) {
                if (detalle.getTipo() != null && detalle.getTipo().contains(tipo)) {
                    if (detalle.getMonto() != null) {
                        return NumbersUtils.formatear(detalle.getMonto());
                    }
                }
            }
        }
        return SALDO_NULL;
    }

    // TODO sumamos todos los saldos que vengan en el array de minutos
    private String obtenerSaldoMinutos(List<DetalleSaldo> minutos) {
        long montoTotal = 0;
        if (minutos != null && minutos.size() > 0) {
            for (DetalleSaldo datosSaldo : minutos) {
                try {
                    if (datosSaldo.getMonto() != null) {
                        String[] arrayMinutos = datosSaldo.getMonto().split(":");
                        if (arrayMinutos.length > 0) {
                            montoTotal = montoTotal + new Long(arrayMinutos[0]);
                        }
                    }
                } catch (Exception e) {
                    return datosSaldo.getMonto();
                }
            }
            return NumbersUtils.formatear(montoTotal);
        }
        return SALDO_NULL;
    }

    // TODO sumamos todos los saldos que vengan en el array de mensajes
    private String obtenerSaldoMensajes(List<DetalleSaldo> mensajes) {
        long montoTotal = 0;
        if (mensajes != null && mensajes.size() > 0) {
            for (DetalleSaldo datosSaldo : mensajes) {
                try {
                    if (datosSaldo.getMonto() != null) {
                        montoTotal = montoTotal + new Long(datosSaldo.getMonto());
                    }
                } catch (Exception e) {
                    return datosSaldo.getMonto();
                }
            }
            return NumbersUtils.formatear(montoTotal);
        }
        return SALDO_NULL;
    }

    // TODO sumamos todos los saldos que vengan en el array de datos
    private String obtenerSaldoDatos(List<DetalleSaldo> datos) {
        double montoTotal = 0;
        if (datos != null && datos.size() > 0) {
            for (DetalleSaldo datosSaldo : datos) {
                if (datosSaldo.getMonto() != null) {
                    try {
                        montoTotal = montoTotal + new Double(datosSaldo.getMonto());
                    } catch (NumberFormatException e) {
                        return SALDO_NULL;
                    }
                }
            }
            if (montoTotal < 1024) {
                ((TextView) getView().findViewById(R.id.gigas_view)).setText("megas");
            } else {
                ((TextView) getView().findViewById(R.id.gigas_view)).setText("gigas");
                montoTotal = montoTotal / 1000.0;
            }
            String montoString = NumbersUtils.round(montoTotal, 2) + "";
            return montoString.replace(".", ",");
        }
        return SALDO_NULL;
    }


    private String obtenerSaldoPacks(List<SaldoPack> packs) {
        if (packs != null && packs.size() > 0) {
            return "ON";
        } else {
            return "OFF";
        }
    }

    public final class HistorialConsumoListener implements RequestListener<HistorialesConsumo> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.sin_datos_consumo), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final HistorialesConsumo datos) {
            Activity context = getActivity();
            if (context != null) {
                cargarDatosConsumo(datos);
                realizoPeticionDeConsumo = true;
            }
        }
    }

    private void cargarDatosConsumo(HistorialesConsumo datos) {
        if (datos != null) {
            // Consumo de Llamadas
            double entrantesLlamadas = 0;
            double salientesLlamadas = 0;
            if (datos != null) {
                HistorialConsumo llamadas = datos.getLlamadas();
                if (llamadas != null) {
                    for (DatosHistorialConsumo entrantes : llamadas.getEntrantes()) {
                        entrantesLlamadas = entrantesLlamadas + entrantes.getCantidad();
                    }
                    for (DatosHistorialConsumo salientes : llamadas.getSalientes()) {
                        salientesLlamadas = salientesLlamadas + salientes.getCantidad();
                    }
                }
            }
            consumollamadasEntranteView.setText(NumbersUtils.formatear((long)entrantesLlamadas));
            consumollamadasSalienteView.setText(NumbersUtils.formatear((long)salientesLlamadas));

            // Consumo de Mensajes
            double entrantesMensajes = 0;
            double salientesMensajes = 0;
            if (datos != null) {
                HistorialConsumo mensajes = datos.getMensajes();
                if (mensajes != null) {
                    for (DatosHistorialConsumo entrantes : mensajes.getEntrantes()) {
                        entrantesMensajes = entrantesMensajes + entrantes.getCantidad();
                    }
                    for (DatosHistorialConsumo salientes : mensajes.getSalientes()) {
                        salientesMensajes = salientesMensajes + salientes.getCantidad();
                    }
                }
            }
            consumoMensajesEntranteView.setText(NumbersUtils.formatear((long)entrantesMensajes));
            consumoMensajesSalienteView.setText(NumbersUtils.formatear((long)salientesMensajes));

            // Consumo de Datos
            double entrantesDatos = 0;
            double salientesDatos = 0;
            if (datos != null) {
                HistorialConsumo internet = datos.getDatos();
                if (internet != null) {
                    for (DatosHistorialConsumo entrantes : internet.getEntrantes()) {
                        entrantesDatos = entrantesDatos + entrantes.getCantidad();
                    }
                    for (DatosHistorialConsumo salientes : internet.getSalientes()) {
                        salientesDatos = salientesDatos + salientes.getCantidad();
                    }
                }
            }
            long entrantesDatosMB = (long) entrantesDatos / 1024;
            long salientesDatosMB = (long) salientesDatos / 1024;
            consumoInternetEntranteView.setText(NumbersUtils.formatear(entrantesDatosMB));
            consumoInternetSalienteView.setText(NumbersUtils.formatear(salientesDatosMB));
        }
    }

    private void marcarMenuLateral(String item) {
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        List<String> titulosMenu = activity.getNavMenuTitles();
        NavDrawerItem.padreSeleccionado = encontrarItem(item, titulosMenu);
        NavDrawerItem.hijoSeleccionado = -1;
        activity.getmDrawerList().setItemChecked(NavDrawerItem.padreSeleccionado, true);
        activity.getmDrawerList().setSelection(NavDrawerItem.padreSeleccionado);
        activity.getmDrawerList().collapseGroup(1);
        activity.getmDrawerList().collapseGroup(2);
    }

    private void marcarSubMenuLateral(int menu, String item, String seccion) {
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        List<String> titulosMenu = activity.getNavSubMenuSaldoTitles();
        if (seccion.equals(MENU_GESTION_TITLE)) {
            titulosMenu = activity.getNavSubMenuGestionTitles();
        }
        NavDrawerItem.padreSeleccionado = menu;
        NavDrawerItem.hijoSeleccionado = encontrarItem(item, titulosMenu);
        activity.getmDrawerList().setItemChecked(NavDrawerItem.hijoSeleccionado, true);
        activity.getmDrawerList().setSelection(NavDrawerItem.hijoSeleccionado);
        activity.getmDrawerList().expandGroup(menu);
    }

    private int encontrarItem(String item, List<String> titulosMenu) {
        for (int i = 0; i < titulosMenu.size(); i++){
            if (item.equals(titulosMenu.get(i))) {
                return i;
            }
        }
        return 0;
    }
}
