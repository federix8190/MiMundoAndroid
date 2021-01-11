package py.com.personal.mimundo.fragments.gestion.facturacion;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.administracion.DatoPerfil;

/**
 * Created by Konecta on 17/10/2015.
 */
public class DetalleFacturaAdapter extends ArrayAdapter<DatoPerfil> {

    private final static int RESOURCE_LAYOUT = R.layout.item_detalle_factura;
    private Typeface tf;

    public DetalleFacturaAdapter(Context context, List<DatoPerfil> items) {
        super(context, RESOURCE_LAYOUT, items);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout screen;
        DatoPerfil item = getItem(position);
        String titulo = item.getTitulo();
        String valor = item.getValor();
        int recurso = item.getIcono();

        if (convertView == null) {
            screen = new RelativeLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(RESOURCE_LAYOUT, screen, true);
        } else {
            screen = (RelativeLayout) convertView;
        }

        TextView tituloView = (TextView)screen.findViewById(R.id.titulo);
        tituloView.setTypeface(tf);
        tituloView.setText(titulo);

        TextView descripcionView  = (TextView)screen.findViewById(R.id.descripcion);
        ImageView iconoView  = (ImageView)screen.findViewById(R.id.icono);
        descripcionView.setTypeface(tf);

        descripcionView.setText(valor);
        iconoView.setImageResource(recurso);

        return screen;
    }
}
