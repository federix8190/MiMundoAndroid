package py.com.personal.mimundo.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;

public class AdapterInfo2 extends RecyclerView.Adapter<AdapterInfo2.ViewHolder> {

    private List<ClaveValor> mDataset;
    private Activity context;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public View mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = v;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public AdapterInfo2(Activity context) {
        this.mDataset = new ArrayList<ClaveValor>();
        this.context = context;
    }

    public void addItem(String titulo, ArrayList<TituloDescripcion> descripcion) {
        ClaveValor claveValor = new ClaveValor(titulo, descripcion);
        mDataset.add(claveValor);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public AdapterInfo2.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dos_lineas_sin_icono_1_con_cabecera, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClaveValor item = mDataset.get(position);
        TextView encabezado =  (TextView) holder.mTextView.findViewById(R.id.encabezado);
        encabezado.setText(item.getEncabezado());
        LinearLayout listaDeDatos = (LinearLayout) holder.mTextView.findViewById(R.id.lista_de_datos);
        for (TituloDescripcion dato : item.getValores()) {
            View plantilla = LayoutInflater.from(context).inflate(R.layout.item_dos_lineas_sin_icono_1, null);
            TextView titulo = (TextView) plantilla.findViewById(R.id.titulo);
            titulo.setText(dato.getTitulo());
            TextView descripcion = (TextView) plantilla.findViewById(R.id.descripcion);
            descripcion.setText(dato.getDescripcion());
            listaDeDatos.addView(plantilla);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ClaveValor {

        public String encabezado;
        public ArrayList<TituloDescripcion> valores;

        public ClaveValor(String clave, ArrayList<TituloDescripcion> valor) {
            this.encabezado = clave;
            this.valores = valor;
        }

        public String getEncabezado() {
            return encabezado;
        }

        public ArrayList<TituloDescripcion> getValores() {
            return valores;
        }
    }

    public static class TituloDescripcion {
        public String titulo;
        public String descripcion;

        public TituloDescripcion(String titulo, String descripcion) {
            this.titulo = titulo;
            this.descripcion = descripcion;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
}
