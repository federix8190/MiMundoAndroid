package py.com.personal.mimundo.fragments.gestion.facturacion;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.grupos.models.DetalleFactura;
import py.com.personal.mimundo.utils.NumbersUtils;

/**
 * Created by Usuario on 9/17/2014.
 */
public class ResumenesFacturaAdapter extends ArrayAdapter<ResumenFactura> {

    public static final String CODIGO_SIN_DATO = "sinDato";
    public static final String CODIGO_LOADING = "loading";
    public static final String CODIGO_SIN_SERVICIO = "sinServicio";

    private final static int RESOURCE_LAYOUT = R.layout.item_resumen_factura ;
    private Typeface tf;

    public ResumenesFacturaAdapter(Context context, List<ResumenFactura> items) {
        super(context, RESOURCE_LAYOUT, items);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout listItemView;
        ResumenFactura item = getItem(position);

        if (convertView == null) {
            listItemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(RESOURCE_LAYOUT, listItemView, true);
        } else {
            listItemView = (LinearLayout) convertView;
        }

        if (item.isHeader()) {
            listItemView.findViewById(R.id.cabecera).setVisibility(View.VISIBLE);
            listItemView.findViewById(R.id.detalle).setVisibility(View.GONE);
            ((TextView) listItemView.findViewById(R.id.nro_linea)).setText("Resumen: " + item.getNroLinea());
            ((TextView) listItemView.findViewById(R.id.nro_linea)).setTypeface(tf);
        } else {
            listItemView.findViewById(R.id.cabecera).setVisibility(View.GONE);
            listItemView.findViewById(R.id.detalle).setVisibility(View.VISIBLE);
            String titulo;
            String descripcion;
            if (item.getDatos().getDescripcion().equals(CODIGO_SIN_DATO)) {
                titulo = MensajesDeUsuario.TITULO_SIN_DATOS;
                descripcion = MensajesDeUsuario.MENSAJE_SIN_DATOS;
            } else if (item.getDatos().getDescripcion().equals(CODIGO_LOADING)) {
                titulo = MensajesDeUsuario.TITULO_LOADING;
                descripcion = MensajesDeUsuario.MENSAJE_LOADING;
            } else if (item.getDatos().getDescripcion().equals(CODIGO_SIN_SERVICIO)) {
                titulo = MensajesDeUsuario.TITULO_SIN_SERVICIO;
                descripcion = MensajesDeUsuario.MENSAJE_SIN_SERVICIO;
            } else {
                titulo = item.getDatos().getDescripcion() == null ? MensajesDeUsuario.MENSAJE_NULL : item.getDatos().getDescripcion();
                descripcion = obtenerDescripcion(item.getDatos().getMontoTotal());
            }

            TextView tituloView = (TextView) listItemView.findViewById(R.id.titulo);
            tituloView.setTypeface(tf);
            tituloView.setText(titulo);
            TextView descripcionView = (TextView) listItemView.findViewById(R.id.descripcion);
            descripcionView.setTypeface(tf);
            descripcionView.setText(descripcion);
        }

        return listItemView;
    }

    private String obtenerDescripcion(Double monto) {
        if (monto == null) {
            return MensajesDeUsuario.MENSAJE_NULL;
        } else {
            return NumbersUtils.formatear(monto.longValue()) + " Gs.";
        }
    }

}
