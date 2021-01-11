package py.com.personal.mimundo.widget.item;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.widget.itemview.ItemViewStanby;

/**
 * Created by Konecta on 7/23/2014.
 */
public class DescripcionHorizontalItemStanby extends ItemStanby {

    /**
     * The item's descripcion1.
     */
    public String descripcion1;

    /**
     * The item's descripcion2.
     */
    public String descripcion2;

    /**
     * @hide
     */
    public DescripcionHorizontalItemStanby() {
    }

    /**
     * Create a new TextItem with the specified text.
     *
     * @param descripcion1 The text used to create this item.
     */
    public DescripcionHorizontalItemStanby(String descripcion1, String descripcion2) {
        this.descripcion1 = descripcion1;
        this.descripcion2 = descripcion2;
    }

    @Override
    public ItemViewStanby newView(Context context, ViewGroup parent) {
        return createCellFromXml(context, R.layout.item_view_descripcion_horizontal_stanby, parent);
    }

    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs);

        /*TypedArray a = r.obtainAttributes(attrs, R.styleable.TextItem);
        descripcion1 = a.getString(R.styleable.TextItem_descripcion1);
        a.recycle();

        TypedArray b = r.obtainAttributes(attrs, R.styleable.SubtitleItem);
        descripcion2 = b.getString(R.styleable.SubtitleItem_descripcion2);
        b.recycle();*/
    }

}