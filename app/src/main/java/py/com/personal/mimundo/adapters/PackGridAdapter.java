package py.com.personal.mimundo.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import py.com.personal.mimundo.activities.R;

/**
 * Created by Konecta on 06/01/2016.
 */
public class PackGridAdapter extends BaseAdapter {

    private Context context;
    private List<String> items;
    private Typeface tf;

    public PackGridAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {
            gridView = inflater.inflate( R.layout.item_single_text , null);
        } else {
            gridView = convertView;
        }

        String item = items.get(position);
        TextView textoView = (TextView) gridView.findViewById(R.id.textView1);
        textoView.setText(item);
        textoView.setTypeface(tf);

        return gridView;
    }
}
