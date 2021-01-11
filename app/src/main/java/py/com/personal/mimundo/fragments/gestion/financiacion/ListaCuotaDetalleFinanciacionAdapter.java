package py.com.personal.mimundo.fragments.gestion.financiacion;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.utils.NumbersUtils;

public class ListaCuotaDetalleFinanciacionAdapter extends ArrayAdapter<ItemDeListaCuota> {

    private final static int RESOURCE_LAYOUT = R.layout.item_cuota_detalle_financiacion ;
    private Typeface tf;

    public ListaCuotaDetalleFinanciacionAdapter(Context context, List<ItemDeListaCuota> items) {
        super(context, RESOURCE_LAYOUT, items);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout screen;
        ItemDeListaCuota item = getItem(position);

        if (convertView == null) {
            screen = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(RESOURCE_LAYOUT, screen, true);
        } else {
            screen = (LinearLayout) convertView;
        }

        TextView tituloView =  (TextView) screen.findViewById(R.id.titulo);
        tituloView.setTypeface(tf);
        TextView descripcion1View =  (TextView) screen.findViewById(R.id.descripcion1);
        descripcion1View.setTypeface(tf);
        TextView descripcion2View =  (TextView) screen.findViewById(R.id.descripcion2);
        descripcion2View.setTypeface(tf);
        ImageView icono = (ImageView) screen.findViewById(R.id.icono);

        if (item.getEstado().equals(ItemDeListaCuota.CODIGO_LOADING)) {
            tituloView.setText(MensajesDeUsuario.TITULO_LOADING);
            descripcion1View.setText(MensajesDeUsuario.MENSAJE_LOADING);
            icono.setImageResource(R.drawable.ic_swap_vert);
        } else if (item.getEstado().equals(ItemDeListaCuota.CODIGO_SIN_DATOS)) {
            tituloView.setText(MensajesDeUsuario.TITULO_SIN_DATOS);
            descripcion1View.setText(MensajesDeUsuario.MENSAJE_SIN_DATOS);
            icono.setImageResource(R.drawable.ic_radio_button_off);
        } else if (item.getEstado().equals(ItemDeListaCuota.CODIGO_SIN_SERVICIO)) {
            tituloView.setText(MensajesDeUsuario.TITULO_SIN_SERVICIO);
            descripcion1View.setText(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
            icono.setImageResource(R.drawable.ic_cloud_off);
        } else {
            tituloView.setText(item.getNumeroFactura());
            descripcion1View.setText(obtenerDescipcion1(item));
            descripcion2View.setText(obtenerDescipcion2(item));
            icono.setImageResource(R.drawable.ic_hash_fact);
        }

        return screen;
    }

    private String obtenerDescipcion1(ItemDeListaCuota item) {
        return "CUOTA " + item.getNumeroCuota() + " - " + item.getCodigoEstado();
    }

    private String obtenerDescipcion2(ItemDeListaCuota item) {
        return item.getFechaVencimiento() + " - " + NumbersUtils.formatear(item.getMontoCuota())
                + " " + NumbersUtils.formatearUnidad("GUARANIES");
    }
}
