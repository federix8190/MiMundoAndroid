package py.com.personal.mimundo.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.atencionpersonal.AtencionPersonalRequest;
import py.com.personal.mimundo.services.atencionpersonal.Sugerencia;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.usuarios.models.Usuario;
import py.com.personal.mimundo.services.usuarios.service.UsuarioService;
import py.com.personal.mimundo.utils.PreferenceUtils;
import py.com.personal.mimundo.utils.StringUtils;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 04/11/2015.
 */
public class AtencionPersonalFragment extends Fragment {

    private ProgressBar progressBar;
    private ScrollView formulario;
    public Toolbar mToolbar;
    private Spinner selectorLineas;

    private EditText nombresView;
    private EditText emailView;
    private EditText numeroLineaView;
    private EditText sugerenciaView;
    private Button enviarSugerenciaBtn;
    private ProgressDialog progress;

    private List<Linea> listaLineas;
    private String lineaSeleccionada;

    private PreferenceUtils preferenceUtils;
    private UsuarioService usuariosService;
    private AtencionPersonalRequest service;
    public static final int ATENCION_PERSONAL_CREAR_DIALOG_1 = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.fragment_atencion_personal, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setTitle("Atención");
        activity.setActionBar(false, null);

        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
        formulario = (ScrollView) v.findViewById(R.id.formulario);

        // Campos del formulario
        nombresView = (EditText) v.findViewById(R.id.nombres_apellidos_field);
        emailView = (EditText) v.findViewById(R.id.email_field);
        numeroLineaView = (EditText) v.findViewById(R.id.nro_linea_field);
        sugerenciaView = (EditText) v.findViewById(R.id.sugerencias_field);

        enviarSugerenciaBtn = (Button) v.findViewById(R.id.button_enviar_sugerencia);
        enviarSugerenciaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String msgError = validarParametros();
                    if (msgError != null) {
                        mostrarMensaje(getResources().getString(R.string.error_title), msgError);
                        return;
                    } else {
                        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
                        String nombres = nombresView.getText().toString();
                        String nroLinea = numeroLineaView.getText().toString();
                        String formato = "nombres y apellidos: %s numero de linea: %s";
                        String datosContacto = String.format(formato, nombres, nroLinea);
                        String email = emailView.getText().toString();
                        String sugerencia = sugerenciaView.getText().toString();
                        String title = getResources().getString(R.string.envio_sugerencias);
                        String  msg = getResources().getString(R.string.ejecutando_operacion);
                        progress =  ProgressDialog.show(activity, title, msg, true);
                        Sugerencia datos = new Sugerencia(datosContacto, email, sugerencia);
                        activity.getSpiceManager().execute(service.enviarSugerencia(datos), new SugerenciaRequestListener());
                    }
                } catch (Exception e) {
                    if (progress != null) {
                        progress.dismiss();
                    }
                    mostrarMensaje(getResources().getString(R.string.error_title), getResources().getString(R.string.error_operacion));
                }
            }
        });

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.header_1)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.header_2)).setTypeface(tf1);

        Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CarroisGothic-Regular.ttf");
        ((TextView) v.findViewById(R.id.nombres_apellidos_label)).setTypeface(tf2);
        ((TextView) v.findViewById(R.id.email_label)).setTypeface(tf2);
        ((TextView) v.findViewById(R.id.nro_linea_label)).setTypeface(tf2);
        ((TextView) v.findViewById(R.id.sugerencias_label)).setTypeface(tf2);
        nombresView.setTypeface(tf2);
        emailView.setTypeface(tf2);
        numeroLineaView.setTypeface(tf2);
        sugerenciaView.setTypeface(tf2);
        enviarSugerenciaBtn.setTypeface(tf2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        Converter converter = new JacksonConverter(objectMapper);
        usuariosService = new UsuarioService(getActivity(), converter, true);
        service = new AtencionPersonalRequest(getActivity(), converter);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        showProgress(true);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.getSpiceManager().execute(usuariosService.consultar(activity.obtenerCodigoUsuario()), new UsuarioRequestListener());
    }

    public final class UsuarioRequestListener implements RequestListener<Usuario> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showProgress(false);
                Toast.makeText(getActivity(), "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                ((BaseDrawerActivity)getActivity()).setActionBar(true, "AtencionPersonalFragment");
            }
        }

        @Override
        public void onRequestSuccess(final Usuario datosUsuario) {
            showProgress(false);
            BaseDrawerActivity activity = ((BaseDrawerActivity) getActivity());
            if (activity != null) {
                activity.setActionBar(true, "AtencionPersonalFragment");
                if (datosUsuario != null) {
                    nombresView.setText(datosUsuario.getNombres());
                    emailView.setText(datosUsuario.getEmail());
                    String nroLinea = activity.obtenerLineaSeleccionada();
                    if (nroLinea != null && StringUtils.esLineaTelefonia(nroLinea)) {
                        numeroLineaView.setText("0" + nroLinea);
                    }
                } else {
                    Toast.makeText(getActivity(), "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public final class SugerenciaRequestListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (progress != null) {
                progress.dismiss();
            }
            mostrarMensaje(getResources().getString(R.string.error_title),
                    getResources().getString(R.string.error_operacion));
        }

        @Override
        public void onRequestSuccess(final Response response) {
            if (progress != null) {
                progress.dismiss();
            }
            if (response != null) {
                final String mensaje = "Tu consulta fue enviada. Muchas Gracias.";//obtenerMensaje(response);
                final String titulo = MensajesDeUsuario.OPERACION_EXITOSA;
                mostrarMensaje(titulo, mensaje);
            } else {
                mostrarMensaje(getResources().getString(R.string.error_title),
                        getResources().getString(R.string.error_operacion));
            }
        }
    }

    private String validarParametros() {
        if (nombresView.getText() != null) {
            if (nombresView.getText().toString() == null || nombresView.getText().toString().isEmpty()) {
                return "Debe proveer el nombre y apellido";
            }
        } else {
            return "Debe proveer el nombre y apellido";
        }
        if (emailView.getText() != null) {
            if (emailView.getText().toString() == null || emailView.getText().toString().isEmpty()) {
                return "Debe proveer el email";
            }
        } else {
            return "Debe proveer el email";
        }
        if (numeroLineaView.getText() != null) {
            if (numeroLineaView.getText().toString() == null || numeroLineaView.getText().toString().isEmpty()) {
                return "Debe proveer el numero de Linea";
            }
        } else {
            return "Debe proveer el numero de Linea";
        }
        if (sugerenciaView.getText() != null) {
            if (sugerenciaView.getText().toString() == null || sugerenciaView.getText().toString().isEmpty()) {
                return "Debe proveer la sugerencia";
            }
        } else {
            return "Debe proveer la sugerencia";
        }
        return null;
    }

    public void mostrarMensaje(final String titulo, final String msg) {

        DialogFragment dialog = MensajesGenericos.newInstance(titulo, msg, false);
        dialog.setTargetFragment(AtencionPersonalFragment.this,ATENCION_PERSONAL_CREAR_DIALOG_1);
        dialog.show(getActivity().getSupportFragmentManager(), "Dialogo-Mensaje");
    }

    public void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        formulario.setVisibility(show? View.GONE: View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {

            case ATENCION_PERSONAL_CREAR_DIALOG_1:

                break;

        }
    }

}
