package py.com.personal.mimundo.fragments.gestion.facturacion;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.grupos.models.Factura;
import py.com.personal.mimundo.services.grupos.models.GrupoFacturacion;
import py.com.personal.mimundo.utils.NumbersUtils;

public class FacturasGrupoAdapter extends RecyclerView.Adapter<FacturasGrupoAdapter.ViewHolder>{

    public static final String CODIGO_LOADING = "loading";
    public static final String CODIGO_SIN_SERVICIO = "sinServicio";
    public static final String CODIGO_SIN_DATOS = "sinDatos";

    private List<Factura> mDataset;
    private Activity context;
    OnItemClickListener mItemClickListener;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout relativeLayout;

        public ViewHolder(LinearLayout v) {
            super(v);
            relativeLayout = v;
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public FacturasGrupoAdapter(Activity context) {
        this.mDataset = new ArrayList<>();
        this.context = context;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    public void addItem(Factura factura) {
        mDataset.add(factura);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public FacturasGrupoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_facturas, parent, false);
        v.setClickable(true);
        v.setBackgroundResource(R.drawable.switch_bakground);
        ViewHolder vh = new ViewHolder((LinearLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Factura item = mDataset.get(position);
        TextView numeroFacturaView = (TextView) holder.relativeLayout.findViewById(R.id.numero_factura);
        numeroFacturaView.setTypeface(tf);
        TextView estadoView = (TextView) holder.relativeLayout.findViewById(R.id.estado);
        ((TextView) holder.relativeLayout.findViewById(R.id.estado_title)).setTypeface(tf);
        estadoView.setTypeface(tf);
        TextView vencimientoView = (TextView) holder.relativeLayout.findViewById(R.id.vencimiento);
        ((TextView) holder.relativeLayout.findViewById(R.id.vencimiento_title)).setTypeface(tf);
        vencimientoView.setTypeface(tf);
        TextView montoView = (TextView) holder.relativeLayout.findViewById(R.id.monto);
        ((TextView) holder.relativeLayout.findViewById(R.id.monto_title)).setTypeface(tf);
        montoView.setTypeface(tf);

        if (item.getCodigoDocumento().equals(CODIGO_LOADING)) {
            numeroFacturaView.setText(MensajesDeUsuario.TITULO_LOADING);
            estadoView.setText(MensajesDeUsuario.MENSAJE_LOADING);
        } else if (item.getCodigoDocumento().equals(CODIGO_SIN_SERVICIO)) {
            numeroFacturaView.setText(MensajesDeUsuario.TITULO_SIN_SERVICIO);
            estadoView.setText(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
        } else if (item.getCodigoDocumento().equals(CODIGO_SIN_DATOS)){
            numeroFacturaView.setText(MensajesDeUsuario.TITULO_SIN_DATOS);
            estadoView.setText(MensajesDeUsuario.MENSAJE_SIN_DATOS);
        } else {
            String montoCuota = obtenerMontoCuota(item.getMontoTotal());
            String numeroFactura = obtenerNumeroFactura(item.getNumeroFactura());
            String vencimientoString = obtenerVencimiento(item.getFechaVencimiento());
            String estado = item.getEstado();
            numeroFacturaView.setText(numeroFactura);
            estadoView.setText(estado);
            vencimientoView.setText(vencimientoString);
            montoView.setText(montoCuota);
        }

        // Ver detalle
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = DetalleFacturaFragment.newInstance(item);
                AppCompatActivity activity = (AppCompatActivity) context;
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        Button verDetallButton = (Button) holder.relativeLayout.findViewById(R.id.button_ver_detalle);
        verDetallButton.setOnClickListener(listener);
        verDetallButton.setTypeface(tf);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private String obtenerNumeroFactura(Long numeroFactura) {
        if (numeroFactura == null) {
            return context.getResources().getString(R.string.numero_factura_desconocido);
        } else {
            return "# " + String.valueOf(numeroFactura);
        }
    }

    private String obtenerMontoCuota(Double montoCuota) {
        if (montoCuota == null) {
            return context.getResources().getString(R.string.monto_cuota_desconocido);
        } else {
            return NumbersUtils.formatear(montoCuota.longValue()) + " Gs.";
        }
    }

    private String obtenerVencimiento(Long vencimiento) {
        if (vencimiento == null) {
            return context.getResources().getString(R.string.sin_datos_vencimiento);
        } else {
            return formatearFecha(vencimiento);
        }
    }

    public String formatearFecha(Long vencimiento) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date vencimientoDate = new Date(vencimiento);
        return sdf.format(vencimientoDate);
    }
}
