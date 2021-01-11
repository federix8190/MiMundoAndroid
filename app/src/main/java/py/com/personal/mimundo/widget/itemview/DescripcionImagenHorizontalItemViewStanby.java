package py.com.personal.mimundo.widget.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.widget.item.DescripcionImagenHorizontalItemStanby;
import py.com.personal.mimundo.widget.item.ItemStanby;

/**
 * Created by Konecta on 7/23/2014.
 */
public class DescripcionImagenHorizontalItemViewStanby extends LinearLayout implements ItemViewStanby {

    private TextView mDescripcion1View;
    private ImageView mImageView;

    public DescripcionImagenHorizontalItemViewStanby(Context context) {
        this(context, null);
    }

    public DescripcionImagenHorizontalItemViewStanby(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void prepareItemView() {
        mDescripcion1View = (TextView) findViewById(R.id.mm_descripcion1);
        mImageView = (ImageView) findViewById(R.id.mm_imagen1);
    }

    public void setObject(ItemStanby object) {
        final DescripcionImagenHorizontalItemStanby item = (DescripcionImagenHorizontalItemStanby) object;
        mDescripcion1View.setText(item.descripcion1);
        //Esto no se si funca
        mImageView.setImageResource(item.imagen1);
    }

}