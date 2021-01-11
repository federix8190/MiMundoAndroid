package py.com.personal.mimundo.fragments.configuracion;

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

/**
 * Created by carlos on 27/10/14.
 */
public class SuspensionTemporalAdapter extends ArrayAdapter<SuspensionTemporalAdapter.TipoSuspension> {

    private final static int RESOURCE_LAYOUT = R.layout.item_suspencion_temporal;
    private Typeface tf;

    public SuspensionTemporalAdapter(Context context, List<TipoSuspension> items) {
        super(context, RESOURCE_LAYOUT, items);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout todoView;
        TipoSuspension item = getItem(position);

        if (convertView == null) {
            todoView = new RelativeLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(RESOURCE_LAYOUT, todoView, true);
        } else {
            todoView = (RelativeLayout) convertView;
        }

        TextView titulo = (TextView)todoView.findViewById(R.id.titulo);
        titulo.setTypeface(tf);
        titulo.setText(item.getTitulo());

        //ImageView iconoIzquierdo = (ImageView)todoView.findViewById(R.id.iconoIzquierdo);
        //iconoIzquierdo.setImageResource(item.getIconoIzquierda());

        ImageView iconoDerecho = (ImageView)todoView.findViewById(R.id.iconoDerecho);
        if (item.isCheckeado()) {
            iconoDerecho.setImageResource(R.drawable.ic_check_box_celeste_institucional);
        } else {
            iconoDerecho.setImageResource(R.drawable.ic_check_box_outline_blank);
        }
        return todoView;
    }

    public static class TipoSuspension {
        private int iconoIzquierda;
        private String titulo;
        private boolean checkeado;
        private String codigo;

        public TipoSuspension(int iconoIzquierda, String titulo, boolean checkeado, String codigo) {
            this.iconoIzquierda = iconoIzquierda;
            this.titulo = titulo;
            this.checkeado = checkeado;
            this.codigo =  codigo;
        }

        public int getIconoIzquierda() {
            return iconoIzquierda;
        }

        public String getTitulo() {
            return titulo;
        }

        public boolean isCheckeado() {
            return checkeado;
        }

        public void setCheckeado(boolean checkeado) {
            this.checkeado = checkeado;
        }

        public String getCodigo() {
            return codigo;
        }
    }
}
