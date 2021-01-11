package py.com.personal.mimundo.fragments.saldo.recarga;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.lineas.models.DetallesRecargaContraFactura;

/**
 * Esta clase define el adapter para renderizar el  historial de recarga de saldo s.o.s. realizada
 * por el usuario.
 *
 * @author Carlos Aquino
 */
public class MontosRecargaAdapter extends RecyclerView.Adapter<MontosRecargaAdapter.ViewHolder> {

    private List<DetallesRecargaContraFactura> mDataset;
    private Activity context;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout v) {
            super(v);
            relativeLayout = v;
        }
    }

    public MontosRecargaAdapter(Activity context) {
        this.mDataset = new ArrayList<>();
        this.context = context;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/CarroisGothic-Regular.ttf");
    }

    public void addItem(DetallesRecargaContraFactura detallesRecargaContraFactura) {
        mDataset.add(detallesRecargaContraFactura);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public MontosRecargaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tres_lineas_con_icono, parent, false);
        v.setBackgroundResource(R.drawable.switch_bakground);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetallesRecargaContraFactura item = mDataset.get(position);

        String fechaPrestamoString = item.getFecha() == null ? context.getResources().getString(R.string.sin_datos_fecha)
                : item.getFecha();
        String montoPrestamoString = item.getDescripcion() == null ? context.getResources().getString(R.string.monto_desconocido)
                : item.getDescripcion();
        String montoRecargoString = item.getCosto() == null ? context.getResources().getString(R.string.costo_recarga_desconocido)
                : "Costo - " + item.getCosto() + " Gs.";
        int icono = R.drawable.ic_send;

        if (item.getNumeroLineaDestino().equals("loading")) {
            montoPrestamoString = MensajesDeUsuario.TITULO_LOADING;
            montoRecargoString = MensajesDeUsuario.MENSAJE_LOADING;
            fechaPrestamoString = "";
            icono = R.drawable.ic_swap_vert;
        } else if (item.getNumeroLineaDestino().equals("sinDatos")) {
            montoPrestamoString = MensajesDeUsuario.TITULO_SIN_DATOS;
            montoRecargoString = MensajesDeUsuario.MENSAJE_SIN_DATOS;
            fechaPrestamoString = "";
            icono = R.drawable.ic_radio_button_off;
        } else if (item.getNumeroLineaDestino().equals("sinServicio")) {
            montoPrestamoString = MensajesDeUsuario.TITULO_SIN_SERVICIO;
            montoRecargoString = MensajesDeUsuario.MENSAJE_SIN_SERVICIO;
            fechaPrestamoString = "";
            icono = R.drawable.ic_cloud_off;
        }

        // Se seetan los datos en la vista.
        TextView _fechaPrestamo = (TextView)holder.relativeLayout.findViewById(R.id.descripcion2);
        TextView _montoPrestamo = (TextView)holder.relativeLayout.findViewById(R.id.titulo);
        TextView _montoRecargo = (TextView)holder.relativeLayout.findViewById(R.id.descripcion1);
        ImageView _icono = (ImageView)holder.relativeLayout.findViewById(R.id.icono);

        _fechaPrestamo.setText(fechaPrestamoString);
        _fechaPrestamo.setTypeface(tf);
        _montoPrestamo.setText(montoPrestamoString);
        _montoPrestamo.setTypeface(tf);
        _montoRecargo.setText(montoRecargoString);
        _montoRecargo.setTypeface(tf);
        _icono.setImageResource(icono);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
