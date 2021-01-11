package py.com.personal.mimundo.adapters;

import java.util.List;

import py.com.personal.mimundo.activities.R;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ListItemAdapter extends ArrayAdapter<String> {

    private int resource;
    private Typeface tf;

    public ListItemAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.resource = resource;
        if (context != null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout listItemView;
        String item = getItem(position);
        if (convertView == null) {
            listItemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, listItemView, true);
        } else {
            listItemView = (LinearLayout) convertView;
        }
        TextView itemView = (TextView) listItemView.findViewById(R.id.rowList);
        itemView.setText(item);
        if (tf != null) {
            itemView.setTypeface(tf);
        }
        return listItemView;
    }
}
