package py.com.personal.mimundo.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.services.lineas.models.Servicio;

public class AdapterServicios extends RecyclerView.Adapter<AdapterServicios.ViewHolder> {

    private List<Servicio> mDataset;
    private Activity context;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout v) {
            super(v);
            relativeLayout = v;
        }
    }

    public AdapterServicios(Activity context) {
        this.mDataset = new ArrayList<>();
        this.context = context;
        if (context != null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
        }
    }

    public void addItem(Servicio servicio) {
        mDataset.add(servicio);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public AdapterServicios.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dos_lineas_sin_icono_1, parent, false);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Servicio item = mDataset.get(position);
        String texto1 = item.getDescripcion();
        Long fechaInicio = item.getFechaInicio();
        Long fechaFin = item.getFechaFin();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String fechaInicioString = "?";
        String fechaFinString = "?";

        if (fechaInicio != null) {
            Date _fechaInicio = new Date(fechaInicio);
            fechaInicioString = sdf.format(_fechaInicio);
        }
        if (fechaFin != null) {
            Date _fechaFin = new Date(fechaFin);
            fechaFinString = sdf.format(_fechaFin);
        }

        TextView titulo = (TextView) holder.relativeLayout.findViewById(R.id.titulo);
        titulo.setText(texto1);
        titulo.setTextColor(context.getResources().getColor(R.color.titulo_primario_card_view));
        titulo.setTypeface(tf);
        TextView descripcion1 = (TextView) holder.relativeLayout.findViewById(R.id.descripcion);
        //descripcion1.setText("Vigente desde " + fechaInicioString + " hasta " + fechaFinString);
        descripcion1.setText("\tVigencia: " + fechaInicioString + " al " + fechaFinString);
        descripcion1.setTypeface(tf);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
