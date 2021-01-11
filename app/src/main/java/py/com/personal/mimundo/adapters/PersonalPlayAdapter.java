package py.com.personal.mimundo.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import py.com.personal.mimundo.activities.R;

/**
 * Created by Konecta on 15/10/2015.
 */
public class PersonalPlayAdapter extends BaseAdapter {

    private Context context;
    private List<String> listaHeaders;
    private List<String> listaFooters;
    private List<Integer> listaImagenes;
    private List<Integer> imagenesFooter;
    private Typeface tf;

    public PersonalPlayAdapter(Context context, List<String> listaHeaders, List<String> listaFooters, List<Integer> listaImagenes, List<Integer> imagenesFooter) {
        this.context = context;
        this.listaHeaders = listaHeaders;
        this.listaFooters = listaFooters;
        this.listaImagenes = listaImagenes;
        this.imagenesFooter = imagenesFooter;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public int getCount() {
        // Number of times getView method call depends upon gridValues.length
        return listaHeaders.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    // Number of times getView method call depends upon gridValues.length

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {
            gridView = inflater.inflate(R.layout.item_personal_play, null);
        } else {
            gridView = convertView;
        }

        String header = listaHeaders.get(position);
        String footer = listaFooters.get(position);
        Integer imagen = listaImagenes.get(position);
        Integer imagenFooter = imagenesFooter.get(position);

        ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView1);
        imageView.setImageResource(imagen);

        TextView headerView = (TextView) gridView.findViewById(R.id.textView1);
        headerView.setText(header);
        headerView.setTypeface(tf);

        TextView footerView = (TextView) gridView.findViewById(R.id.textView2);
        footerView.setText(footer);
        footerView.setTypeface(tf);

        ImageView imagenFooterView = (ImageView) gridView.findViewById(R.id.imagen_footer);
        imagenFooterView.setImageResource(imagenFooter);


        return gridView;
    }
}
