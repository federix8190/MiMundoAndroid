package py.com.personal.mimundo.fragments.gestion.facturacion;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.administracion.DatoPerfil;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.facturacion.LineasGrupoRequest;
import py.com.personal.mimundo.services.cache.facturacion.ResumenGrupoRequest;
import py.com.personal.mimundo.services.grupos.models.GrupoFacturacion;
import py.com.personal.mimundo.services.grupos.models.ResumenGrupo;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.models.ListaLineas;
import py.com.personal.mimundo.utils.ListUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 06/08/2014.
 */
public class InfoGrupoFragment extends Fragment {

    private GrupoFacturacion datosGrupo;

    // Info general.
    private ListView listaInfoGrupoView;
    private InfoGrupoAdapter adapterInfoGrupo;
    private List<DatoPerfil> listaInfoGrupo;

    private ListView listaLineasView;
    private LineasGrupoAdapter adapterLineas;
    private List<Linea> listaLineas;
    private ResumenGrupo resumen;
    private List<Linea> lineasGrupo;
    private LineasGrupoRequest lineasRequest;
    private ResumenGrupoRequest resumenRequest;

    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Boolean marcaLineas;
    private Boolean marcaResumen;

    public static InfoGrupoFragment newInstance(GrupoFacturacion datosGrupo) {
        InfoGrupoFragment f = new InfoGrupoFragment();
        f.datosGrupo = datosGrupo;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            // Datos del grupo de facturacion
            String jsonDatosGrupo = savedInstanceState.getString("datosGrupo");
            datosGrupo = new Gson().fromJson(jsonDatosGrupo, GrupoFacturacion.class);
            System.err.println("Grupo Facturacion : " + datosGrupo.getCodigo() + " - " + datosGrupo.getDescripcion());
            // Lista de lineas del grupo
            String jsonObject = savedInstanceState.getString("listaLineas");
            ListaLineas lista = new Gson().fromJson(jsonObject, ListaLineas.class);
            lineasGrupo = lista.getLista();
            // Resumen del grupo de facturacion
            String jsonResumen = savedInstanceState.getString("resumen");
            resumen = new Gson().fromJson(jsonResumen, ResumenGrupo.class);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListaLineas lista = new ListaLineas();
        lista.setLista(lineasGrupo);
        outState.putString("datosGrupo", new Gson().toJson(datosGrupo));
        outState.putString("listaLineas", new Gson().toJson(lista));
        outState.putString("resumen", new Gson().toJson(resumen));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_info_grupo, container, false);
        listaInfoGrupoView = (ListView) v.findViewById(R.id.listaInfoGrupo);
        cargarDatosGrupo();
        listaLineasView = (ListView) v.findViewById(R.id.listaLineasInfoGrupo);

        progressBar = (ProgressBar)v.findViewById(R.id.progressbar_InfoGrupoFacturacion);
        scrollView = (ScrollView)v.findViewById(R.id.scroll_InfoGrupoFacturacion);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.infoGrupoTitle)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.segundoTitulo)).setTypeface(tf);

        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = false;

        // Peticion de lineas.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineasRequest = new LineasGrupoRequest(getActivity(), new JacksonConverter(objectMapper));
        resumenRequest = new ResumenGrupoRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    public void showProgress(final boolean show) {
        if(marcaResumen && marcaLineas) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            scrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        marcaLineas = true;
        marcaResumen = true;
        showProgress(false);
        marcaLineas = false;
        marcaResumen = false;
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (lineasGrupo == null) {
            setearMensajeLoading();
            String codigoGrupo = datosGrupo.getCodigo();
            activity.getSpiceManager().execute(lineasRequest.lineasGrupo(codigoGrupo), new LineasGruposRequestListener());
        } else {
            cargarLineasGrupo(lineasGrupo);
        }
        if (resumen == null) {
            String codigoGrupo = datosGrupo.getCodigo();
            System.err.println("Resumen Grupo : " + codigoGrupo);
            activity.getSpiceManager().execute(resumenRequest.resumenGrupo(codigoGrupo), new ResumenGruposRequestListener());
        } else {
            cargarDatosDetalle(resumen);
        }
    }

    public final class LineasGruposRequestListener implements RequestListener<List> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            marcaLineas = true;
            Context context = getActivity();
            if(context != null) {
                showProgress(false);
                removerMensajeLoading();
                setearMensajeFalla();
                Toast.makeText(context, getResources().getString(R.string.error_obtener_lineas_grupo),
                        Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onRequestSuccess(final List lineas) {
            Context context = getActivity();
            if(context != null) {
                removerMensajeLoading();
                if (lineas != null) {
                    cargarLineasGrupo(lineas);
                    lineasGrupo = lineas;
                } else {
                    setearMensajeFalla();
                    Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public final class ResumenGruposRequestListener implements RequestListener<ResumenGrupo> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            marcaResumen = true;
            Context context = getActivity();
            if(context != null) {
                showProgress(false);
                Toast.makeText(context, getResources().getString(R.string.error_obtener_detalle_grupo),
                        Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onRequestSuccess(final ResumenGrupo resumen) {
            Context context = getActivity();
            if(context != null) {
                if (resumen != null) {
                    cargarDatosDetalle(resumen);
                } else {
                    String msg = getResources().getString(R.string.info_grupo) + MensajesDeUsuario.MENSAJE_SIN_DATOS;
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cargarLineasGrupo(List<Linea> lineas) {
        marcaLineas = true;
        showProgress(false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (!lineas.isEmpty()) {
            listaLineas = lineas;
            String nro = lineas.get(0).getNumeroLinea();
            if (nro == null) {
                nro = lineas.get(0).getNumeroContrato();
            }
            int posicion = activity.getPosicionLinea(nro);
            System.err.println("Posicion : " + nro + " - " + posicion);
            activity.seleccionarItem(posicion);
        } else {
            Linea linea = new Linea();
            linea.setNumeroLinea(MensajesDeUsuario.TITULO_SIN_DATOS);
            linea.setTipoLinea("");
            linea.setNumeroContrato(MensajesDeUsuario.MENSAJE_SIN_DATOS);
            listaLineas = new ArrayList<Linea>();
            listaLineas.add(linea);
        }

        adapterLineas =  new LineasGrupoAdapter(getActivity(), listaLineas);
        listaLineasView.setAdapter(adapterLineas);
        ListUtils.setListViewHeightBasedOnChildren(listaLineasView, 16);
    }

    private void setearMensajeLoading() {
        listaLineas = new ArrayList<>();
        adapterLineas =  new LineasGrupoAdapter(getActivity(), listaLineas);
        listaLineasView.setAdapter(adapterLineas);
        Linea linea = new Linea();
        linea.setNumeroLinea(MensajesDeUsuario.TITULO_LOADING);
        linea.setTipoLinea("");
        linea.setNumeroContrato(MensajesDeUsuario.MENSAJE_LOADING);
        listaLineas.add(linea);
        adapterLineas.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaLineasView, 0);
    }

    private void removerMensajeLoading() {
        listaLineas.remove(0);
        adapterLineas.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaLineasView, 0);
    }

    private  void setearMensajeFalla() {
        Linea linea = new Linea();
        linea.setNumeroLinea(MensajesDeUsuario.TITULO_SIN_SERVICIO);
        linea.setTipoLinea("");
        linea.setNumeroContrato(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
        listaLineas.add(linea);
        adapterLineas.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaLineasView, 0);
    }

    private void cargarDatosGrupo() {
        // Dia de acreditacion
        String diaAcreditacion = MensajesDeUsuario.TITULO_SIN_DATOS;
        if (datosGrupo.getDiaAcreditacion() != null) {
            diaAcreditacion = String.valueOf(datosGrupo.getDiaAcreditacion());
        }

        // Debito automatico
        Long debito = datosGrupo.getDebito();
        String tieneDebito = (debito == null || debito == 0) ? MensajesDeUsuario.NO : MensajesDeUsuario.SI;

        // Cargamos los datos del grupo
        listaInfoGrupo = new ArrayList<>();
        listaInfoGrupo.add(new DatoPerfil(getResources().getString(R.string.info_grupo_descripcion),
                datosGrupo.getDescripcion(), R.drawable.ic_descrip_fact));
        listaInfoGrupo.add(new DatoPerfil(getResources().getString(R.string.info_grupo_codigo),
                datosGrupo.getCodigo(), R.drawable.ic_key_fact));
        listaInfoGrupo.add(new DatoPerfil(getResources().getString(R.string.info_grupo_estado),
                datosGrupo.getEstado(), R.drawable.ic_estado_fact));
        listaInfoGrupo.add(new DatoPerfil(getResources().getString(R.string.info_grupo_dia_acreditacion),
                diaAcreditacion, R.drawable.ic_acreditacion_fact));
        listaInfoGrupo.add(new DatoPerfil(getResources().getString(R.string.info_grupo_debito),
                tieneDebito, R.drawable.ic_debito_fact));
        listaInfoGrupo.add(new DatoPerfil(getResources().getString(R.string.info_grupo_lineas_telefonia),
                "?", R.drawable.ic_lineastel_fact));
        listaInfoGrupo.add(new DatoPerfil(getResources().getString(R.string.info_grupo_lineas_internet),
                "?", R.drawable.ic_mundo_fact));

        // Cargamos el adapter
        adapterInfoGrupo =  new InfoGrupoAdapter(getActivity(), listaInfoGrupo);
        listaInfoGrupoView.setAdapter(adapterInfoGrupo);
        adapterInfoGrupo.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaInfoGrupoView, 0);
    }

    public void cargarDatosDetalle(ResumenGrupo resumen) {
        marcaResumen= true;
        showProgress(false);
        String cantidadLineasTelefonia = String.valueOf(resumen.getCantidadLineasTelefonia());
        String cantidadLineasInternet = String.valueOf(resumen.getCantidadLineasInternet());
        listaInfoGrupo.remove(listaInfoGrupo.size() - 1);
        listaInfoGrupo.remove(listaInfoGrupo.size() - 1);
        listaInfoGrupo.add(new DatoPerfil(getResources().getString(R.string.info_grupo_lineas_telefonia),
                cantidadLineasTelefonia, R.drawable.ic_grupo_telefonia));
        listaInfoGrupo.add(new DatoPerfil(getResources().getString(R.string.info_grupo_lineas_internet),
                cantidadLineasInternet, R.drawable.ic_planes_internet));
        adapterInfoGrupo.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaInfoGrupoView, 0);
    }
}
