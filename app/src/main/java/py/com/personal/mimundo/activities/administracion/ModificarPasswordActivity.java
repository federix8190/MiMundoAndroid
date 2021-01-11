package py.com.personal.mimundo.activities.administracion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.security.KeyStore;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import py.com.personal.mimundo.activities.AppBaseActivity;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.adapters.AdapterSpinner4;
import py.com.personal.mimundo.disenhos.Configuraciones;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.saldo.PinSesionFragment;
import py.com.personal.mimundo.security.client.MySSLSocketFactory;
import py.com.personal.mimundo.services.SampleRetrofitSpiceService;
import py.com.personal.mimundo.services.pines.models.CrearPin;
import py.com.personal.mimundo.services.pines.service.PinInterface;
import py.com.personal.mimundo.services.pines.service.PinValidacionService;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 11/08/2014.
 */
public class ModificarPasswordActivity extends AppBaseActivity {

    private Spinner canalEnvioSpinner;
    private EditText usuarioTextView;

    private ProgressBar mProgressView;
    private RelativeLayout mLoginFormView;

//    private EnviarPinTask mTask;
    private PinValidacionService service;
    private String user;

    private SpiceManager spiceManager = new SpiceManager(SampleRetrofitSpiceService.class);

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        service = new PinValidacionService(this, new JacksonConverter(objectMapper), false);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetear_password);
        setTitle(getResources().getString(R.string.modificar_password_title));
        configurarActionBar();
        registerBaseActivityReceiver();

        mLoginFormView = (RelativeLayout) findViewById(R.id.form_login);
        mProgressView = (ProgressBar) findViewById(R.id.progressbar_login);
        canalEnvioSpinner = (Spinner) findViewById(R.id.canal_envio);
        usuarioTextView = (EditText) findViewById(R.id.usuario);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String usuario  = obtenerDato(usuarioTextView, getResources().getString(R.string.modificar_password_ingresar_usuario));

                    String canalEnvio;
                    if (!usuario.isEmpty() && usuario.substring(0, 1).equals("0")) {
                        usuario = usuario.substring(1, usuario.length());
                    }
                    user = usuario;
                    if (canalEnvioSpinner.getSelectedItemPosition() == 0) {
                        canalEnvio = "SMS";
                    } else {
                        canalEnvio = "MAIL";
                    }
                    showProgress(true);
                    ModificarPasswordActivity.this.getSpiceManager().execute(service.generarPin(usuario,canalEnvio), new PinSesionListener());
//                    mTask = new EnviarPinTask(usuario, canalEnvio);
//                    mTask.execute((Void) null);
                } catch(Exception e) {
                    Toast.makeText(ModificarPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        TextView enviarPinButton = (TextView) findViewById(R.id.btn_enviar_pin);
        enviarPinButton.setOnClickListener(onClickListener);

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.combo_item);
        AdapterSpinner4 adapter = new AdapterSpinner4(true, this);
        adapter.addItem("Mensaje de texto");
        adapter.addItem("Email");
        canalEnvioSpinner.setAdapter(adapter);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Platform-Regular.otf");
        ((EditText) findViewById(R.id.usuario)).setTypeface(tf);
        ((EditText) findViewById(R.id.email)).setTypeface(tf);
        enviarPinButton.setTypeface(tf);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBaseActivityReceiver();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    //@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow for very
        // easy animations. If available, use these APIs to fade-in  the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    public final class PinSesionListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showProgress(false);
            DialogFragment dialogo = ModificarPassDialog.newInstance(
                    getResources().getString(R.string.modificar_password_title),
                    getResources().getString(R.string.alta_usuario_error_envio_pin), false);
            dialogo.show(ModificarPasswordActivity.this.getSupportFragmentManager(), "Modificar Contraseña");
        }

        @Override
        public void onRequestSuccess(final Response respuesta) {
            showProgress(false);
            if (respuesta != null) {

                List<Header> headers = respuesta.getHeaders();
                for (Header header : headers) {
                    if (header != null && header.getName().equals("Location")) {
                        String cadena = header.getValue(); /*guardar en el preferences el id del pin creado*/
                        String[] numerosComoArray = cadena.split("/");
                        String pinId = numerosComoArray[numerosComoArray.length-1];
                        SharedPreferences  pref = ModificarPasswordActivity.this.getSharedPreferences("MiMundoPreferences", BaseDrawerActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putLong("pinId", Long.parseLong(pinId));
                        editor.putString("user", user);
                        editor.commit();
                        break;
                    }
                }

                DialogFragment dialogo = ModificarPassDialog.newInstance(
                        getResources().getString(R.string.modificar_password_title),
                        getResources().getString(R.string.alta_usuario_pin_enviado), true);
                dialogo.show(ModificarPasswordActivity.this.getSupportFragmentManager(), "Modificar Contraseña");
            } else {
                DialogFragment dialogo = ModificarPassDialog.newInstance(
                        getResources().getString(R.string.modificar_password_title),
                        getResources().getString(R.string.alta_usuario_error_envio_pin), false);
                dialogo.show(ModificarPasswordActivity.this.getSupportFragmentManager(), "Modificar Contraseña");
            }
        }
    }

    private String obtenerDato(EditText parametro, String msg) throws Exception {
        if (parametro.getText() != null && !parametro.getText().toString().isEmpty()) {
            return parametro.getText().toString();
        } else {
            throw new Exception(msg);
        }
    }

    private RestAdapter getRestAdapter(){
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("client_id", Configuraciones.CLIENT_ID);
            }
        };
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Configuraciones.MI_MUNDO_SERVER)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new ApacheClient(getNewHttpClient()))
                .build();

        return adapter;
    }

    public void modificarNeutralClick(boolean exitoso) {
        if (exitoso) {
            Intent i = new Intent(ModificarPasswordActivity.this, IngresarPinActivity.class);
            startActivity(i);
        }
    }
    public static class ModificarPassDialog extends DialogFragment {

        public static ModificarPassDialog newInstance(String title, String message, boolean exitoso) {
            ModificarPassDialog frag = new ModificarPassDialog();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            args.putBoolean("exitoso", exitoso);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");
            final boolean exitoso = getArguments().getBoolean("exitoso");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setNeutralButton(R.string.alta_usuario_btn_aceptar,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((ModificarPasswordActivity)getActivity()).modificarNeutralClick(exitoso);
                                }
                            }
                    )
                    .create();
        }
    }

    private void configurarActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
