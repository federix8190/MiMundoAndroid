package py.com.personal.mimundo.fragments.configuracion;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.fragments.configuracion.SuspensionTemporalAdapter.TipoSuspension;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.HomeActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.Constantes;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.lineas.service.SuspenderLineaRequest;
import py.com.personal.mimundo.services.lineas.service.SuspensionVoluntaria;
import py.com.personal.mimundo.services.cache.lineas.ObtenerLineaRequest;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.models.OpcionSuspension;
import py.com.personal.mimundo.utils.ListUtils;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 06/10/2014.
 */
public class SuspencionTemporalFragment extends Fragment {

    private String linea;
    public String tipoSuspension;
    private Linea datoslinea;
    private ObtenerLineaRequest lineasRequest;
    private SuspenderLineaRequest suspenderLineaRequest;
    private SuspensionVoluntaria suspensionVoluntaria;
    private ProgressDialog progress;
    private Button restituirButton;
    private Button suspenderLineaButton;
    private Typeface tf1;

    // Elementos del redisenho
    private ListView listSuspensionesView;
    private List<TipoSuspension> listaSuspenciones;
    private SuspensionTemporalAdapter aa;
    private View formularioSuspencion;
    private View formularioRestitucion;
    private View contenedorProgress;

    // Boton de modificar datos.
    //View contenedor_boton_suspender;
    //View contenedor_boton_restituir;

    public static SuspencionTemporalFragment newInstance() {
        return new SuspencionTemporalFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonLinea = savedInstanceState.getString("datoslinea");
            datoslinea = new Gson().fromJson(jsonLinea, Linea.class);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("datoslinea", new Gson().toJson(datoslinea));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_suspension_temporal, container, false);

        //contenedor_boton_suspender = v.findViewById(R.id.contenedor_boton_suspender);
        //contenedor_boton_restituir = v.findViewById(R.id.contenedor_boton_restituir);
        tipoSuspension = Constantes.SUSPENDER_LINEA_SINIESTRO;

        tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.titulo_estado)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.motivos_suspension_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.titulo_estado_1)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.restitucion_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.titulo_info)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.descripcion_info)).setTypeface(tf1);

        listSuspensionesView = (ListView)v.findViewById(R.id.contenedor_opciones);
        listaSuspenciones =  new ArrayList<>();
        listaSuspenciones.add(new TipoSuspension(R.drawable.ic_help,
                getActivity().getResources().getString(R.string.descripcion_suspension_extravio),
                true, Constantes.SUSPENDER_LINEA_SINIESTRO));
        listaSuspenciones.add(new TipoSuspension(R.drawable.ic_directions_walk,
                getActivity().getResources().getString(R.string.descripcion_suspension_voluntaria),
                false, Constantes.SUSPENSION_VOLUNTARIA));
        listaSuspenciones.add(new TipoSuspension(R.drawable.ic_report_problem,
                getActivity().getResources().getString(R.string.descripcion_suspension_robo),
                false, Constantes.SUSPENDER_LINEA_ROBO));

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TipoSuspension item = listaSuspenciones.get(i);
                switch (i) {
                    case 0 :
                        tipoSuspension = Constantes.SUSPENDER_LINEA_SINIESTRO;
                        break;
                    case 1 :
                        tipoSuspension = Constantes.SUSPENSION_VOLUNTARIA;
                        break;
                    case 2 :
                        tipoSuspension = Constantes.SUSPENDER_LINEA_ROBO;
                        break;
                }
                if (!item.isCheckeado()) {
                    for (TipoSuspension suspension : listaSuspenciones) {
                        suspension.setCheckeado(false);
                    }
                    item.setCheckeado(true);
                    aa.notifyDataSetChanged();
                }
            }
        };
        listSuspensionesView.setOnItemClickListener(listener);
        aa = new SuspensionTemporalAdapter(getActivity(), listaSuspenciones);
        listSuspensionesView.setAdapter(aa);
        aa.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listSuspensionesView, 0);

        // Referencias a formularios.
        formularioSuspencion = v.findViewById(R.id.servicio_suspencion_desactivado);
        formularioRestitucion = v.findViewById(R.id.servicio_suspencion_activado);
        contenedorProgress = v.findViewById(R.id.progressbar);
        showProgress(true);

        View.OnClickListener suspenderListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!esClaseComercialValida()) {
                    return;
                }
//                DialogFragment dialogo = crearDialogo();
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            suspenderLinea();
                    }
                };
                DialogInterface.OnClickListener rechazarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if (suspenderLineaButton.getText().equals(getResources().getString(R.string.texto_boton_suspender))) {
                    builder.setMessage(getResources().getString(R.string.confirmar_suspender_linea));
                } else {
                    builder.setMessage(getResources().getString(R.string.confirmar_reactivar_linea));
                }
                builder.setTitle(getResources().getString(R.string.confirmar_title));
                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
                builder.setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar), rechazarListener);
                AlertDialog dialog = builder.create();
                HomeActivity activity = (HomeActivity) getActivity();
                if (tipoSuspension == null || tipoSuspension.isEmpty()) {
                    Toast.makeText(activity, MensajesDeUsuario.ERROR_TIPO_SUSPENSION, Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                }
            }
        };
        suspenderLineaButton = (Button) v.findViewById(R.id.suspender_action);
        suspenderLineaButton.setOnClickListener(suspenderListener);
        suspenderLineaButton.setTypeface(tf1);

        View.OnClickListener restituirListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DialogFragment dialogo = crearDialogo(getResources().getString(R.string.reactivar_btn));
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reactivarLinea();
                    }
                };
                DialogInterface.OnClickListener rechazarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if (suspenderLineaButton.getText().equals(getResources().getString(R.string.texto_boton_suspender))) {
                    builder.setMessage(getResources().getString(R.string.confirmar_suspender_linea));
                } else {
                    builder.setMessage(getResources().getString(R.string.confirmar_reactivar_linea));
                }
                builder.setTitle(getResources().getString(R.string.confirmar_title));
                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
                builder.setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar), rechazarListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        restituirButton = (Button) v.findViewById(R.id.restituir_linea);
        restituirButton.setOnClickListener(restituirListener);
        restituirButton.setTypeface(tf1);

        linea = ((BaseDrawerActivity) getActivity()).obtenerLineaSeleccionada();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        lineasRequest = new ObtenerLineaRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (datoslinea == null) {
            activity.getSpiceManager().execute(lineasRequest.datosLinea(linea),
                    new DatosLineaRequestListener());
        } else {
            showProgress(false);
            cargarDatos(datoslinea);
        }
    }

//    private DialogFragment crearDialogo(final String operacion) {
//        return new DialogFragment() {
//            @Override
//            public Dialog onCreateDialog(Bundle savedInstanceState) {
//                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        if (operacion.equals(getResources().getString(R.string.texto_boton_suspender))) {
//                            suspenderLinea();
//                        } else {
//                            reactivarLinea();
//                        }
//                    }
//                };
//                DialogInterface.OnClickListener rechazarListener = new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                };
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                if (suspenderLineaButton.getText().equals(getResources().getString(R.string.texto_boton_suspender))) {
//                    builder.setMessage(getResources().getString(R.string.confirmar_suspender_linea));
//                } else {
//                    builder.setMessage(getResources().getString(R.string.confirmar_reactivar_linea));
//                }
//                builder.setTitle(getResources().getString(R.string.confirmar_title));
//                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
//                builder.setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar), rechazarListener);
//                AlertDialog dialog = builder.create();
//                return dialog;
//            }
//        };
//    }

    // Validaciones la clase comercial de la linea
    private boolean esClaseComercialValida() {
        String claseComercial = datoslinea.getClaseComercial();
        if (!claseComercial.equals("POSPA") && !claseComercial.equals("PCTRL")) {
            DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.error_title));
            builder.setMessage(getResources().getString(R.string.error_suspension));
            builder.setNeutralButton("OK", aceptarListener);
            AlertDialog dialog = builder.create();
            dialog.show();
//            DialogFragment dialogError = new DialogFragment() {
//                @Override
//                public Dialog onCreateDialog(Bundle savedInstanceState) {
//                    DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    };
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setTitle(getResources().getString(R.string.error_title));
//                    builder.setMessage(getResources().getString(R.string.error_suspension));
//                    builder.setNeutralButton("OK", aceptarListener);
//                    AlertDialog dialog = builder.create();
//                    return dialog;
//                }
//            };
//            dialogError.show(getActivity().getSupportFragmentManager(), "Reactivar");
            return false;
        }
        return true;
    }

    private void suspenderLinea() {

        HomeActivity activity = (HomeActivity) getActivity();
        linea = activity.obtenerLineaSeleccionada();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        String title = MensajesDeUsuario.TITULO_DIALOGO_SUSPENDER_LINEA;
        String msg = MensajesDeUsuario.MENSAJE_DIALOGO_SUSPENDER_LINEA;
        progress = ProgressDialog.show(activity, title, msg, true);

        try {
            String msgExito = MensajesDeUsuario.MENSAJE_SUSPENSION_EXITOSA;
            String msgError = MensajesDeUsuario.ERROR_SUSPENDER_LINEA;
            if (tipoSuspension.equals(Constantes.SUSPENDER_LINEA_SINIESTRO)
                    || tipoSuspension.equals(Constantes.SUSPENDER_LINEA_ROBO)) {
                suspenderLineaRequest = new SuspenderLineaRequest(activity, new JacksonConverter(objectMapper));
                activity.getSpiceManager().execute(
                        suspenderLineaRequest.ejecutar(linea, tipoSuspension, new OpcionSuspension()),
                        new OperacionRequestListener(msgExito, msgError)
                );
            } else if (tipoSuspension.equals(Constantes.SUSPENSION_VOLUNTARIA)) {
                suspensionVoluntaria = new SuspensionVoluntaria(activity, new JacksonConverter(objectMapper));
                activity.getSpiceManager().execute(
                        suspensionVoluntaria.suspender(linea),
                        new OperacionRequestListener(msgExito, msgError)
                );
            }
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void reactivarLinea() {
        HomeActivity activity = (HomeActivity) getActivity();
        linea = activity.obtenerLineaSeleccionada();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        String title = MensajesDeUsuario.TITULO_DIALOGO_RESTITUIR_LINEA;
        String msg = MensajesDeUsuario.MENSAJE_DIALOGO_SUSPENDER_LINEA;
        progress = ProgressDialog.show(activity, title, msg, true);
        try {
            suspensionVoluntaria = new SuspensionVoluntaria(activity, new JacksonConverter(objectMapper));
            String msgExito = MensajesDeUsuario.MENSAJE_RESTITUCION_EXITOSA;
            String msgError = MensajesDeUsuario.ERROR_RESTITUIR_LINEA;
            activity.getSpiceManager().execute(
                    suspensionVoluntaria.restituir(linea),
                    new OperacionRequestListener(msgExito, msgError)
            );
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public final class DatosLineaRequestListener implements RequestListener<Linea> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if(getActivity() != null) {
                showProgress(false);
                Toast.makeText(getActivity(), MensajesDeUsuario.SIN_DATOS_LINEA, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Linea datos) {
            Activity context = getActivity();
            if(context != null) {
                showProgress(false);
                if (datos != null) {
                    datoslinea = datos;
                    cargarDatos(datos);
                } else {
                    Toast.makeText(context, MensajesDeUsuario.SIN_DATOS_LINEA, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cargarDatos(Linea datos) {
        String estado = datos.getCodigoEstadoLinea();
        if (estado != null && estado.equals(Constantes.ESTADO_LINEA_ACTIVA)) {
            formularioSuspencion.setVisibility(View.VISIBLE);
            suspenderLineaButton.setVisibility(View.VISIBLE);
            ((TextView) getView().findViewById(R.id.activado)).setTypeface(tf1);
        } else {
            formularioRestitucion.setVisibility(View.VISIBLE);
            restituirButton.setVisibility(View.VISIBLE);
            ((TextView) getView().findViewById(R.id.desactivado)).setTypeface(tf1);
        }
    }

    public final class OperacionRequestListener implements RequestListener<Response> {

        String msgExito;
        String msgError;

        public OperacionRequestListener(String msgExito, String msgError) {
            this.msgExito = msgExito;
            this.msgError = msgError;
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();
                Toast.makeText(getActivity(), msgError, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Response response) {
            if (getActivity() != null) {
                progress.dismiss();
                if (response != null) {
                    DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(msgExito).setTitle(MensajesDeUsuario.OPERACION_EXITOSA);
                    builder.setNeutralButton("OK", aceptarListener);
                    AlertDialog dialog = builder.create();
                    dialog.show();
//                    DialogFragment dialogo = new DialogFragment() {
//                        @Override
//                        public Dialog onCreateDialog(Bundle savedInstanceState) {
//                            DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                }
//                            };
//                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                            builder.setMessage(msgExito).setTitle(MensajesDeUsuario.OPERACION_EXITOSA);
//                            builder.setNeutralButton("OK", aceptarListener);
//                            AlertDialog dialog = builder.create();
//                            return dialog;
//                        }
//                    };
//                    dialogo.show(getActivity().getSupportFragmentManager(), "Suspension");
                } else {
                    Toast.makeText(getActivity(), msgError, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void showProgress(final boolean show) {
        // The ViewPropertyAnimator APIs are not available, so simply show and hide the relevant UI components.
        contenedorProgress.setVisibility(show? View.VISIBLE : View.GONE);
    }
}
