package py.com.personal.mimundo.fragments.saldo.packs;

import android.graphics.Typeface;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.lineas.packs.AcreditarPack;
import py.com.personal.mimundo.services.lineas.packs.OperacionPack;
import py.com.personal.mimundo.services.lineas.service.AcreditarPacksRequest;
import py.com.personal.mimundo.services.lineas.packs.DuracionUnidad;
import py.com.personal.mimundo.services.lineas.packs.Pack;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 17/09/2014.
 */
public class ListaPacksAdapter extends RecyclerView.Adapter<ListaPacksAdapter.ViewHolder> {

    private AcreditarPacksRequest acreditarPacksRequest;
    private ProgressDialog progress;
    private String lineaBeneficiaria;
    private Typeface tf;
    public static final int LISTA_PACKS_ADAPTER_CREAR_DIALOG_1 = 1;
    public static final int LISTA_PACKS_ADAPTER_CREAR_DIALOG_2 = 2;

    private BaseDrawerActivity mContext;
    private List<Pack> mDataset;
    private String linea;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView relativeLayout;

        public ViewHolder(CardView v) {
            super(v);
            relativeLayout = v;
        }
    }

    public ListaPacksAdapter(BaseDrawerActivity context, String linea, List<Pack> mDataset) {
        this.mDataset = mDataset;
        this.mContext = context;
        this.linea = linea;
        if (context != null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Platform-Regular.otf");
        }
    }

    public void addItem(Pack pack) {
        mDataset.add(pack);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDataset.clear();
    }

    @Override
    public ListaPacksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_pack, parent, false);
        ListaPacksAdapter.ViewHolder vh = new ListaPacksAdapter.ViewHolder((CardView) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListaPacksAdapter.ViewHolder holder, int position) {

        final Pack datosPack = mDataset.get(position);

        TextView costoTextView = (TextView) holder.relativeLayout.findViewById(R.id.costo);
        TextView descripcionTextView = (TextView) holder.relativeLayout.findViewById(R.id.descripcion);
        TextView vigenciaTextView = (TextView) holder.relativeLayout.findViewById(R.id.vigencia);

        costoTextView.setTypeface(tf);
        descripcionTextView.setTypeface(tf);
        vigenciaTextView.setTypeface(tf);

        // Vigencia y costo
        DecimalFormat formateador = new DecimalFormat("###,###");
        String costo = formateador.format((int) datosPack.getMonto());
        costo = costo.replace(',', '.');
        costoTextView.setText("Gs. " + costo);
        descripcionTextView.setText(datosPack.getDescripcion());
        String unidadTiempo = datosPack.getDuracionUnidad().equals(DuracionUnidad.DIA) ? " Día" : " Hora";
        float duracion = datosPack.getDuracion();
        if (duracion > 1) {
            unidadTiempo += "s";
        }
        String vigencia = duracion + unidadTiempo;
        if (duracion - (int) duracion == 0.0) {
            vigencia = ((int) duracion) + unidadTiempo;
        }
        vigenciaTextView.setText("Vigencia " + vigencia);

        // Operaciones del Pack
        OperacionPack[] operaciones = datosPack.getOperaciones();
        if (operaciones == null || operaciones.length == 0) {
            return;
        }

        boolean tieneActivacion = false;
        boolean tieneSuscripcion = false;
        boolean tieneRegalo = false;
        boolean tieneReactivar = false;
        for (int i = 0; i < operaciones.length; i++) {
            OperacionPack operacionPack = operaciones[i];
            if (operacionPack != null && operacionPack.getCodigo() != null) {
                if (operacionPack.getCodigo().equals(OperacionPack.ACT)) {// && operacionPack.getCardinalidad() == 0) {
                    tieneActivacion = true;
                }
                if (operacionPack.getCodigo().equals(OperacionPack.REG)) {// && operacionPack.getCardinalidad() == 1) {
                    tieneRegalo = true;
                }
                if (operacionPack.getCodigo().equals(OperacionPack.SUS)) {
                    tieneSuscripcion = true;
                }
                if (operacionPack.getCodigo().equals(OperacionPack.REA)) {
                    tieneReactivar = true;
                }
            }
        }

        if (tieneActivacion) {
            System.err.println("Pack Tiene Activar : " + datosPack.getDescripcion());
            String titulo = mContext.getResources().getString(R.string.acreditar_packs_title);
            String msgExitoso = MensajesDeUsuario.COMPRA_PACK_EXITOSA + linea;
            String msgError = MensajesDeUsuario.ERROR_ACREDITAR_PACK;
            String codPack = datosPack.getCodigo();
            String tituloDialog = MensajesDeUsuario.COMPRA_PACK_DIALOGO_TITULO + " - Código: " + codPack;
            View.OnClickListener activarListener = new MyOnClickListener(linea, codPack, "ACT", titulo, msgExitoso, msgError, tituloDialog, false);
            Button activarPackButton = (Button) holder.relativeLayout.findViewById(R.id.button_activar_pack);
            activarPackButton.setVisibility(View.VISIBLE);
            activarPackButton.setOnClickListener(activarListener);
            activarPackButton.setTypeface(tf);
        }

        if (tieneRegalo) {
            System.err.println("Pack Tiene Reactivar : " + datosPack.getDescripcion());
            String titulo = mContext.getResources().getString(R.string.regalar_packs_title);
            String msgExitoso = MensajesDeUsuario.REGALO_PACK_EXITOSA;
            String msgError = MensajesDeUsuario.ERROR_REGALAR_PACK;
            String tituloDialog = MensajesDeUsuario.REGALAR_PACK_DIALOGO_TITULO;
            String codPack = datosPack.getCodigo();
            View.OnClickListener regalarListener = new MyOnClickListener(linea, codPack, "ACT", titulo, msgExitoso, msgError, tituloDialog, true);
            Button regalarPackButton = (Button) holder.relativeLayout.findViewById(R.id.button_regalar_pack);
            regalarPackButton.setVisibility(View.VISIBLE);
            regalarPackButton.setOnClickListener(regalarListener);
            regalarPackButton.setTypeface(tf);
        }

        if (tieneSuscripcion) {
            System.err.println("Pack Tiene Suscripcion : " + datosPack.getDescripcion());
            String titulo = mContext.getResources().getString(R.string.suscripcion_packs_title);
            String msgExitoso = MensajesDeUsuario.SUSCRIBIR_PACK_EXITOSA;
            String msgError = MensajesDeUsuario.ERROR_SUSCRIBIR_PACK;
            String tituloDialog = MensajesDeUsuario.SUSCRIBIR_PACK_DIALOGO_TITULO;
            String codPack = datosPack.getCodigo();
            View.OnClickListener suscribirListener = new MyOnClickListener(linea, codPack, "SUS", titulo, msgExitoso, msgError, tituloDialog, false);
            Button suscribirPackButton = (Button) holder.relativeLayout.findViewById(R.id.button_suscribir_pack);
            suscribirPackButton.setVisibility(View.VISIBLE);
            suscribirPackButton.setOnClickListener(suscribirListener);
            suscribirPackButton.setTypeface(tf);
        }

        if (tieneReactivar) {
            System.err.println("Pack Tiene Suscripcion : " + datosPack.getDescripcion());
            String titulo = "Reactivar";
            String msgExitoso = MensajesDeUsuario.SUSCRIBIR_PACK_EXITOSA;
            String msgError = MensajesDeUsuario.ERROR_SUSCRIBIR_PACK;
            String tituloDialog = MensajesDeUsuario.SUSCRIBIR_PACK_DIALOGO_TITULO;
            String codPack = datosPack.getCodigo();
            View.OnClickListener suscribirListener = new MyOnClickListener(linea, codPack, "REA", titulo, msgExitoso, msgError, tituloDialog, false);
            Button suscribirPackButton = (Button) holder.relativeLayout.findViewById(R.id.button_reactivar_pack);
            suscribirPackButton.setVisibility(View.VISIBLE);
            suscribirPackButton.setOnClickListener(suscribirListener);
            suscribirPackButton.setTypeface(tf);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyOnClickListener implements View.OnClickListener {

        final static String CANAL = "ag";
        String linea;
        String codPack;
        String codOperacion;
        String titulo;
        String msgExitoso;
        String msgError;
        String tituloDialog;
        boolean esRegalo;
        String msg;

        public MyOnClickListener(String linea, String codPack, String codOperacion, String titulo,
                                 String msgExitoso, String msgError, String tituloDialog, boolean esRegalo) {
            this.linea = linea;
            this.codPack = codPack;
            this.codOperacion = codOperacion;
            this.titulo = titulo;
            this.msgExitoso = msgExitoso;
            this.msgError = msgError;
            this.tituloDialog = tituloDialog;
            this.esRegalo = esRegalo;
            this.msg = mContext.getResources().getString(R.string.ejecutando_operacion);
        }

        @Override
        public void onClick(View v) {
            //final BaseDrawerActivity activity = (BaseDrawerActivity) getContext();
            try {

                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setIcon(R.drawable.new_document);
                alertDialog.setTitle(tituloDialog);
                alertDialog.setMessage(MensajesDeUsuario.PACK_DIALOGO_MSG);
                if (esRegalo) {
                    LayoutInflater inflater = mContext.getLayoutInflater();
                    alertDialog.setView(inflater.inflate(R.layout.dialog_ingresar_linea, null));
                }

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (esRegalo) {
                                    AlertDialog d = (AlertDialog) dialog;
                                    EditText lineaEditText = (EditText) d.findViewById(R.id.numero_regalar_pack);
                                    lineaBeneficiaria = lineaEditText.getText().toString();
                                    if (lineaBeneficiaria == null || lineaBeneficiaria.equals("")) {
                                        Toast.makeText(mContext, mContext.getResources().getString(R.string.ingresar_nro_linea),
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                AcreditarPack datos = new AcreditarPack();
                                datos.setCodigoPack(codPack);
                                datos.setCodigoOperacion(codOperacion);
                                datos.setCanal(CANAL);
                                progress = ProgressDialog.show(mContext, titulo, msg, true);
                                try {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
                                    Converter converter = new JacksonConverter(objectMapper);
                                    acreditarPacksRequest = new AcreditarPacksRequest(mContext, converter);
                                    mContext.getSpiceManager().execute(
                                            acreditarPacksRequest.acreditarPacks(linea, datos),
                                            new AcreditarPacksRequestListener(msgExitoso, msgError)
                                    );
                                } catch (Exception e) {
                                    progress.dismiss();
                                    Toast.makeText(mContext, MensajesDeUsuario.OPERACION_ERROR, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void mostrarMensaje(final String titulo, final String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setIcon(R.drawable.new_document);
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public final class AcreditarPacksRequestListener implements RequestListener<Response> {

        private String msgExitoso;
        private String msgError;

        public AcreditarPacksRequestListener(String msgExitoso, String msgError) {
            this.msgExitoso = msgExitoso;
            this.msgError = msgError;
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            mostrarMensaje(mContext.getResources().getString(R.string.error_title), msgError);
            progress.dismiss();
        }

        @Override
        public void onRequestSuccess(final Response res) {
            progress.dismiss();
            if (res.getStatus() == 200) {
                final String titulo = MensajesDeUsuario.OPERACION_EXITOSA;
                mostrarMensaje(titulo, msgExitoso);
            } else {
                mostrarMensaje(mContext.getResources().getString(R.string.error_title), msgError);
            }
        }
    }
}