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
import py.com.personal.mimundo.adapters.administracion.DatoPerfil;

public class CabeceraDetalleFinanciacionAdapter extends ArrayAdapter<DatoPerfil>{

    private final static int RESOURCE_LAYOUT = R.layout.item_cabecera_detalle_financiacion ;
    private Typeface tf;

    public CabeceraDetalleFinanciacionAdapter(Context context, List<DatoPerfil> items) {
        super(context, RESOURCE_LAYOUT, items);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout screen;
        DatoPerfil item = getItem(position);

        if (convertView == null) {
            screen = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(RESOURCE_LAYOUT, screen, true);
        } else {
            screen = (LinearLayout) convertView;
        }

        TextView tituloView = (TextView) screen.findViewById(R.id.titulo);
        tituloView.setTypeface(tf);
        tituloView.setText(item.getTitulo());

        TextView descripcionView = (TextView) screen.findViewById(R.id.descripcion);
        descripcionView.setTypeface(tf);
        descripcionView.setText(item.getValor());

        ImageView icono = (ImageView) screen.findViewById(R.id.icono);
        icono.setImageResource(item.getIcono());

        return screen;
    }
}
