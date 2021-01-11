package py.com.personal.mimundo.adapters;

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
import py.com.personal.mimundo.services.lineas.models.Plan;
import py.com.personal.mimundo.utils.NumbersUtils;

public class AdapterPlanes extends RecyclerView.Adapter<AdapterPlanes.ViewHolder> {

    private List<Plan> mDataset;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout v) {
            super(v);
            relativeLayout = v;
        }
    }

    public AdapterPlanes(Activity context) {
        this.mDataset = new ArrayList<>();
        if (context != null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
        }
    }

    public void addItem(Plan plan) {
        mDataset.add(plan);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public AdapterPlanes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_planes, parent, false);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Plan item = mDataset.get(position);
        String titulo = item.getDescripcion();
        Double valor = item.getCostoMasIva();
        String moneda = item.getMoneda() == null? "GUARANIES": item.getMoneda();
        String tasador = item.getTasador();
        int icono;

        TextView _titulo = (TextView) holder.relativeLayout.findViewById(R.id.titulo);
        _titulo.setTypeface(tf);
        TextView _valor  = (TextView) holder.relativeLayout.findViewById(R.id.descripcion);
        _valor.setTypeface(tf);
        ImageView _icono  = (ImageView) holder.relativeLayout.findViewById(R.id.icono);

        _titulo.setText(titulo);

        if (valor != null) {
            _valor.setText("\t" + NumbersUtils.formatear(valor.intValue()) + " " + NumbersUtils.formatearUnidad(moneda));
        } else {
            _valor.setText("\t" + "...");
        }

        if (tasador != null) {
            if (tasador.equals("LLA")) {
                icono = R.drawable.ic_llam_planes;
            } else if (tasador.equals("SMS") || tasador.equals("MMS")) {
                icono = R.drawable.ic_mens_planes;
            } else if (tasador.equals("DAT")) {
                icono = R.drawable.ic_internet_planes;
            } else if (tasador.equals("WLL")) {
                icono = R.drawable.ic_wifi_tethering_3;
            } else if (tasador.equals("GPRS")) {
                icono = R.drawable.ic_swap_vert;
            } else {
                icono = R.drawable.ic_info;
            }
            _icono.setImageResource(icono);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

