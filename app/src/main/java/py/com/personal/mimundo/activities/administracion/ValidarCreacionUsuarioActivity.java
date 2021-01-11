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
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.security.client.MySSLSocketFactory;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.usuarios.models.ValidarUsuario;
import py.com.personal.mimundo.services.usuarios.service.UsuarioInterface;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

/**
 * Created by Konecta on 25/07/2014.
 */
public class ValidarCreacionUsuarioActivity extends AppBaseActivity {

    private ValidarCreacionTask mValidacionTask;

    private ProgressBar mProgressView;
    private RelativeLayout mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_creacion_usuario);
        setTitle(getResources().getString(R.string.alta_usuario_title));
        configurarActionBar();
        registerBaseActivityReceiver();

        mLoginFormView = (RelativeLayout) findViewById(R.id.form_login);
        mProgressView = (ProgressBar) findViewById(R.id.progressbar_login);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText numeroLinea = (EditText) findViewById(R.id.numeroLinea);
                if(numeroLinea.getText() != null && !numeroLinea.getText().toString().isEmpty()) {
                    showProgress(true);
                    System.err.println("Numero de linea  : " + numeroLinea.getText().toString());
                    mValidacionTask = new ValidarCreacionTask(numeroLinea.getText().toString());
                    mValidacionTask.execute((Void) null);
                } else {
                    Toast.makeText(ValidarCreacionUsuarioActivity.this,
                            getResources().getString(R.string.alta_usuario_ingresar_linea), Toast.LENGTH_SHORT).show();
                }
            }
        };
        TextView validarLinea = (TextView) findViewById(R.id.validarLineaButton);
        validarLinea.setOnClickListener(listener);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Platform-Regular.otf");
        ((EditText) findViewById(R.id.numeroLinea)).setTypeface(tf);
        validarLinea.setTypeface(tf);
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

    public void showProgress(final boolean show) {
        // The ViewPropertyAnimator APIs are not available, so simply show and hide the relevant UI components.
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ValidarCreacionTask extends AsyncTask<Void, Void, Resultado> {

        private String mNumeroLinea;

        ValidarCreacionTask(String numeroLinea) {
            mNumeroLinea = numeroLinea;
        }

        @Override
        protected Resultado doInBackground(Void... params) {
            try {
                RestAdapter adapter = getRestAdapter();
                UsuarioInterface service = adapter.create(UsuarioInterface.class);
                String numeroLinea = mNumeroLinea;
                if (!mNumeroLinea.isEmpty() && mNumeroLinea.substring(0, 1).equals("0")) {
                    numeroLinea = mNumeroLinea.substring(1, mNumeroLinea.length());
                }
                ValidarUsuario datos = new ValidarUsuario();
                datos.setTipoUsuario("LINEA");
                datos.setNumeroLinea(numeroLinea);
                Resultado resultado = service.valiarCreacionUsuario(datos);

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user", mNumeroLinea);
                editor.commit();
                return resultado;
            } catch (RetrofitError e) {
                Resultado r = (Resultado) e.getBody();
                if (r != null && r.getMensaje() != null && !r.getMensaje().isEmpty()) {
                    return new Resultado(false, r.getMensaje());
                } else {
                    return new Resultado(false, "Error al ejecutar la operación");
                }
            }
        }

        @Override
        protected void onPostExecute(final Resultado resultado) {
            mValidacionTask = null;
            showProgress(false);
            if (resultado.isExitoso()) {
                Intent i = new Intent(ValidarCreacionUsuarioActivity.this, AltaUsuarioLineaActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (i != null) {
                    startActivity(i);
                }
            } else {
                DialogFragment dialogo = ValidarCreacionUsuarioDialogFragment.newInstance(
                        getResources().getString(R.string.alta_usuario_validacion_title), resultado.getMensaje());
                //DialogFragment dialogo = crearDialogo(getResources().getString(R.string.alta_usuario_validacion_title), resultado.getMensaje());
                dialogo.show(ValidarCreacionUsuarioActivity.this.getSupportFragmentManager(), "Validacion");
            }
        }

        @Override
        protected void onCancelled() {
            mValidacionTask = null;
            showProgress(false);
        }
    }

    private RestAdapter getRestAdapter() {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);

        // No se debe incluir el Token de session para validar la linea
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

    /*private DialogFragment crearDialogo(final String titulo, final String mensaje) {

        DialogFragment dialog = ValidarCreacionUsuarioDialogFragment.newInstance(titulo, mensaje);

        return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(mensaje).setTitle(titulo);
                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
                AlertDialog dialog = builder.create();
                return dialog;
            }
        };
    }*/

    public void validarNeutralClick() {

    }


    public static class ValidarCreacionUsuarioDialogFragment extends DialogFragment {

        public static ValidarCreacionUsuarioDialogFragment newInstance(String title, String message) {
            ValidarCreacionUsuarioDialogFragment frag = new ValidarCreacionUsuarioDialogFragment();
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
                                    ((ValidarCreacionUsuarioActivity)getActivity()).validarNeutralClick();
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
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE
                | ActionBar.DISPLAY_SHOW_CUSTOM);
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
