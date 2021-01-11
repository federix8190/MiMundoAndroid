package py.com.personal.mimundo.fragments.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.activities.SaldosParceable;
import py.com.personal.mimundo.adapters.AdapterInfoCard;
import py.com.personal.mimundo.services.cache.lineas.ObtenerSaldosLineaRequest;
import py.com.personal.mimundo.services.lineas.models.Saldos;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class SaldoFragment extends Fragment {

    public static final String DETALLE_SALDO = "detalle_saldo";
    private boolean realizoPeticionDeSaldo = false;

    private ObtenerSaldosLineaRequest saldosRequest;

    private String linea;
    private RecyclerView recycleView;
    private AdapterInfoCard mAdapter;
    private View progressbar;
    //private View.OnClickListener listener0;
    private View.OnClickListener listener1;
    //private View.OnClickListener listener2;
    private SaldosParceable saldoParceable;
    private Saldos saldos;

    public static SaldoFragment newInstance() {
        SaldoFragment f = new SaldoFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_saldo, container, false);
        Activity context = getActivity();

        recycleView = (RecyclerView) v.findViewById(R.id.lista_cards_perfil);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(mLayoutManager);
//        recycleView.setItemAnimator(new GarageDoorItemAnimator());
        recycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterInfoCard(context);
        recycleView.setAdapter(mAdapter);

        progressbar = v.findViewById(R.id.progressbar);

        final BaseDrawerActivity activity = ((BaseDrawerActivity) getActivity());
        linea = activity.obtenerLineaSeleccionada();

        listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jsonSaldos = new Gson().toJson(saldos);
                Bundle b = new Bundle();
                b.putString(PacksDeSaldoFragment.SALDO, jsonSaldos);
                Intent intent = new Intent(activity, PacksDeSaldoFragment.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        };

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
            //activity.getSpiceManager().execute(saldosRequest.saldosLinea(linea), new SaldosLineaRequestListener());
        }
    }

    public void showProgress(final boolean show) {
        progressbar.setVisibility(show? View.VISIBLE: View.GONE);
        recycleView.setVisibility(show? View.GONE: View.VISIBLE);
    }

    /*public final class SaldosLineaRequestListener implements RequestListener<Saldos> {

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
                cargarDatosSaldo(datos);
                cargarDatosPacks(datos);
                cargarCardConsumo();
                realizoPeticionDeSaldo = true;
            }
        }
    }*/

    private void cargarDatosPacks(Saldos datos) {
        String titulo = getResources().getString(R.string.packs_vigentes_title);
        if (datos == null) {
            ArrayList<AdapterInfoCard.TituloDescripcion> datoSaldo = new ArrayList<>();
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Packs", getResources().getString(R.string.sin_servicio)));
            mAdapter.addItem(titulo, false, listener1, datoSaldo, R.drawable.card_view_encabezado_1, false, false);
            return;
        }
        saldos = datos;
        ArrayList<AdapterInfoCard.TituloDescripcion> datoSaldo = new ArrayList<>();
        datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Packs", getResources().getString(R.string.packs_vigentes_descripcion)));
        mAdapter.addItem(titulo, false, listener1, datoSaldo, R.drawable.card_view_encabezado_1, false, true);
    }

    /*private void cargarDatosSaldo(Saldos datos) {

        String titulo = "El saldo de tu línea";

        if (datos == null) {
            ArrayList<AdapterInfoCard.TituloDescripcion> datoSaldo = new ArrayList<>();
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Sin datos de tu saldo", "Sin servicio.."));
            mAdapter.addItem(titulo, false, listener0, datoSaldo, R.drawable.card_view_encabezado_4, false, false);
            return;
        }

        saldoParceable = new SaldosParceable(datos.getMensajes(), datos.getMinutos(), datos.getDatos(), datos.getMoneda());
        ArrayList<AdapterInfoCard.TituloDescripcion> datoSaldo = new ArrayList<>();

        // Moneda
        List<DetalleSaldo> saldoMoneda = datos.getMoneda();
        if (saldoMoneda != null && saldoMoneda.size() > 0) {
            String montoSaldo = NumbersUtils.formatear(saldoMoneda.get(0).getMonto());
            String unidad = NumbersUtils.formatearUnidad(saldoMoneda.get(0).getUnidad());
            String descripcion = montoSaldo + " " + unidad;
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Moneda", descripcion));
        } else {
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Moneda", "0 " + NumbersUtils.formatearUnidad("GUARANIES")));
        }

        // Mensajes
        List<DetalleSaldo> saldoMensajes = datos.getMensajes();
        if (saldoMensajes != null && saldoMensajes.size() > 0) {
            String montoSaldo = NumbersUtils.formatear(saldoMensajes.get(0).getMonto());
            String unidad = NumbersUtils.formatearUnidad(saldoMensajes.get(0).getUnidad());
            String descripcion = montoSaldo + " " + unidad;
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Mensajes", descripcion));
        } else {
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Mensajes", "0 MENSAJES"));
        }

        // Datos
        List<DetalleSaldo> saldoDatos = datos.getDatos();
        if (saldoDatos != null && saldoDatos.size() > 0) {
            String montoSaldo = NumbersUtils.formatear(saldoDatos.get(0).getMonto());
            String unidad = NumbersUtils.formatearUnidad( saldoDatos.get(0).getUnidad());
            String descripcion = montoSaldo + " " + unidad;
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Datos", descripcion));
        } else {
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Datos", "0 " + NumbersUtils.formatearUnidad("MEGABYTES")));
        }

        // Minutos
        List<DetalleSaldo> saldoMinutos = datos.getMinutos();
        if (saldoMinutos != null && saldoMinutos.size() > 0) {
            String montoSaldo = NumbersUtils.formatear(saldoMinutos.get(0).getMonto());
            String unidad = NumbersUtils.formatearUnidad(saldoMinutos.get(0).getUnidad());
            String descripcion = montoSaldo + " " + unidad;
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Minutos", descripcion));
        } else {
            datoSaldo.add(new AdapterInfoCard.TituloDescripcion("Minutos", "0 MINUTOS"));
        }

        mAdapter.addItem(titulo, false, listener0, datoSaldo, R.drawable.card_view_encabezado_4, false, true);

        List<DetalleSaldo> pospagoArray = datos.getPospago();
        boolean tieneLimiteConsumo = pospagoArray.size() != 0;
        if (tieneLimiteConsumo) {
            String montoDisponible = null;
            String montoExcedente = null;
            for (DetalleSaldo detalle : pospagoArray) {
                if(detalle.getTipo().equals("Saldo Disponible")) {
                    String monto = NumbersUtils.formatear(detalle.getMonto());
                    montoDisponible = monto + " " + detalle.getUnidad();
                } else if (detalle.getTipo().equals("Saldo Excedente")) {
                    String monto = NumbersUtils.formatear(detalle.getMonto());
                    montoExcedente = monto + " " + detalle.getUnidad();
                }
            }
            ArrayList<AdapterInfoCard.TituloDescripcion> datoPospago = new ArrayList<AdapterInfoCard.TituloDescripcion>();
            datoPospago.add(new AdapterInfoCard.TituloDescripcion("Disponible", montoDisponible));
            datoPospago.add(new AdapterInfoCard.TituloDescripcion("Excedente", montoExcedente));
            mAdapter.addItem("Límite de consumo", false, null, datoPospago, R.drawable.card_view_encabezado_3, false, false);
        }
    }

    private void cargarCardConsumo() {
        String titulo = "Tu consumo";
        int background = R.drawable.card_view_encabezado_2;
        ArrayList<AdapterInfoCard.TituloDescripcion> datoSaldo = new ArrayList<AdapterInfoCard.TituloDescripcion>();
        String tituloConsumo = getResources().getString(R.string.detalle_consumo_title);
        String descripcionConsumo = getResources().getString(R.string.detalle_consumo_descripcion);
        datoSaldo.add(new AdapterInfoCard.TituloDescripcion(tituloConsumo, descripcionConsumo));
        mAdapter.addItem(titulo, false, listener2, datoSaldo, background, false, true);
    }*/

}
