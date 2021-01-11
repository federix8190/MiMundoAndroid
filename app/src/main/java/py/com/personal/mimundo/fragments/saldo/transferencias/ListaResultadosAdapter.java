package py.com.personal.mimundo.fragments.saldo.transferencias;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.utils.NumbersUtils;

public class ListaResultadosAdapter extends RecyclerView.Adapter<ListaResultadosAdapter.ViewHolder> {

    private Typeface tf;
    private BaseDrawerActivity mContext;
    private List<ResultadoTransferencia> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView relativeLayout;

        public ViewHolder(CardView v) {
            super(v);
            relativeLayout = v;
        }
    }

    public ListaResultadosAdapter(BaseDrawerActivity context, List<ResultadoTransferencia> mDataset) {
        this.mDataset = mDataset;
        this.mContext = context;
        if (context != null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
        }
    }

    public void addItem(ResultadoTransferencia res) {
        mDataset.add(res);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public ListaResultadosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultado_transferencia, parent, false);
        ListaResultadosAdapter.ViewHolder vh = new ListaResultadosAdapter.ViewHolder((CardView) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListaResultadosAdapter.ViewHolder holder, int position) {

        final ResultadoTransferencia resultado = mDataset.get(position);

        TextView origenView = (TextView) holder.relativeLayout.findViewById(R.id.lineaOrigen);
        TextView destinoView = (TextView) holder.relativeLayout.findViewById(R.id.lineaDestino);
        TextView tipoView = (TextView) holder.relativeLayout.findViewById(R.id.tipo);
        TextView montoView = (TextView) holder.relativeLayout.findViewById(R.id.monto);
        TextView estadoView = (TextView) holder.relativeLayout.findViewById(R.id.estado);

        String tipo = resultado.getTipo();
        String monto = resultado.getMonto();
        monto = NumbersUtils.formatear(monto);

        if (tipo != null) {
            if (tipo.equals("GUA")) {
                tipo = "GUARANIES";
                monto = monto + " Gs.";
            } else if (tipo.equals("SMS")) {
                tipo = "MENSAJES";
                monto = monto + " SMS.";
            } else if (tipo.equals("MIN")) {
                tipo = "MINUTOS";
                monto = monto + " MIN.";
            } else if (tipo.equals("DAT")) {
                tipo = "DATOS";
                monto = monto + " MB.";
            }
        }

        origenView.setText(resultado.getOrigen());
        destinoView.setText(resultado.getDestino());
        tipoView.setText(tipo);
        montoView.setText(monto);
        estadoView.setText(resultado.getMensaje());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
