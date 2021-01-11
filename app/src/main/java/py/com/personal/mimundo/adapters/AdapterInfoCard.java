package py.com.personal.mimundo.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;

public class AdapterInfoCard  extends RecyclerView.Adapter<AdapterInfoCard.ViewHolder> {

    private List<ClaveValor> mDataset;
    private Activity context;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView card;

        public ViewHolder(CardView v) {
            super(v);
            card = v;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public AdapterInfoCard(Activity context) {
        this.mDataset = new ArrayList<ClaveValor>();
        this.context = context;
    }

    public void addItem(String titulo, boolean isEditable, View.OnClickListener listener,
            ArrayList<TituloDescripcion> datos, int background, boolean isHeader, boolean isClickable) {
        ClaveValor claveValor;
        claveValor = new ClaveValor(titulo, isEditable, listener, datos, background, isHeader, isClickable);
        mDataset.add(claveValor);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public AdapterInfoCard.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_perfil, parent, false);
        ViewHolder vh = new ViewHolder((CardView)v);
        return vh;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClaveValor item = mDataset.get(position);

        if (holder.card.getTag() == null) {
            holder.card.setTag(position);

            TextView tituloDelCard = (TextView) holder.card.findViewById(R.id.titulo_card);
            tituloDelCard.setText(item.getTitulo());
            View contenedorCabecera = holder.card.findViewById(R.id.contenedor_cabecera);
            if(Build.VERSION.SDK_INT > 16) {
                contenedorCabecera.setBackground(context.getResources().getDrawable(item.getBackground()));
            } else {
                contenedorCabecera.setBackgroundDrawable(context.getResources().getDrawable(item.getBackground()));
            }

            // Si es editable agregar un flat button al cardview.
            if (item.isEditable()) {
                View editar = holder.card.findViewById(R.id.flat_button_editar_perfil);
                editar.setVisibility(View.VISIBLE);
                Button boton = (Button) holder.card.findViewById(R.id.boton);
                boton.setOnClickListener(item.getListener());
            }

            if (item.isClickable()) {
                View elementoCLickable = holder.card.findViewById(R.id.body_card_view);
                elementoCLickable.setClickable(true);
                elementoCLickable.setOnClickListener(item.getListener());
                tituloDelCard.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flecha_enter_card, 0, 0, 0);
            }

            // Carga la lista de datos.
            LinearLayout listaDeDatos = (LinearLayout) holder.card.findViewById(R.id.contenedor_datos);
            for (TituloDescripcion dato : item.getDatos()) {
                View plantilla = LayoutInflater.from(context).inflate(R.layout.item_card_perfil, null);
                TextView titulo = (TextView) plantilla.findViewById(R.id.titulo);
                titulo.setText(dato.getTitulo());
                TextView descripcion = (TextView) plantilla.findViewById(R.id.descripcion);
                descripcion.setText(dato.getDescripcion());
                listaDeDatos.addView(plantilla);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ClaveValor {

        public String titulo;
        public boolean isEditable;
        public ArrayList<TituloDescripcion> datos;
        public int background;
        public boolean header;
        public boolean clickable;
        public View.OnClickListener listener;

        public ClaveValor(
                String clave,
                boolean editable,
                View.OnClickListener listener,
                ArrayList<TituloDescripcion> datos,
                int backgrounnd,
                boolean isHeader,
                boolean isClickable
        ) {
            this.titulo = clave;
            this.isEditable = editable;
            this.datos = datos;
            this.background = backgrounnd;
            this.header = isHeader;
            this.clickable = isClickable;
            this.listener = listener;
        }

        public View.OnClickListener getListener() {
            return listener;
        }

        public String getTitulo() {
            return titulo;
        }

        public boolean isEditable() {
            return isEditable;
        }

        public ArrayList<TituloDescripcion> getDatos() {
            return datos;
        }

        public int getBackground() {
            return background;
        }

        public boolean isHeader() {
            return header;
        }

        public boolean isClickable() {
            return clickable;
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