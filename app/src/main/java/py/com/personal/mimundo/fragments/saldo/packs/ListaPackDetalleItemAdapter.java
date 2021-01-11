package py.com.personal.mimundo.fragments.saldo.packs;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.services.lineas.packs.NodoPack;

/**
 * Created by Konecta on 28/08/2015.
 */
public class ListaPackDetalleItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<NodoPack> nodos;
    //private String[] colores;
    private Typeface tf;

    public ListaPackDetalleItemAdapter(Context c) {
        mContext = c;
        nodos = new ArrayList<NodoPack>();
        //colores = mContext.getResources().getStringArray(R.array.coloresPacks);
        tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Platform-Regular.otf");
    }

    public Context getContext() {
        return mContext;
    }

    public void addItem(NodoPack nodoPack) {
        nodos.add(nodoPack);
    }

    public  void addCollection(List<NodoPack> lista) {
        nodos = lista;
    }

    public int getCount() {
        return nodos.size();
    }

    public Object getItem(int position) {
        return nodos == null? null: nodos.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView;
        if (convertView == null) {

            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            gridView = li.inflate(R.layout.fragment_lista_packs_detalle, parent, false);
        } else {

            gridView = (View) convertView;
        }

        TextView titulo = (TextView) gridView.findViewById(R.id.nombre);
        titulo.setTypeface(tf);
        NodoPack pack = (NodoPack)getItem(position);
        titulo.setText(pack.getDescripcion());

        return gridView;
    }
}
