package py.com.personal.mimundo.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.models.NavDrawerItem;

public class AdapterInfoPacks extends BaseExpandableListAdapter {

    private Context _context;
    private List<ClaveValor> _listDataHeader;
    private HashMap<String, List<DetallePack>> _listDataChild;
    private Typeface tf;

    public AdapterInfoPacks(Context context, List<ClaveValor> listDataHeader, HashMap<String, List<DetallePack>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getClave());//.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final List<DetallePack> datos = (List<DetallePack>) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_detalle_saldo_pack, null);
        }
        List<String> items = new ArrayList();
        for (DetallePack d : datos) {
            items.add(d.getVigencia());
            items.add(d.getSaldo());
        }
        ((TextView) convertView.findViewById(R.id.headerGrid_1)).setTypeface(tf);
        ((TextView) convertView.findViewById(R.id.headerGrid_2)).setTypeface(tf);
        PackGridAdapter adapter = new PackGridAdapter(this._context, items);
        GridView gridView = (GridView) convertView.findViewById(R.id.gridView_detalle_pack);
        gridView.setAdapter(adapter);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getClave()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ClaveValor datos = (ClaveValor) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_saldos_packs, null);
        }

        TextView tituloView = (TextView) convertView.findViewById(R.id.titulo);
        TextView descripcionView = (TextView) convertView.findViewById(R.id.descripcion);
        tituloView.setText(datos.getClave());
        descripcionView.setText("\t" + datos.getValor());
        tituloView.setTypeface(tf);
        descripcionView.setTypeface(tf);

        ImageView iconoColapsable = (ImageView) convertView.findViewById(R.id.iconoColapsable);
        if (isExpanded) {
            iconoColapsable.setImageResource(R.drawable.ic_expand_less);
        } else {
            iconoColapsable.setImageResource(R.drawable.ic_expand_more);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ClaveValor {

        public String clave;
        public String valor;

        public ClaveValor(String clave, String valor) {
            this.clave = clave;
            this.valor = valor;
        }

        public String getClave() {
            return clave;
        }

        public String getValor() {
            return valor;
        }
    }

    public static class DetallePack {

        public String vigencia;
        public String saldo;

        public DetallePack(String vigencia, String saldo) {
            this.vigencia = vigencia;
            this.saldo = saldo;
        }

        public String getVigencia() {
            return vigencia;
        }

        public void setVigencia(String vigencia) {
            this.vigencia = vigencia;
        }

        public String getSaldo() {
            return saldo;
        }

        public void setSaldo(String saldo) {
            this.saldo = saldo;
        }
    }

}

