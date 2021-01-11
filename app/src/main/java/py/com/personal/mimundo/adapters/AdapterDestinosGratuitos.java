package py.com.personal.mimundo.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;

public class AdapterDestinosGratuitos extends RecyclerView.Adapter<AdapterDestinosGratuitos.ViewHolder> {

    private List<String> mDataset;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout v) {
            super(v);
            relativeLayout = v;
        }
    }

    public AdapterDestinosGratuitos(Activity context, List<String> lineas) {
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

    public void removeItem(int posicion) {
        mDataset.remove(posicion);
        notifyDataSetChanged();
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public AdapterDestinosGratuitos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linea_destino_gratuito, parent, false);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterDestinosGratuitos.ViewHolder holder, final int position) {

        String linea = mDataset.get(position);
        System.err.println("Agregar Linea adapter : " + linea);
        TextView _titulo = (TextView) holder.relativeLayout.findViewById(R.id.linea);
        _titulo.setText(linea);

        /*ImageView eliminar = (ImageView) holder.relativeLayout.findViewById(R.id.eliminar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.err.println("Eliminar elemento : " + position);
                removeItem(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
