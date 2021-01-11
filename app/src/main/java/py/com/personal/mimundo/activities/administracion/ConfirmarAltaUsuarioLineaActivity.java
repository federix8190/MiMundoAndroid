package py.com.personal.mimundo.activities.administracion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import py.com.personal.mimundo.disenhos.RelativeLayout3;
import py.com.personal.mimundo.security.client.MySSLSocketFactory;
import py.com.personal.mimundo.services.pines.models.Pin;
import py.com.personal.mimundo.services.usuarios.service.UsuarioInterface;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.Response;

/**
 * Created by Konecta on 23/07/2014.
 */
public class ConfirmarAltaUsuarioLineaActivity extends AppBaseActivity {

    private ProgressBar mProgressView;
    private RelativeLayout mLoginFormView;

    private ActivarUsuarioTask mActivarUsuarioTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_pin);
        setTitle(getResources().getString(R.string.alta_usuario_title));
        configurarActionBar();
        registerBaseActivityReceiver();

        mLoginFormView = (RelativeLayout) findViewById(R.id.form_login);
        mProgressView = (ProgressBar) findViewById(R.id.progressbar_login);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    DialogFragment newFragment = OnCreateDialogFragment.newInstance(MensajesDeUsuario.ACUERDO_DE_USUARIO + "\n"
                            + MensajesDeUsuario.BASES_Y_CONDICIONES, getString(R.string.alta_usuario_acuerdo_msg));
                    newFragment.show(getSupportFragmentManager(), "Acuerdo");

                } catch (Exception e) {
                    Toast.makeText(ConfirmarAltaUsuarioLineaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        TextView confirmarPin = (TextView) findViewById(R.id.confirmarPinButton);
        confirmarPin.setOnClickListener(listener);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Platform-Regular.otf");
        ((EditText) findViewById(R.id.pin)).setTypeface(tf);
        confirmarPin.setTypeface(tf);
    }

    public void onCreatePositiveClick() {

            try {
                String user = obtenerUser();
                final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                Long idPin = pref.getLong("pinId", 0);
                String respuesta = obtenerDato((EditText)findViewById(R.id.pin),
                        getResources().getString(R.string.alta_usuario_ingresar_pin));
                Pin pin = new Pin(idPin, respuesta);
                showProgress(true);
                mActivarUsuarioTask = new ActivarUsuarioTask(user, pin);
                mActivarUsuarioTask.execute();
            } catch (Exception e) {
                Toast.makeText(ConfirmarAltaUsuarioLineaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
    }

    public void onCreateNegativeClick() {
    }

    public static class OnCreateDialogFragment extends DialogFragment {

        public static OnCreateDialogFragment newInstance(String title, String message) {
            OnCreateDialogFragment frag = new OnCreateDialogFragment();
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
                    .setIcon(R.drawable.new_document)
                    .setPositiveButton(getString(R.string.alta_usuario_btn_aceptar),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((ConfirmarAltaUsuarioLineaActivity)getActivity()).onCreatePositiveClick();
                                }
                            }
                    )
                    .setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((ConfirmarAltaUsuarioLineaActivity)getActivity()).onCreateNegativeClick();
                                }
                            }
                    )
                    .create();
        }
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
     * Muestra el progressBar
     */
    public void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private String obtenerDato(EditText parametro, String msg) throws Exception {
        if(parametro.getText() != null && !parametro.getText().toString().isEmpty()) {
            return parametro.getText().toString();
        } else {
            throw new Exception(msg);
        }
    }

    /**
     * Activa el usuario.
     */
    public class ActivarUsuarioTask extends AsyncTask<Void, Void, Boolean> {

        private Pin pin;
        private String user;

        ActivarUsuarioTask(String user, Pin pin) {
            this.pin = pin;
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                RestAdapter adapter = getRestAdapter();
                UsuarioInterface service = adapter.create(UsuarioInterface.class);
                if (!user.isEmpty() && user.substring(0, 1).equals("0")) {
                    user = user.substring(1, user.length());
                }
                Response res = service.activarUsuario(user, pin);
                if (res.getStatus() == 200) {
                    System.err.println("Activar usuario : " + res.getStatus());
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean sucess) {
            mActivarUsuarioTask = null;
            showProgress(false);
            if(sucess){
                Intent i = new Intent(ConfirmarAltaUsuarioLineaActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if (i != null) {
                    Toast.makeText(ConfirmarAltaUsuarioLineaActivity.this,
                            getResources().getString(R.string.alta_usuario_creacion_exitosa), Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    closeAllActivities();
                } else {
                    Toast.makeText(ConfirmarAltaUsuarioLineaActivity.this,
                            getResources().getString(R.string.alta_usuario_error_activacion), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mActivarUsuarioTask = null;
            showProgress(false);
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

    private String obtenerUser() {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
        String user = pref.getString("user", "");
        return user;
    }

    private void configurarActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
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
