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
import android.os.Build;
import android.os.Bundle;
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
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.security.client.MySSLSocketFactory;
import py.com.personal.mimundo.services.SampleRetrofitSpiceService;
import py.com.personal.mimundo.services.pines.service.PinValidacionService;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 11/08/2014.
 */
public class IngresarPinActivity extends AppBaseActivity {

    //private ValidarPinTask mValidarPin;

    private ProgressBar mProgressView;
    private RelativeLayout mLoginFormView;

    private PinValidacionService service;

    private SpiceManager spiceManager = new SpiceManager(SampleRetrofitSpiceService.class);

    private String respuestaPin;

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
        setContentView(R.layout.activity_ingresar_pin);
        setTitle(getResources().getString(R.string.modificar_password_title));
        configurarActionBar();
        registerBaseActivityReceiver();

        mLoginFormView = (RelativeLayout) findViewById(R.id.form_login);
        mProgressView = (ProgressBar) findViewById(R.id.progressbar_login);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                    Long idPin = pref.getLong("pinId", 0);
                    EditText respuesta = (EditText)findViewById(R.id.pin);
                    if(respuesta.getText() != null && !respuesta.getText().toString().isEmpty()) {
                        showProgress(true);
                        //mValidarPin = new ValidarPinTask(idPin, respuesta.getText().toString());
                        //mValidarPin.execute();
                        respuestaPin = respuesta.getText().toString();
                        IngresarPinActivity.this.getSpiceManager().execute(
                                service.validarPin(idPin,respuesta.getText().toString()),
                                new PinSesionValidarListener()
                        );
                    } else {
                        Toast.makeText(IngresarPinActivity.this,
                                getResources().getString(R.string.alta_usuario_ingresar_pin), Toast.LENGTH_SHORT).show();
                    }

                } catch(Exception e) {
                    Toast.makeText(IngresarPinActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        };
        TextView confirmarPin = (TextView) findViewById(R.id.confirmarPinButton);
        confirmarPin.setOnClickListener(listener);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Platform-Regular.otf");
        ((EditText) findViewById(R.id.pin)).setTypeface(tf);
        confirmarPin.setTypeface(tf);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBaseActivityReceiver();
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


    public final class PinSesionValidarListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showProgress(false);
        }

        @Override
        public void onRequestSuccess(final Response respuesta) {
            showProgress(false);
            if(respuesta != null && respuesta.getStatus() == 200) {
                Intent i = new Intent(IngresarPinActivity.this, IngresarPasswordActivity.class);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("pinRespuesta", respuestaPin);
                editor.commit();
                if (i != null) {
                    startActivity(i);
                }
            } else {
                DialogFragment dialogo = IngresarPinActivityDialog.newInstance(
                        getResources().getString(R.string.modificar_password_confirmar_pin),
                        getResources().getString(R.string.modificar_password_pin_invalido));
                dialogo.show(IngresarPinActivity.this.getSupportFragmentManager(), "Confirmar PIN");
            }
        }
    }

    public void ingresarPinNeutralClick() {
    }

    public static class IngresarPinActivityDialog extends DialogFragment {

        public static IngresarPinActivityDialog newInstance(String title, String message) {
            IngresarPinActivityDialog frag = new IngresarPinActivityDialog();
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
                                    ((IngresarPinActivity)getActivity()).ingresarPinNeutralClick();
                                }
                            }
                    )
                    .create();
        }
    }

    private RestAdapter getRestAdapter(){
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
//                request.addHeader("Authorization", pref.getString("Authorization", ""));
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

    private void configurarActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
    }
}
