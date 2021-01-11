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

public class AdapterLineasTransferencia extends RecyclerView.Adapter<AdapterLineasTransferencia.ViewHolder> {

    private List<String> mDataset;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout v) {
            super(v);
            relativeLayout = v;
        }
    }

    public AdapterLineasTransferencia(Activity context, List<String> lineas) {
        this.mDataset = lineas;//new ArrayList<>();
        if (context != null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
        }
    }

    public void addItems(List<String> lineas) {
        mDataset.addAll(lineas);
        //notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(String linea) {
        mDataset.add(linea);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public AdapterLineasTransferencia.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lineas_transferencia, parent, false);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String linea = mDataset.get(position);
        System.err.println("Agregar Linea adapter : " + linea);
        TextView _titulo = (TextView) holder.relativeLayout.findViewById(R.id.linea);
        _titulo.setText(linea);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
