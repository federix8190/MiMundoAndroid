package py.com.personal.mimundo.fragments.saldo.transferencias;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.utils.StringUtils;

public class ListaLineaAdapter extends ArrayAdapter<ElementoCheck> {

    private static final int LAYOUT = R.layout.item_lista_de_lineas_transferencia;
    private Typeface tf;

    public ListaLineaAdapter(Context context, List<ElementoCheck> items) {
        super(context, LAYOUT, items);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout screen;
        ElementoCheck item = getItem(position);
        String numero = item.getLinea().getNumeroLinea();

        if (convertView == null) {
            screen = new RelativeLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(LAYOUT, screen, true);
        } else {
            screen = (RelativeLayout) convertView;
        }

        TextView linea = (TextView)screen.findViewById(R.id.textLineasTransferencia);
        linea.setTypeface(tf);
        CheckBox check = (CheckBox)screen.findViewById(R.id.checkLineasTransferencia);

        check.setOnClickListener(null);
        check.setClickable(false);

        if (StringUtils.esLineaTelefonia(numero)) {
            linea.setText("0" + numero);
        } else {
            linea.setText(numero);
        }

        item.setTextViewTitle(linea);
        item.setCheckBox(check);

        return screen;
    }
}
