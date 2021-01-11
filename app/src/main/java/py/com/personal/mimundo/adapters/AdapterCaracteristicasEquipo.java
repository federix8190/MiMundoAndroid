package py.com.personal.mimundo.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;

public class AdapterCaracteristicasEquipo extends RecyclerView.Adapter<AdapterCaracteristicasEquipo.ViewHolder> {

    private List<CaracteristicasEquipo> mDataset;
    private Activity context;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout v) {
            super(v);
            relativeLayout = v;
        }
    }

    public AdapterCaracteristicasEquipo(Activity context) {
        this.mDataset = new ArrayList<CaracteristicasEquipo>();
        this.context = context;
        if (context != null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
        }
    }

    public void addItem(CaracteristicasEquipo caracteristicasEquipo) {
        mDataset.add(caracteristicasEquipo);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public AdapterCaracteristicasEquipo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datos_equipo, parent, false);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.relativeLayout.setClickable(true);
        CaracteristicasEquipo item = mDataset.get(position);

        String titulo = item.getCaracteristica();
        String valor = item.getDescripcion();

        TextView _titulo = (TextView)holder.relativeLayout.findViewById(R.id.titulo);
        _titulo.setTypeface(tf);
        TextView _valor  = (TextView)holder.relativeLayout.findViewById(R.id.descripcion);
        _valor.setTypeface(tf);
        _titulo.setText(titulo);
        _valor.setText(valor);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
