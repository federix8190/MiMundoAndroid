package py.com.personal.mimundo.widget.item;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.widget.itemview.ItemViewStanby;


/**
 * Clase que lista un grupo de Item en un scroll view con unos items de header y
 * otros de footer <br>
 * Cambios </br> <li>Agrega y elimina items inmediatamente</li> <li>No refresca
 * la vista, cada Item se tiene que encargar de su modificacion</li> <li>Agrega
 * una cabecera y un pie de pagina que estan compuestos de items</li>
 * <li>Se pueden mexclar varios ManyItems</li>
 *
 * @author Arturo Volpe
 *
 */
public class ManyItemsStanby {

    private static final String MANY_ITEMS_NOT_FOUND = "ManyItems no encontrado, omitiendo eliminacion";
    private static final String ITEM_NOT_FOUND = "Item no encontrado, omitiendo eliminacion";
    public static final String TAG = ManyItemsStanby.class.getSimpleName();
    /**
     * Contexto
     */
    Context context;

    /**
     * Items que ya fueron dibujados
     */
    List<ItemStanby> itemStanbies;
    /**
     * Items que fueron dibujados en el header
     */
    List<ItemStanby> header;

    /**
     * Items que fueron dibujados en el footer
     */
    List<ItemStanby> footer;

    private HashMap<ItemStanby, View> vistas;

    int ultimoHeader = 0;
    int ultimoFooter = 0;
    int ultimoMain = 0;
    int cantidadOtros = 0;

    private static final LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

    /**
     * Otros ManyItems agregados al cuerpo Sus footer y header son omitidos;
     */
    List<ManyItemsStanby> anexos;

    public ManyItemsStanby(Context context) {
        this.context = context;
        itemStanbies = new ArrayList<ItemStanby>();
        footer = new ArrayList<ItemStanby>();
        header = new ArrayList<ItemStanby>();
        anexos = new ArrayList<ManyItemsStanby>();

        LayoutInflater li = LayoutInflater.from(context);
        layout = (LinearLayout) li.inflate(R.layout.many_items_stanby, null);
        scrollView = (ScrollView) layout.findViewById(R.id.scrollView1);
        headerLayout = (LinearLayout) layout.findViewById(R.id.header);
        footerLayout = (LinearLayout) layout.findViewById(R.id.footer);
        mainLayout = (LinearLayout) layout.findViewById(R.id.main);
        headerLayout.setVisibility(View.GONE);

        vistas = new HashMap<ItemStanby, View>();
    }

    ScrollView scrollView;
    LinearLayout layout;
    LinearLayout headerLayout;
    LinearLayout footerLayout;
    LinearLayout mainLayout;

    /**
     * Los header y otras se mantienen SI se declaran dos con la misma posicion,
     * solo el ultimo queda
     *
     * @param otros
     */
    public void add(ManyItemsStanby otros) {
        otros.scrollView.removeView(otros.mainLayout);
        mainLayout.addView(otros.mainLayout);
        cantidadOtros += otros.itemStanbies.size();
        // for (Item item : otros.items) {
        // primerOtro = addToLayout(mainLayout, item, primerOtro++);
        // cantidadOtros++;
        // }
        anexos.add(otros);
    }

    /**
     * Elimina un grupo de items
     *
     * @param otros
     */
    public void remove(ManyItemsStanby otros) {
        if (!anexos.contains(otros)) {
            Log.w(TAG, MANY_ITEMS_NOT_FOUND);
            return;
        }
        mainLayout.removeView(otros.mainLayout);
        // for (Item item : otros.items) {
        // View v = vistas.get(item);
        // mainLayout.removeView(v);
        // }
        cantidadOtros -= otros.itemStanbies.size();
    }

    public void add(ItemStanby itemStanby) {
        addToLayout(mainLayout, itemStanby, ultimoMain++);
        itemStanbies.add(itemStanby);
    }

    public void remove(ItemStanby itemStanby) {
        if (!itemStanbies.remove(itemStanby)) {
            Log.w(TAG, ITEM_NOT_FOUND);
            return;
        }
        ultimoMain--;
        View v = vistas.get(itemStanby);
        if (v == null) {
            Log.w(TAG, ITEM_NOT_FOUND);
            return;
        }
        mainLayout.removeView(v);
    }

    public void addHeader(ItemStanby itemStanby) {
        addToLayout(headerLayout, itemStanby, ultimoHeader++);
        header.add(itemStanby);
    }

    public void removeHeader(ItemStanby itemStanby) {
        if (!header.remove(itemStanby)) {
            Log.w(TAG, ITEM_NOT_FOUND);
            return;
        }
        View v = vistas.get(itemStanby);
        if (v == null) {
            Log.w(TAG, ITEM_NOT_FOUND);
            return;
        }
        headerLayout.removeView(v);
    }

    public void addFooter(ItemStanby itemStanby) {
        addToLayout(footerLayout, itemStanby, ultimoFooter++);
        footer.add(itemStanby);
    }

    public void removeFooter(ItemStanby itemStanby) {
        if (!footer.remove(itemStanby)) {
            Log.w(TAG, ITEM_NOT_FOUND);
            return;
        }
        View v = vistas.get(itemStanby);
        if (v == null) {
            Log.w(TAG, ITEM_NOT_FOUND);
            return;
        }
        footerLayout.removeView(v);
    }

    public View getView() {
        return layout;
    }

    private int addToLayout(LinearLayout layout, ItemStanby itemStanby, int comienzo) {
        ItemViewStanby v = itemStanby.newView(context, null);
        v.prepareItemView();
        v.setObject(itemStanby);
        View view = (View) v;
        vistas.put(itemStanby, view);
        layout.addView(view, comienzo, lp);
        return comienzo;
    }

    public List<ItemStanby> getItemStanbies() {
        return itemStanbies;
    }

}
