package py.com.personal.mimundo.adapters.administracion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import py.com.personal.mimundo.activities.R;

public class AdapterDatosPerfil extends ArrayAdapter<DatoPerfil> {

    int resource;

    public AdapterDatosPerfil(Context context, int resource, List<DatoPerfil> items) {
        super(context, resource, items);
        this.resource = resource;
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
            li.inflate(resource, screen, true);
        } else {
            screen = (RelativeLayout) convertView;
        }
        TextView _titulo = (TextView)screen.findViewById(R.id.titulo);
        TextView _valor  = (TextView)screen.findViewById(R.id.descripcion);
        ImageView _icono  = (ImageView)screen.findViewById(R.id.icono);
        _titulo.setText(titulo);
        _valor.setText(valor);
        _icono.setImageResource(recurso);
        return screen;
    }

}
