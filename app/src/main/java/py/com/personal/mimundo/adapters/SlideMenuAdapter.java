package py.com.personal.mimundo.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.models.NavDrawerItem;

public class SlideMenuAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<NavDrawerItem> items;
    private Typeface tf;

    public SlideMenuAdapter(Activity context, List<NavDrawerItem> items) {
        this.context = context;
        this.items = items;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<NavDrawerItem> listaHijos = items.get(groupPosition).getHijos();
        return listaHijos.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {

        NavDrawerItem item = (NavDrawerItem) getChild(groupPosition, childPosition);
        String title = item.getTitulo();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layoutId = R.layout.drawer_list_item_child;
            convertView = inflater.inflate(layoutId, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.titulo);
        txtTitle.setText(title);
        txtTitle.setTypeface(tf);

        // Se setea el color del titulo.
        LinearLayout menuLateralItem = (LinearLayout) convertView.findViewById(R.id.menu_lateral_item_content);
        View marcador = (View) convertView.findViewById(R.id.marcador);
        if (!item.isColapsable() && item.isFragment() && NavDrawerItem.padreSeleccionado.equals(groupPosition)
                && NavDrawerItem.hijoSeleccionado.equals(childPosition)) {
            //txtTitle.setTextColor(context.getResources().getColor(R.color.cyan500));
            menuLateralItem.setBackgroundColor(context.getResources().getColor(R.color.menu_lateral_select));
            marcador.setVisibility(View.VISIBLE);
        } else {
            //txtTitle.setTextColor(context.getResources().getColor(R.color.titulo_primario_lista));
            menuLateralItem.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            marcador.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return items.get(groupPosition).getHijos().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        // Item seleccionado.
        NavDrawerItem item = (NavDrawerItem) getGroup(groupPosition);
        String _titulo = item.getTitulo();
        String _iconoIzquierdo = item.getIconoIzquierdo();

        // Si el view es null, entonces se infla.
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layoutId = R.layout.drawer_list_item;
            convertView = li.inflate(layoutId, null);
        }

        // Se setean los valores por defecto.
        ImageView iconoIzquierdo = (ImageView) convertView.findViewById(R.id.iconoIzquierdo);
        TextView titulo = (TextView) convertView.findViewById(R.id.titulo);
        ImageView iconoColapsable = (ImageView) convertView.findViewById(R.id.iconoColapsable);
        titulo.setText(_titulo);
        titulo.setTypeface(tf);
        iconoColapsable.setImageResource(R.drawable.ic_transparent);
        int idRes = context.getResources().getIdentifier(_iconoIzquierdo, "drawable", context.getPackageName());
        iconoIzquierdo.setImageResource(idRes);

        // Si el item posee hijos se setea un icono en el lado derecho.
        if (isExpanded && item.isColapsable()) {
            // Si el item esta colapsado el item se anhade su icono a la derecha.
            iconoColapsable.setImageResource(R.drawable.expand_less);
        } else if (item.isColapsable()) {
            // Si el item no esta colapsado solo se anhade su icono a la derecha.
            iconoColapsable.setImageResource(R.drawable.expand_more);
        }

        // Se setea el color del icono y del titulo.
        LinearLayout menuLateralItem = (LinearLayout) convertView.findViewById(R.id.menu_lateral_item_content);
        View marcador = (View) convertView.findViewById(R.id.marcador);
        if (!item.isColapsable() && item.isFragment() && NavDrawerItem.padreSeleccionado.equals(groupPosition)
                && NavDrawerItem.hijoSeleccionado.equals(-1)) {
            //idRes = context.getResources().getIdentifier(_iconoIzquierdo + "_cyan500", "drawable", context.getPackageName());
            //iconoIzquierdo.setImageResource(idRes);
            //titulo.setTextColor(context.getResources().getColor(R.color.cyan500));
            menuLateralItem.setBackgroundColor(context.getResources().getColor(R.color.menu_lateral_select));
            marcador.setVisibility(View.VISIBLE);
        } else {
            //titulo.setTextColor(context.getResources().getColor(R.color.titulo_primario_lista));
            menuLateralItem.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            marcador.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
