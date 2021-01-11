package py.com.personal.mimundo.activities.administracion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import py.com.personal.mimundo.activities.AppBaseActivity;
import py.com.personal.mimundo.disenhos.Configuraciones;
import py.com.personal.mimundo.activities.LoginActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.security.client.MySSLSocketFactory;
import py.com.personal.mimundo.services.SampleRetrofitSpiceService;
import py.com.personal.mimundo.services.pines.service.PinValidacionService;
import py.com.personal.mimundo.services.usuarios.models.ModificarClave;
import py.com.personal.mimundo.services.usuarios.service.UsuarioInterface;
import py.com.personal.mimundo.services.usuarios.service.UsuarioService;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 12/08/2014.
 */
public class IngresarPasswordActivity extends AppBaseActivity {

    private ProgressBar mProgressView;
    private RelativeLayout mLoginFormView;

    //private ModificarPasswordTask mTask;

    private PinValidacionService service;
    private UsuarioService usuarioService;

    private SpiceManager spiceManager = new SpiceManager(SampleRetrofitSpiceService.class);

    private String respuestaPin;

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        service = new PinValidacionService(this, new JacksonConverter(objectMapper));

        usuarioService = new UsuarioService(this,new JacksonConverter(objectMapper), false);
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
        setContentView(R.layout.activity_modificar_password);
        setTitle(getResources().getString(R.string.modificar_password_title));
        configurarActionBar();
        registerBaseActivityReceiver();

        mLoginFormView = (RelativeLayout) findViewById(R.id.form_login);
        mProgressView = (ProgressBar) findViewById(R.id.progressbar_login);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                String usuario = pref.getString("user", "");
                Long idPin = pref.getLong("pinId", 0);
                String respuesta = pref.getString("pinRespuesta", "");

                try {
                    String password1 = obtenerDato((EditText) findViewById(R.id.password),
                            getResources().getString(R.string.alta_usuario_confirmar_password));
                    String password2 = obtenerDato((EditText) findViewById(R.id.confirma_password),
                            getResources().getString(R.string.alta_usuario_ingresar_password));

                    if (password1.equals(password2)) {
                        showProgress(true);
                        //mTask = new ModificarPasswordTask(usuario, idPin, respuesta, password1);
                        //mTask.execute();
                        IngresarPasswordActivity.this.getSpiceManager().execute(usuarioService.obtenerClave(usuario, idPin, respuesta, password1), new IngresarPasswordListener());
                    } else {
                        Toast.makeText(IngresarPasswordActivity.this,
                                getResources().getString(R.string.alta_usuario_error_password), Toast.LENGTH_SHORT).show();
                    }

                } catch(Exception e) {
                    Toast.makeText(IngresarPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        TextView aceptarButton = (TextView) findViewById(R.id.btn_aceptar);
        aceptarButton.setOnClickListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBaseActivityReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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

    public final class IngresarPasswordListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            //mTask = null;
            showProgress(false);
        }

        @Override
        public void onRequestSuccess(Response respuesta) {
            //mTask = null;
            showProgress(false);
            if (respuesta != null) {
                if (respuesta.getStatus() == 200) {

                    DialogFragment newFragment = ModifDialogFragment.newInstance(getString(R.string.modificar_password_operacion_exitosa),
                            MensajesDeUsuario.OPERACION_EXITOSA);
                    newFragment.show(getSupportFragmentManager(), "Atencion");

                } else if (respuesta.getStatus() == 404) {

                    crearDialogo(getResources().getString(R.string.modificar_password_title),
                            getResources().getString(R.string.modificar_password_usuario_existe));

                } else if (respuesta.getStatus() == 400) {
                    crearDialogo(getResources().getString(R.string.modificar_password_title),
                            getResources().getString(R.string.modificar_password_pin_invalido));
                }
            } else {
                crearDialogo(getResources().getString(R.string.modificar_password_title),
                        getResources().getString(R.string.error_operacion));
            }
        }
    }

    private String obtenerDato(EditText parametro, String msg) throws Exception {
        if(parametro.getText() != null && !parametro.getText().toString().isEmpty()) {
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

    private void crearDialogo(String titulo, String mensaje) {

        DialogFragment newFragment = ModificarDialogFragment.newInstance(titulo, mensaje);

        newFragment.show(getSupportFragmentManager(), "Modificar Contraseña");
    }

    public void modifNeutralClick () {
        final Intent i = new Intent(IngresarPasswordActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (i != null) {
            startActivity(i);
            closeAllActivities();
        }
    }

    public static class ModificarDialogFragment extends DialogFragment {

        public static ModificarDialogFragment newInstance(String title, String message) {
            ModificarDialogFragment frag = new ModificarDialogFragment();
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
                    .setNeutralButton(R.string.alta_usuario_btn_aceptar,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((IngresarPasswordActivity)getActivity()).modifNeutralClick();
                                }
                            }
                    )
                    .create();
        }
    }

    public static class ModifDialogFragment extends DialogFragment {

        public static ModificarDialogFragment newInstance(String title, String message) {
            ModificarDialogFragment frag = new ModificarDialogFragment();
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
                    .setNeutralButton(R.string.alta_usuario_btn_aceptar,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((IngresarPasswordActivity)getActivity()).modifNeutralClick();
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
}
