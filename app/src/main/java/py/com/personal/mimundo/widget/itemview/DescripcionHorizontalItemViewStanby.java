package py.com.personal.mimundo.widget.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.widget.item.DescripcionHorizontalItemStanby;
import py.com.personal.mimundo.widget.item.ItemStanby;

/**
 * Created by Konecta on 7/23/2014.
 */
public class DescripcionHorizontalItemViewStanby extends LinearLayout implements ItemViewStanby {

    private TextView mDescripcion1View;
    private TextView mDescripcion2View;

    public DescripcionHorizontalItemViewStanby(Context context) {
        this(context, null);
    }

    public DescripcionHorizontalItemViewStanby(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void prepareItemView() {
        mDescripcion1View = (TextView) findViewById(R.id.mm_descripcion1);
        mDescripcion2View = (TextView) findViewById(R.id.mm_descripcion2);
    }

    public void setObject(ItemStanby object) {
        final DescripcionHorizontalItemStanby item = (DescripcionHorizontalItemStanby) object;
        mDescripcion1View.setText(item.descripcion1);
        mDescripcion2View.setText(item.descripcion2);
    }

}