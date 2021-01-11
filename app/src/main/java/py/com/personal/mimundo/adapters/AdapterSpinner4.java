package py.com.personal.mimundo.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.services.personas.models.TipoDocumento;

/**
 * Created by Konecta on 29/10/2015.
 */
public class AdapterSpinner4 extends BaseAdapter {

    private int mDotSize;
    private boolean mTopLevel;
    private ArrayList<ItemDeSpinner> mItems = new ArrayList<ItemDeSpinner>();
    private Activity context;
    private Typeface tf;

    public AdapterSpinner4(boolean mTopLevel, Activity context) {
        this.mTopLevel = mTopLevel;
        this.context = context;
        if (context != null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
        }
    }

    public void clear() {
        mItems.clear();
    }

    public void addItem(String tag, String tipoDocumento, boolean indented, int color) {
        mItems.add(new ItemDeSpinner(false, tag, tipoDocumento, indented, color));
    }

    public void addItem(TipoDocumento tipoDocumento) {
        mItems.add(new ItemDeSpinner(false, "", tipoDocumento.getDescripcion(), true, 0));
    }

    public void addItem(String item) {
        mItems.add(new ItemDeSpinner(false, "", item, true, 0));
    }

    public void addHeader(String title) {
        mItems.add(new ItemDeSpinner(true, "", title, false, 0));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean isHeader(int position) {
        return position >= 0 && position < mItems.size() && mItems.get(position).esEncabezado;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = context.getLayoutInflater().inflate(R.layout.explore_spinner_item_dropdown_2, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView headerTextView = (TextView) view.findViewById(R.id.header_text);
        headerTextView.setTypeface(tf);
        TextView normalTextView = (TextView) view.findViewById(R.id.normal_text);
        normalTextView.setTypeface(tf);

        if (isHeader(position)) {
            headerTextView.setText(getTitle(position));
            headerTextView.setVisibility(View.VISIBLE);
            normalTextView.setVisibility(View.GONE);
        } else {
            headerTextView.setVisibility(View.GONE);
            normalTextView.setVisibility(View.VISIBLE);
            setUpNormalDropdownView(position, normalTextView);
        }

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(mTopLevel ? R.layout.spinner_item_edit_4 : R.layout.explore_spinner_item, container, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setTypeface(tf);
        textView.setText(getTitle(position));
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < mItems.size() ? mItems.get(position).titulo : "";
    }


    private int getColor(int position) {
        return position >= 0 && position < mItems.size() ? mItems.get(position).color : 0;
    }

    private String getTag(int position) {
        return position >= 0 && position < mItems.size() ? mItems.get(position).tag : "";
    }

    private void setUpNormalDropdownView(int position, TextView textView) {
        textView.setText(getTitle(position));
        textView.setTypeface(tf);
        ShapeDrawable colorDrawable = (ShapeDrawable) textView.getCompoundDrawables()[2];
        int color = getColor(position);
        if (color == 0) {
            if (colorDrawable != null) {
                textView.setCompoundDrawables(null, null, null, null);
            }
        } else {
            if (mDotSize == 0) {
                mDotSize = context.getResources().getDimensionPixelSize(
                        R.dimen.tag_color_dot_size);
            }
            if (colorDrawable == null) {
                colorDrawable = new ShapeDrawable(new OvalShape());
                colorDrawable.setIntrinsicWidth(mDotSize);
                colorDrawable.setIntrinsicHeight(mDotSize);
                colorDrawable.getPaint().setStyle(Paint.Style.FILL);
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, colorDrawable, null);
            }
            colorDrawable.getPaint().setColor(color);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return !isHeader(position);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    private class ItemDeSpinner {

        boolean esEncabezado;
        String tag, titulo;
        int color;
        boolean seleccionable;

        ItemDeSpinner(boolean isHeader, String tag, String title, boolean indented, int color) {
            this.esEncabezado = isHeader;
            this.tag = tag;
            this.titulo = title;
            this.seleccionable = indented;
            this.color = color;
        }
    }
}
