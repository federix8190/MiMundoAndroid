package py.com.personal.mimundo.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;

public class AdapterInfo1 extends RecyclerView.Adapter<AdapterInfo1.ViewHolder> {

    private List<ClaveValor> mDataset;
    private Activity context;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout mTextView;

        public ViewHolder(RelativeLayout v) {
            super(v);
            mTextView = v;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public AdapterInfo1(Activity context) {
        this.mDataset = new ArrayList<ClaveValor>();
        this.context = context;
    }

    public void addItem(String titulo, String descripcion) {
        ClaveValor claveValor = new ClaveValor(titulo, descripcion);
        mDataset.add(claveValor);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public AdapterInfo1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dos_lineas_sin_icono_1, parent, false);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView titulo = (TextView)holder.mTextView.findViewById(R.id.titulo);
        TextView descripcion = (TextView)holder.mTextView.findViewById(R.id.descripcion);

        titulo.setText(mDataset.get(position).getClave());
        descripcion.setText(mDataset.get(position).getValor());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ClaveValor {

        public String clave;
        public String valor;

        public ClaveValor(String clave, String valor) {
            this.clave = clave;
            this.valor = valor;
        }

        public String getClave() {
            return clave;
        }

        public String getValor() {
            return valor;
        }
    }
}
