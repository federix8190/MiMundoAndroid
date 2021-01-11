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
 * Created by Konecta on 16/09/2014.
 */
public class PackItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<NodoPack> nodos;
    private Typeface tf;

    public PackItemAdapter(Context c) {
        mContext = c;
        nodos = new ArrayList<>();
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
            gridView = li.inflate(R.layout.list_item_hashtags, parent, false);
        } else {
            gridView = convertView;
        }

        TextView titulo = (TextView) gridView.findViewById(R.id.nombre);
        titulo.setTypeface(tf);
        ImageView iconoDerecho  = (ImageView) gridView.findViewById(R.id.imagenIzquierda);
        NodoPack pack = (NodoPack)getItem(position);
        titulo.setText(pack.getDescripcion());


        if (pack.getDescripcion().toLowerCase().equals("combos")) {
            iconoDerecho.setImageResource(R.drawable.combo_packs);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_combos));
        } else if (pack.getDescripcion().toLowerCase().equals("mensajes")) {
            iconoDerecho.setImageResource(R.drawable.ic_email_white_big);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_mensajes));
        } else if (pack.getDescripcion().toLowerCase().equals("roaming")) {
            iconoDerecho.setImageResource(R.drawable.roaming_pack);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_roaming));
        } else if (pack.getDescripcion().toLowerCase().equals("internet")) {
            iconoDerecho.setImageResource(R.drawable.internet_packs);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_internet));
        } else if (pack.getDescripcion().toLowerCase().equals("spotify")) {
            iconoDerecho.setImageResource(R.drawable.spotify_pack);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_spotify));
        } else if (pack.getDescripcion().toLowerCase().equals("internacionales")) {
            iconoDerecho.setImageResource(R.drawable.internacional_pack);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_internacionales));
        } else if (pack.getDescripcion().toLowerCase().equals("tv")) {
            iconoDerecho.setImageResource(R.drawable.ic_tv_pack);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_tv));
        } else if (pack.getDescripcion().toLowerCase().equals("regalar packs")) {
            iconoDerecho.setImageResource(R.drawable.ic_bookmark_white_big);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_regalo));
        } else if (pack.getDescripcion().toLowerCase().equals("llamadas")) {
            iconoDerecho.setImageResource(R.drawable.llamadas_packs);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_default));
        } /*else if (pack.getDescripcion().toLowerCase().equals("minutos")) {
            iconoDerecho.setImageResource(R.drawable.ic_schedule_white_big);
        } else if (pack.getDescripcion().toLowerCase().equals("packs extendidos")) {
            iconoDerecho.setImageResource(R.drawable.ic_extension_white_big);
        } else if (pack.getDescripcion().toLowerCase().equals("packs normales")) {
            iconoDerecho.setImageResource(R.drawable.ic_keyboard_alt_white_big);
        }*/ else {
            iconoDerecho.setImageResource(R.drawable.internet_packs);
            gridView.setBackgroundColor(mContext.getResources().getColor(R.color.pack_default));
        }

        return gridView;
    }
}
