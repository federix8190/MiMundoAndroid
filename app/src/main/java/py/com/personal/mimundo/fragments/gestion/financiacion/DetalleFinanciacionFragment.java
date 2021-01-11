package py.com.personal.mimundo.fragments.gestion.financiacion;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.administracion.DatoPerfil;
import py.com.personal.mimundo.disenhos.Formateador;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.financiaciones.CuotasFinanciacionRequest;
import py.com.personal.mimundo.services.financiaciones.models.Cuota;
import py.com.personal.mimundo.services.financiaciones.models.Financiacion;
import py.com.personal.mimundo.services.financiaciones.models.ListaCuotas;
import py.com.personal.mimundo.utils.ListUtils;
import py.com.personal.mimundo.utils.NumbersUtils;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class DetalleFinanciacionFragment extends Fragment {

    private Financiacion datosFinanciacion;

    // Cabecera.
    private CabeceraDetalleFinanciacionAdapter cabeceraAdapter;
    private ListView cabeceraView ;
    private ArrayList<DatoPerfil> listaItems;

    // Lista cuotas.
    private ListaCuotaDetalleFinanciacionAdapter cuotaAdapter;
    private ListView listaCuotasView;
    private List<ItemDeListaCuota> listaCuotas;
    private List<Cuota> cuotas;

    private CuotasFinanciacionRequest cuotasFinanciacionRequest;

    public static DetalleFinanciacionFragment newInstance(Financiacion financiacion) {
        DetalleFinanciacionFragment d = new DetalleFinanciacionFragment();
        d.datosFinanciacion = financiacion;
        return d;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String json = savedInstanceState.getString("datosFinanciacion");
            datosFinanciacion = new Gson().fromJson(json, Financiacion.class);
            String jsonLista = savedInstanceState.getString("listaCuotas");
            ListaCuotas lista = new Gson().fromJson(jsonLista, ListaCuotas.class);
            cuotas = lista.getLista();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("datosFinanciacion", new Gson().toJson(datosFinanciacion));
        ListaCuotas lista = new ListaCuotas(cuotas);
        outState.putString("listaCuotas", new Gson().toJson(lista));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detalle_financiacion, container, false);
        getActivity().setTitle("Detalle de Financiación");
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = false;

        cabeceraView = (ListView) v.findViewById(R.id.cabeceraDetalleFinanciacion);
        inicializarCabecera();
        listaCuotasView = (ListView) v.findViewById(R.id.cuotasDetalleFinanciacion);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        cuotasFinanciacionRequest = new CuotasFinanciacionRequest(getActivity(), new JacksonConverter(objectMapper));

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.titulo_1)).setTypeface(tf);
        ((TextView) v.findViewById(R.id.titulo_2)).setTypeface(tf);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (cuotas == null) {
            setearMensajeLoading();
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            Long numeroCuenta = datosFinanciacion.getNumeroCuenta();
            activity.getSpiceManager().execute(cuotasFinanciacionRequest.listarCuotas(numeroCuenta), new CuotasRequestListener());
        } else {
            cargarListaDeCuotas(cuotas);
        }
    }

    public final class CuotasRequestListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Activity context = getActivity();
            if (context != null) {
                removerUltimoElemento();
                setearMensajeSinServicio();
                Toast.makeText(context, MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List datos) {
            if (getActivity() != null) {
                removerUltimoElemento();
                if (datos != null) {
                    cargarListaDeCuotas(datos);
                    cuotas = datos;
                } else {
                    setearMensajeSinServicio();
                    Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Metodo que incializa la parte superior del sceen con los datos pertenecientes a la cabecera
     * estos datos son pasados por el fragment padre.
     */
    private void inicializarCabecera() {

        String numeroCuenta = String.valueOf(datosFinanciacion.getNumeroCuenta());
        String monto = NumbersUtils.formatear(datosFinanciacion.getMonto().longValue()) + " " + NumbersUtils.formatearUnidad("GUARANIES");
        String saldo = NumbersUtils.formatear(datosFinanciacion.getSaldo().longValue()) + " " + NumbersUtils.formatearUnidad("GUARANIES");
        String fechaInicio = Formateador.formatearFecha(datosFinanciacion.getFechaInicio());
        String fechaPrimerVencimiento = Formateador.formatearFecha(datosFinanciacion.getPrimerVencimiento());

        listaItems = new ArrayList<>();
        listaItems.add(new DatoPerfil(getResources().getString(R.string.codigo_label),
                numeroCuenta, R.drawable.ic_key_fact));
        listaItems.add(new DatoPerfil(getResources().getString(R.string.monto_financiado),
                monto, R.drawable.ic_monto));
        listaItems.add(new DatoPerfil(getResources().getString(R.string.saldo_label),
                saldo, R.drawable.ic_total_fact));
        listaItems.add(new DatoPerfil(getResources().getString(R.string.fecha_inicio),
                fechaInicio, R.drawable.ic_emision_fact));
        listaItems.add(new DatoPerfil(getResources().getString(R.string.primer_vencimiento),
                fechaPrimerVencimiento, R.drawable.ic_vencimiento_fact));
        listaItems.add(new DatoPerfil(getResources().getString(R.string.cuotas_pagadas),
                "?/?", R.drawable.ic_debito_fact));
        cabeceraAdapter = new CabeceraDetalleFinanciacionAdapter(getActivity(), listaItems);
        cabeceraView.setAdapter(cabeceraAdapter);
        ListUtils.setListViewHeightBasedOnChildren(cabeceraView, 0);
    }

    private void cargarListaDeCuotas(List<Cuota> lista) {
        Collections.sort(lista, new ComparadorCuotas());
        listaCuotas =  new ArrayList<>();
        if (!lista.isEmpty()) {
            for (Cuota cuota: lista) {
                ItemDeListaCuota item = new ItemDeListaCuota();
                item.setNumeroFactura(cuota.getNumeroFactura());
                item.setNumeroCuota(cuota.getNumeroCuota());
                item.setFechaVencimiento(cuota.getFechaVencimiento());
                item.setCodigoEstado(cuota.getCodigoEstado());
                item.setDescripcionEstado(cuota.getDescripcionEstado());
                item.setMontoCuota(cuota.getMontoCuota());
                listaCuotas.add(item);
            }
        } else {
            ItemDeListaCuota item = new ItemDeListaCuota();
            item.setEstado(ItemDeListaCuota.CODIGO_SIN_DATOS);
        }
        cuotaAdapter = new ListaCuotaDetalleFinanciacionAdapter(getActivity(), listaCuotas);
        listaCuotasView.setAdapter(cuotaAdapter);
        cuotaAdapter.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaCuotasView, 0);
        calcularCuotasFaltantes(listaCuotas);
    }

    private void calcularCuotasFaltantes (List<ItemDeListaCuota> lista) {
        Integer cuotasPagadas = 0;
        Integer totalDeCuotas = lista.size() - 1;
        for (ItemDeListaCuota cuota: lista) {
            if (cuota.isCancelada()) {
                cuotasPagadas = cuotasPagadas + 1;
            }
        }
        String relacionAbonadoFaltante = cuotasPagadas + "/" + totalDeCuotas;

        // Se carga los datos en la vista.
        removerUltimoElemento();
        listaItems.add(new DatoPerfil(getResources().getString(R.string.cuotas_pagadas), relacionAbonadoFaltante, R.drawable.ic_debito_fact));
        cabeceraAdapter.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(cabeceraView, 0);
    }

    public class ComparadorCuotas implements Comparator<Cuota> {
        @Override
        public int compare(Cuota o1, Cuota o2) {
            Integer oN1 = Integer.valueOf(o1.getNumeroCuota());
            Integer oN2 = Integer.valueOf(o2.getNumeroCuota());
            return oN2.compareTo(oN1);
        }
    }

    public void setearMensajeLoading() {
        ItemDeListaCuota item = new ItemDeListaCuota();
        item.setEstado(ItemDeListaCuota.CODIGO_LOADING);
        listaCuotas = new ArrayList<>();
        listaCuotas.add(item);
        cuotaAdapter = new ListaCuotaDetalleFinanciacionAdapter(getActivity(), listaCuotas);
        listaCuotasView.setAdapter(cuotaAdapter);
        cuotaAdapter.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaCuotasView, 0);
    }

    public void setearMensajeSinServicio() {
        ItemDeListaCuota item = new ItemDeListaCuota();
        item.setEstado(ItemDeListaCuota.CODIGO_SIN_SERVICIO);
        listaCuotas = new ArrayList<>();
        listaCuotas.add(item);
        cuotaAdapter = new ListaCuotaDetalleFinanciacionAdapter(getActivity(), listaCuotas);
        listaCuotasView.setAdapter(cuotaAdapter);
        cuotaAdapter.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaCuotasView, 0);
    }

    public void removerUltimoElemento() {
        listaItems.remove(listaItems.size() -1);
        cabeceraAdapter = new CabeceraDetalleFinanciacionAdapter(getActivity(), listaItems);
        cabeceraView.setAdapter(cabeceraAdapter);
        cabeceraAdapter.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(cabeceraView, 0);
    }
}

