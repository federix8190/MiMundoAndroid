package py.com.personal.mimundo.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import py.com.personal.mimundo.adapters.AdapterSpinnerActionBar;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.SampleRetrofitSpiceService;
import py.com.personal.mimundo.services.atencionpersonal.AtencionPersonalRequest;
import py.com.personal.mimundo.services.atencionpersonal.Sugerencia;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.usuarios.models.Usuario;
import py.com.personal.mimundo.services.usuarios.service.UsuarioService;
import py.com.personal.mimundo.utils.PreferenceUtils;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class AtencionPersonalActivity extends AppCompatActivity {

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

    private SpiceManager spiceManager = new SpiceManager(SampleRetrofitSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atencion_personal);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        formulario = (ScrollView) findViewById(R.id.formulario);

        // Campos del formulario
        nombresView = (EditText) findViewById(R.id.nombres_apellidos_field);
        emailView = (EditText) findViewById(R.id.email_field);
        numeroLineaView = (EditText) findViewById(R.id.nro_linea_field);
        sugerenciaView = (EditText) findViewById(R.id.sugerencias_field);

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Atención");
        listaLineas = BaseDrawerActivity.getLineasUsuario();

        enviarSugerenciaBtn = (Button) findViewById(R.id.button_enviar_sugerencia);
        enviarSugerenciaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String msgError = validarParametros();
                    if (msgError != null) {
                        mostrarMensaje(getResources().getString(R.string.error_title), msgError);
                        return;
                    } else {
                        String nombres = nombresView.getText().toString();
                        String nroLinea = numeroLineaView.getText().toString();
                        String formato = "nombres y apellidos: %s numero de linea: %s";
                        String datosContacto = String.format(formato, nombres, nroLinea);
                        String email = emailView.getText().toString();
                        String sugerencia = sugerenciaView.getText().toString();
                        String title = getResources().getString(R.string.envio_sugerencias);
                        String  msg = getResources().getString(R.string.ejecutando_operacion);
                        progress =  ProgressDialog.show(AtencionPersonalActivity.this, title, msg, true);
                        Sugerencia datos = new Sugerencia(datosContacto, email, sugerencia);
                        getSpiceManager().execute(service.enviarSugerencia(datos), new SugerenciaRequestListener());
                    }
                } catch (Exception e) {
                    if (progress != null) {
                        progress.dismiss();
                    }
                    mostrarMensaje(getResources().getString(R.string.error_title), getResources().getString(R.string.error_operacion));
                }
            }
        });

        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) findViewById(R.id.header_1)).setTypeface(tf1);
        ((TextView) findViewById(R.id.header_2)).setTypeface(tf1);

        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/CarroisGothic-Regular.ttf");
        ((TextView) findViewById(R.id.nombres_apellidos_label)).setTypeface(tf2);
        ((TextView) findViewById(R.id.email_label)).setTypeface(tf2);
        ((TextView) findViewById(R.id.nro_linea_label)).setTypeface(tf2);
        ((TextView) findViewById(R.id.sugerencias_label)).setTypeface(tf2);
        nombresView.setTypeface(tf2);
        emailView.setTypeface(tf2);
        numeroLineaView.setTypeface(tf2);
        sugerenciaView.setTypeface(tf2);
        enviarSugerenciaBtn.setTypeface(tf2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        Converter converter = new JacksonConverter(objectMapper);
        usuariosService = new UsuarioService(this, converter, true);
        service = new AtencionPersonalRequest(this, converter);

        showProgress(true);
        getSpiceManager().execute(usuariosService.consultar(obtenerUsuario()), new UsuarioRequestListener());
    }

    public final class UsuarioRequestListener implements RequestListener<Usuario> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showProgress(false);
            Toast.makeText(AtencionPersonalActivity.this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
            desplegarSelector();
        }

        @Override
        public void onRequestSuccess(final Usuario datosUsuario) {
            showProgress(false);
            if (datosUsuario != null) {
                desplegarSelector();
                nombresView.setText(datosUsuario.getNombres());
                emailView.setText(datosUsuario.getEmail());
                numeroLineaView.setText(datosUsuario.getLineaAsignada());
            } else {
                Toast.makeText(AtencionPersonalActivity.this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
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

    private void desplegarSelector() {
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.selector_lineas, null);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(Gravity.RIGHT));

        AdapterSpinnerActionBar adapterSelectorLineas = new AdapterSpinnerActionBar(true, this);
        for (Linea linea : listaLineas) {
            adapterSelectorLineas.addItem(linea.getNumeroLinea());
        }

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lineaSeleccionada = listaLineas.get(i).getNumeroLinea();
                numeroLineaView.setText(lineaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        // Agregamos el selector de lineas al ActionBar
        selectorLineas = (Spinner) actionBar.getCustomView().findViewById(R.id.selector_lineas);
        selectorLineas.setAdapter(adapterSelectorLineas);
        selectorLineas.setOnItemSelectedListener(listener);

    }

    public void mostrarMensaje(final String titulo, final String msg) {
        DialogFragment newFragment = AtencionPersonalActivity.MostrarMensajeDialogFragment.newInstance(titulo, msg);
        newFragment.show(getSupportFragmentManager(), "Dialogo-Mensaje");
    }

    public void mostrarNeutralClick() {
    }

    public static class MostrarMensajeDialogFragment extends DialogFragment {

        public static AtencionPersonalActivity.MostrarMensajeDialogFragment newInstance(String title, String message) {
            AtencionPersonalActivity.MostrarMensajeDialogFragment frag = new AtencionPersonalActivity.MostrarMensajeDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((AtencionPersonalActivity)getActivity()).mostrarNeutralClick();
                                }
                            }
                    )
                    .create();
        }
    }

    private String obtenerUsuario() {
        preferenceUtils = new PreferenceUtils(AtencionPersonalActivity.this);
        return preferenceUtils.getCodigoUsuario();
    }

    public void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        formulario.setVisibility(show? View.GONE: View.VISIBLE);
    }

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }

}
