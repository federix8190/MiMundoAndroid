package py.com.personal.mimundo.fragments.gestion.facturacion;

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
import py.com.personal.mimundo.services.lineas.models.Linea;

/**
 * Created by Usuario on 9/17/2014.
 */
public class LineasGrupoAdapter extends ArrayAdapter<Linea> {

    private final static int RESOURCE_LAYOUT = R.layout.item_lineas_grupo ;
    private Typeface tf;

    public LineasGrupoAdapter(Context context, List<Linea> items) {
        super(context, RESOURCE_LAYOUT, items);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout listItemView;
        Linea item = getItem(position);

        if (convertView == null) {
            listItemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(RESOURCE_LAYOUT, listItemView, true);
        } else {
            listItemView = (LinearLayout) convertView;
        }

        // Se extraen las referencias en el view.
        TextView titulo = (TextView) listItemView.findViewById(R.id.titulo);
        titulo.setTypeface(tf);
        TextView descripcion1 = (TextView) listItemView.findViewById(R.id.descripcion1);
        descripcion1.setTypeface(tf);
        TextView descripcion2 = (TextView) listItemView.findViewById(R.id.descripcion2);
        descripcion2.setTypeface(tf);
        ((TextView) listItemView.findViewById(R.id.title1)).setTypeface(tf);
        ((TextView) listItemView.findViewById(R.id.title2)).setTypeface(tf);

        // Datos.
        String numeroLineaString = item.getNumeroLinea() == null? MensajesDeUsuario.MENSAJE_NULL : item.getNumeroLinea();
        String descripcion1String = item.getNumeroContrato() == null? MensajesDeUsuario.MENSAJE_NULL : item.getNumeroContrato();
        String descripcion2String = item.getTipoLinea() == null? MensajesDeUsuario.MENSAJE_NULL : item.getTipoLinea();

        titulo.setText(numeroLineaString);
        descripcion1.setText(descripcion1String);
        descripcion2.setText(descripcion2String);

        ImageView _icono  = (ImageView)listItemView.findViewById(R.id.icono);
        _icono.setImageResource(R.drawable.ic_nro_linea);

        return listItemView;
    }
}
