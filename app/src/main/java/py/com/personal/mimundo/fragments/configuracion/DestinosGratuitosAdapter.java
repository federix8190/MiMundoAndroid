package py.com.personal.mimundo.fragments.configuracion;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.services.destinos.models.Destino;

/**
 * Created by Konecta on 07/10/2014.
 */
public class DestinosGratuitosAdapter extends ArrayAdapter<Destino> {

    private List<Destino> items;
    private Typeface tf;
    private static final int RESOURCE = R.layout.item_destino_gratuito;

    public DestinosGratuitosAdapter(Context context, List<Destino> items) {
        super(context, RESOURCE, items);
        this.items = items;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout view;
        if (convertView == null) {
            view = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(RESOURCE, view, true);
        } else {
            view = (LinearLayout) convertView;
        }

        final EditText destinoField = (EditText) view.findViewById(R.id.linea_destino_field);
        destinoField.setTypeface(tf);
        Destino lineaDestino = getItem(position);
        if (lineaDestino != null) {
            destinoField.setText(lineaDestino.getNumeroLinea());
        }

        // Listener para modificar los items de la lista de destinos gratuitos
        final int indice = position;
        destinoField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final EditText lineaDestino = (EditText) v;
                    Destino destino = items.get(indice);
                    destino.setNumeroLinea(lineaDestino.getText().toString());
                    items.set(indice, destino);
                }
            }
        });

        View.OnClickListener eliminarListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinoField.setText("");
                Destino destino = items.get(indice);
                destino.setNumeroLinea("");
                items.set(indice, destino);
            }
        };
        ImageButton eliminarDestinoButton = (ImageButton) view.findViewById(R.id.eliminar_destino_btn);
        eliminarDestinoButton.setOnClickListener(eliminarListener);

        return view;
    }

}
