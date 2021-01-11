package py.com.personal.mimundo.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import py.com.personal.mimundo.activities.R;

/**
 * Created by Konecta on 25/08/2015.
 */
public class DashboardAdapter extends BaseAdapter {

    private Context context;
    private List<String> listaTitulos;
    private List<Integer> listaImagenes;
    private Typeface tf;

    //Constructor to initialize values
    public DashboardAdapter(Context context, List<String> listaTitulos, List<Integer> listaImagenes) {
        this.context = context;
        this.listaTitulos = listaTitulos;
        this.listaImagenes = listaImagenes;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public int getCount() {
        // Number of times getView method call depends upon gridValues.length
        return listaTitulos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    // Number of times getView method call depends upon gridValues.length

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {
            gridView = inflater.inflate( R.layout.dashboard , null);
        } else {
            gridView = convertView;
        }

        String titulo = listaTitulos.get(position);
        Integer imagen = listaImagenes.get(position);
        ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView1);
        TextView textoView = (TextView) gridView.findViewById(R.id.textView1);
        textoView.setText(titulo);
        textoView.setTypeface(tf);
        imageView.setImageResource(imagen);

        return gridView;
    }
}
