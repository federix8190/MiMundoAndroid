package py.com.personal.mimundo.fragments.gestion.financiacion;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.services.financiaciones.models.Financiacion;
import py.com.personal.mimundo.utils.NumbersUtils;

public class ListaFinanciacionAdapter extends RecyclerView.Adapter<ListaFinanciacionAdapter.ViewHolder> {

    private List<Financiacion> mDataset;
    private Activity context;
    OnItemClickListener mItemClickListener;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout v) {
            super(v);
            relativeLayout = v;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public ListaFinanciacionAdapter(Activity context) {
        this.mDataset = new ArrayList<>();
        this.context = context;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    public void addItem(Financiacion financiacion) {
        mDataset.add(financiacion);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public ListaFinanciacionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_financiacion, parent, false);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Financiacion item = mDataset.get(position);

        ImageView icono = (ImageView) holder.relativeLayout.findViewById(R.id.icono);
        icono.setImageResource(R.drawable.ic_hash_fact);

        TextView titulo = (TextView) holder.relativeLayout.findViewById(R.id.titulo);
        titulo.setTypeface(tf);
        titulo.setText(item.getNumeroCuenta().toString());

        TextView estadoView = (TextView) holder.relativeLayout.findViewById(R.id.estado);
        ((TextView) holder.relativeLayout.findViewById(R.id.estado_title)).setTypeface(tf);
        estadoView.setTypeface(tf);
        estadoView.setText(item.getDescripcionEstado());

        TextView montoView = (TextView) holder.relativeLayout.findViewById(R.id.monto);
        ((TextView) holder.relativeLayout.findViewById(R.id.monto_title)).setTypeface(tf);
        montoView.setTypeface(tf);
        montoView.setText(NumbersUtils.formatear(item.getMonto().intValue())
                + " " + NumbersUtils.formatearUnidad("GUARANIES"));

        // Ver detalle
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetalleFinanciacionFragment fragment = DetalleFinanciacionFragment.newInstance(item);
                AppCompatActivity activity = (AppCompatActivity) context;
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }
        };
        Button verDetallButton = (Button) holder.relativeLayout.findViewById(R.id.button_ver_detalle);
        verDetallButton.setOnClickListener(listener);
        verDetallButton.setTypeface(tf);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}