package py.com.personal.mimundo.fragments.saldo.prestamo;

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
import py.com.personal.mimundo.services.lineas.models.DetalleRecargaSos;

public class RecargaSosAdapter extends RecyclerView.Adapter<RecargaSosAdapter.ViewHolder> {


    private List<DetalleRecargaSos> mDataset;
    private Activity context;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout v) {
            super(v);
            relativeLayout = v;
        }
    }

    public RecargaSosAdapter(Activity context) {
        this.mDataset = new ArrayList<>();
        this.context = context;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/CarroisGothic-Regular.ttf");
    }

    public void addItem(DetalleRecargaSos detalleRecargaSos) {
        mDataset.add(detalleRecargaSos);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public RecargaSosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_tres_lineas_con_icono;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        v.setBackgroundResource(R.drawable.switch_bakground);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetalleRecargaSos item = mDataset.get(position);

        // Se extraen los datos del objeto.
        String fechaPrestamoString = item.getFechaPrestamo() == null ? context.getResources().getString(R.string.sin_datos_fecha):
                item.getFechaPrestamo();
        String montoPrestamoString = item.getMontoPrestamo() == null ? context.getResources().getString(R.string.monto_desconocido):
                String.valueOf(item.getMontoPrestamo().intValue()) + " Gs.";
        String montoRecargoString = item.getMontoRecargo() == null ? context.getResources().getString(R.string.monto_recarga_desconocido):
                "Recarga - " + String.valueOf(item.getMontoRecargo().intValue()) + " Gs.";
        int icono = R.drawable.ic_send;

        if (item.getMontoPrestamo() == -1.0) {
            montoPrestamoString = MensajesDeUsuario.TITULO_LOADING;
            montoRecargoString = MensajesDeUsuario.MENSAJE_LOADING;
            fechaPrestamoString = "";
            icono = R.drawable.ic_swap_vert;
        } else if (item.getMontoPrestamo() == -2.0) {
            montoPrestamoString = MensajesDeUsuario.TITULO_SIN_DATOS;
            montoRecargoString = item.getTiempoGracia();
            fechaPrestamoString = "";
            icono = R.drawable.ic_radio_button_off;
        } else if (item.getMontoPrestamo() == -3.0) {
            montoPrestamoString = MensajesDeUsuario.TITULO_SIN_SERVICIO;
            montoRecargoString = MensajesDeUsuario.MENSAJE_SIN_SERVICIO;
            fechaPrestamoString = "";
            icono = R.drawable.ic_cloud_off;
        }

        // Se seetan los datos en la vista.
        TextView _fechaPrestamo = (TextView) holder.relativeLayout.findViewById(R.id.descripcion2);
        TextView _montoPrestamo = (TextView) holder.relativeLayout.findViewById(R.id.titulo);
        TextView _montoRecargo = (TextView) holder.relativeLayout.findViewById(R.id.descripcion1);
        ImageView _icono = (ImageView) holder.relativeLayout.findViewById(R.id.icono);

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
