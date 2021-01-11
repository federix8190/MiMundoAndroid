package py.com.personal.mimundo.fragments.gestion.facturacion;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.grupos.models.GrupoFacturacion;

/**
 * Created by Konecta on 19/08/2014.
 */
public class ListaGrupoFacturacionAdapter extends RecyclerView.Adapter<ListaGrupoFacturacionAdapter.ViewHolder> {

    private List<GrupoFacturacion> mDataset;
    private Activity context;
    OnItemClickListener mItemClickListener;
    private Typeface tf;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

    public ListaGrupoFacturacionAdapter(Activity context) {
        this.mDataset = new ArrayList<>();
        this.context = context;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    public void addItem(GrupoFacturacion grupoFacturacion) {
        mDataset.add(grupoFacturacion);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public ListaGrupoFacturacionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_grupo_facturacion, parent, false);
        v.setClickable(true);
        v.setBackgroundResource(R.drawable.switch_bakground);
        ViewHolder vh = new ViewHolder((RelativeLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final GrupoFacturacion item = mDataset.get(position);

        ImageView icono = (ImageView) holder.relativeLayout.findViewById(R.id.icono_descripcion);
        TextView descripcionGrupoTextView = (TextView) holder.relativeLayout.findViewById(R.id.descripcion);
        descripcionGrupoTextView.setTypeface(tf);

        TextView codigoGrupoTextView = (TextView) holder.relativeLayout.findViewById(R.id.codigo);
        codigoGrupoTextView.setTypeface(tf);
        ((TextView) holder.relativeLayout.findViewById(R.id.codigo_title)).setTypeface(tf);

        TextView estadoGrupoTextView = (TextView) holder.relativeLayout.findViewById(R.id.estado);
        estadoGrupoTextView.setTypeface(tf);
        ((TextView) holder.relativeLayout.findViewById(R.id.estado_title)).setTypeface(tf);

        // Se insertan los datos.
        if (item.getCodigo().equals("loading")) {
            codigoGrupoTextView.setText(MensajesDeUsuario.TITULO_LOADING);
            estadoGrupoTextView.setText(MensajesDeUsuario.MENSAJE_LOADING);
        } else if (item.getCodigo().equals("sinServicio")) {
            codigoGrupoTextView.setText(MensajesDeUsuario.TITULO_SIN_SERVICIO);
            estadoGrupoTextView.setText(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
        } else if (item.getCodigo().equals("sinDatos")) {
            codigoGrupoTextView.setText(context.getResources().getString(R.string.sin_grupos_facturacion));
            estadoGrupoTextView.setText(context.getResources().getString(R.string.sin_grupos_para_esta_linea));
        } else {
            String descripcion = item.getDescripcion();
            descripcionGrupoTextView.setText(descripcion);
            codigoGrupoTextView.setText(item.getCodigo());
            estadoGrupoTextView.setText(item.getEstado());
            if (descripcion.contains("TELEFONIA")) {
                icono.setImageResource(R.drawable.ic_smart_fact);
            } else if (descripcion.contains("DHT") || descripcion.equals("PERSONAL_TV")) {
                icono.setImageResource(R.drawable.ic_tv_fact);
            } else if (descripcion.contains("WIRELESS")) {
                icono.setImageResource(R.drawable.ic_mundo_fact);
            } else {
                icono.setImageResource(R.drawable.ic_hash_fact);
            }
        }

        // Ver detalle
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = DetalleGrupoFacturacionFragment.newInstance(item);
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
