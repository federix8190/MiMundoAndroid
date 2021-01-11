package py.com.personal.mimundo.fragments.configuracion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.MensajesGenericos;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptarRechazar;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptar;
import py.com.personal.mimundo.services.servicios.service.ActivacionServicios;
import py.com.personal.mimundo.services.servicios.service.ConsultarEstadosServicios;
import py.com.personal.mimundo.services.servicios.models.EstadoServicio;
import py.com.personal.mimundo.services.servicios.models.EstadoServicios;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 06/10/2014.
 */
public class RecargaContraFacturaFragment extends Fragment {

    private String linea;

    private ProgressDialog progress;
    private ActivacionServicios activacionServicios;
    private ConsultarEstadosServicios consultarEstados;
    private Typeface tf1;
    public static final int RECARGA_CONTRA_FACTURA_CREAR_DIALOG_1 = 1;
    public static final int RECARGA_CONTRA_FACTURA_CREAR_DIALOG_2 = 2;
    public static final int RECARGA_CONTRA_FACTURA_CREAR_DIALOG_3 = 3;

    // Redisenho.
    Activity context;
    ViewStub stub;
    EstadoServicio servicio;
    RelativeLayout cardLoading;

    public static RecargaContraFacturaFragment newInstance() {
        return new RecargaContraFacturaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_activar_recarga_contra_factura, container,false);

        context = getActivity();
        stub = (ViewStub) v.findViewById(R.id.stub);
        linea = ((BaseDrawerActivity)context).obtenerLineaSeleccionada();
        cardLoading = (RelativeLayout) v.findViewById(R.id.servicio_estado_loading);

        tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.titulo_estado)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.restitucion_title)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.descripcion_info_1)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.descripcion_info_2)).setTypeface(tf1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        consultarEstados = new ConsultarEstadosServicios(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (servicio == null) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            linea = activity.obtenerLineaSeleccionada();
            activity.getSpiceManager().execute(
                    consultarEstados.consultar(linea),
                    new EstadosServiciostListener()
            );
        }
    }

    public final class ActivacionRecargaContraFacturaListener implements RequestListener<Response> {

        String msgError;

        public ActivacionRecargaContraFacturaListener(String msgError) {
            this.msgError = msgError;
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();
                mostrarMsgError(msgError);
            }
        }

        @Override
        public void onRequestSuccess(final Response response) {
            if (getActivity() != null) {
                progress.dismiss();
                if (response != null) {
                    final String msgExito = getResources().getString(R.string.envio_solicitud_exitosa);
                    DialogFragment dialogo = crearDialogo(MensajesDeUsuario.TITULO_DIALOGO_ACTIVAR_RCF, msgExito);
                    dialogo.show(getActivity().getSupportFragmentManager(), "Activar-RCF");
                } else {
                    mostrarMsgError(msgError);
                }
            }
        }
    }

    public final class DesactivacionRecargaContraFacturaListener implements RequestListener<Response> {

        String msgError;

        public DesactivacionRecargaContraFacturaListener(String msgError) {
            this.msgError = msgError;
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();
                mostrarMsgError(msgError);
            }
        }

        @Override
        public void onRequestSuccess(final Response response) {
            if (getActivity() != null) {
                progress.dismiss();
                if (response != null) {
                    final String msg = getResources().getString(R.string.envio_solicitud_exitosa);
                    DialogFragment dialogo = crearDialogo(MensajesDeUsuario.TITULO_DIALOGO_DESACTIVAR_RCF, msg);
                    dialogo.show(getActivity().getSupportFragmentManager(), "Desctivar-RCF");
                } else {
                    mostrarMsgError(msgError);
                }
            }
        }
    }

    private void mostrarMsgError(final String msgError){
        DialogFragment dialogo = MensajesGenericos.newInstance("Error", msgError, false);
        dialogo.setTargetFragment(RecargaContraFacturaFragment.this, RECARGA_CONTRA_FACTURA_CREAR_DIALOG_2);
        dialogo.show(getActivity().getSupportFragmentManager(), "Activar-RCF");
    }

    public final class EstadosServiciostListener implements RequestListener<EstadoServicios> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), MensajesDeUsuario.SIN_DATOS_LINEA, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final EstadoServicios estados) {
            Activity context = getActivity();
            if (context != null) {
                if (estados != null) {
                    servicio = estados.getRecargaContraFactura();
                    if (servicio.isHabilitado()) {
                        if (stub != null) {
                            if (servicio.isActivado()) {
                                if (stub != null && stub.getParent() != null) {
                                    stub.setLayoutResource(R.layout.servicio_activado);
                                    stub.inflate();
                                    ((TextView) getView().findViewById(R.id.activado)).setTypeface(tf1);
                                }
                                asociarListenersDeDesactivacion();
                            } else {
                                if (stub != null && stub.getParent() != null) {
                                    stub.setLayoutResource(R.layout.servicio_desactivado);
                                    stub.inflate();
                                    ((TextView) getView().findViewById(R.id.desactivado)).setTypeface(tf1);
                                }
                                asociarListenersDeActivacion();
                            }
                        }
                    } else {
                        //Toast.makeText(context, getResources().getString(R.string.servicio_inhabilitado),
                          //      Toast.LENGTH_SHORT).show();
                        // poner un view con mensaje de que el servicio no esta habilitado
                        if (stub != null && stub.getParent() != null) {
                            stub.setLayoutResource(R.layout.servicio_inhabilitado);
                            stub.inflate();
                            cardLoading.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Toast.makeText(context, MensajesDeUsuario.SIN_ESTADO_SERVICIO, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void asociarListenersDeDesactivacion() {
        View view = getView();
        if (view != null) {
            cardLoading.setVisibility(View.GONE);
            RelativeLayout swith = (RelativeLayout) view.findViewById(R.id.contenedor_boton_desactivar_servicio);
            swith.setVisibility(View.VISIBLE);
            final boolean esActivacion = false;
            View.OnClickListener listenerDesactivar = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialogo = crearDialogo(esActivacion);
                    dialogo.show(getActivity().getSupportFragmentManager(), "Desactivar Roaming");
                }
            };
            Button botonDesactivar = (Button) view.findViewById(R.id.boton_desactivar_servicio);
            botonDesactivar.setOnClickListener(listenerDesactivar);
        }
    }

    private void asociarListenersDeActivacion() {
        View view = getView();
        if (view != null) {
            cardLoading.setVisibility(View.GONE);
            RelativeLayout swith = (RelativeLayout) view.findViewById(R.id.contenedor_boton_activar_servicio);
            swith.setVisibility(View.VISIBLE);
            final boolean esActivacion = true;
            View.OnClickListener listenerActivar = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialogo = crearDialogo(esActivacion);
                    dialogo.show(getActivity().getSupportFragmentManager(), "Activar Roaming");
                }
            };
            Button botonActivar = (Button)  view.findViewById(R.id.boton_activar_servicio);
            botonActivar.setOnClickListener(listenerActivar);
        }
    }

    private DialogFragment crearDialogo(final boolean esActivacion) {

        DialogFragment dialog = null;

        if (esActivacion) {

            dialog = MensajesGenericosAceptarRechazar.newInstance(
                    getResources().getString(R.string.activacion_title),
                    getResources().getString(R.string.roaming_confirmar_activacion),
                    "", esActivacion);

        } else {
            dialog = MensajesGenericosAceptarRechazar.newInstance(
                    getResources().getString(R.string.desactivacion_title),
                    getResources().getString(R.string.roaming_confirmar_desactivacion),
                    "", esActivacion);
        }

        dialog.setTargetFragment(RecargaContraFacturaFragment.this, RECARGA_CONTRA_FACTURA_CREAR_DIALOG_3);
        return dialog;
    }

    private DialogFragment crearDialogo(final String titulo, final String mensaje) {
        DialogFragment dialogo = MensajesGenericosAceptar.newInstance(titulo, mensaje);
        dialogo.setTargetFragment(RecargaContraFacturaFragment.this, RECARGA_CONTRA_FACTURA_CREAR_DIALOG_1);
        return dialogo;
    }

    private void ejecutarActivacion() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);

            String title = MensajesDeUsuario.TITULO_DIALOGO_ACTIVAR_RCF;
            String  msg = MensajesDeUsuario.MENSAJE_DIALOGO_ACTIVAR_RCF;
            progress =  ProgressDialog.show(context, title, msg, true);
            activacionServicios = new ActivacionServicios(context, new JacksonConverter(objectMapper));
            ((BaseDrawerActivity)context).getSpiceManager().execute(
                activacionServicios.activarRecargaContraFactura(linea),
                new ActivacionRecargaContraFacturaListener(MensajesDeUsuario.ERROR_ACTIVAR_SERVICIO)
            );
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ejecutarDesactivacion() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);

            String title = MensajesDeUsuario.TITULO_DIALOGO_DESACTIVAR_RCF;
            String  msg = MensajesDeUsuario.MENSAJE_DIALOGO_DESACTIVAR_RCF;
            progress =  ProgressDialog.show(context, title, msg, true);
            activacionServicios = new ActivacionServicios(context, new JacksonConverter(objectMapper));
            ((BaseDrawerActivity)context).getSpiceManager().execute(
                    activacionServicios.desactivarRecargaContraFactura(linea),
                    new DesactivacionRecargaContraFacturaListener(MensajesDeUsuario.ERROR_ACTIVAR_SERVICIO)
            );
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {

            case RECARGA_CONTRA_FACTURA_CREAR_DIALOG_1:
                if (resultCode == Activity.RESULT_OK) {

                }

                break;

            case RECARGA_CONTRA_FACTURA_CREAR_DIALOG_2:
                if (resultCode == Activity.RESULT_OK) {

                }
                break;

            case RECARGA_CONTRA_FACTURA_CREAR_DIALOG_3:
                if (resultCode == Activity.RESULT_OK) {

                    boolean esActivacion = Boolean.getBoolean(data.getStringExtra("esActivacion"));

                    if (esActivacion) {
                        ejecutarActivacion();
                    } else {
                        ejecutarDesactivacion();
                    }
                }

                break;
        }
    }
}
