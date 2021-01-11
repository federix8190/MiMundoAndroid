package py.com.personal.mimundo.activities.administracion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import py.com.personal.mimundo.activities.AppBaseActivity;
import py.com.personal.mimundo.disenhos.Configuraciones;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.security.client.MySSLSocketFactory;
import py.com.personal.mimundo.services.captchas.models.PeticionCaptcha;
import py.com.personal.mimundo.services.captchas.service.CaptchasInterface;
import py.com.personal.mimundo.services.usuarios.models.CrearUsuarioTemporal;
import py.com.personal.mimundo.services.usuarios.service.UsuarioInterface;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

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

import py.com.personal.mimundo.utils.json.JSONObject;
import py.com.personal.mimundo.utils.json.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

/**
 * Created by Konecta on 23/07/2014.
 */
public class AltaUsuarioLineaActivity extends AppBaseActivity {

    private CrearUsuarioTask mCrearUsuarioTask;
    private CargarCaptchaTask mCargarCaptchaTask;
    ImageView mImagenCaptcha;

    private ProgressBar mProgressView;
    private RelativeLayout mLoginFormView;
    private ProgressBar progressCaptcha;
    private String mensajeServerDefault = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_usuario_linea);
        setTitle(getResources().getString(R.string.alta_usuario_title));
        configurarActionBar();
        registerBaseActivityReceiver();

        mLoginFormView = (RelativeLayout) findViewById(R.id.form_login);
        mProgressView = (ProgressBar) findViewById(R.id.progressbar_login);
        progressCaptcha = (ProgressBar) findViewById(R.id.loader_progress_captcha);
        progressCaptcha.setVisibility(View.VISIBLE);

        mImagenCaptcha = (ImageView)findViewById(R.id.captcha_imagen);
        mCargarCaptchaTask = new CargarCaptchaTask();
        mCargarCaptchaTask.execute();

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                String user = pref.getString("user", "");
                Long idPeticionCaptcha = pref.getLong("idPeticionCaptcha", 0);

                try {
                    String email = obtenerDato((EditText) findViewById(R.id.email),
                            getResources().getString(R.string.alta_usuario_confirmar_correo));
                    String password1 = obtenerDato((EditText) findViewById(R.id.password),
                            getResources().getString(R.string.alta_usuario_confirmar_password));
                    String password2 = obtenerDato((EditText) findViewById(R.id.confirmaPassword),
                            getResources().getString(R.string.alta_usuario_ingresar_password));
                    String captcha = obtenerDato((EditText) findViewById(R.id.captcha),
                            getResources().getString(R.string.alta_usuario_ingresar_captcha));

                    if (password1.equals(password2)) {
                        CrearUsuarioTemporal datos = new CrearUsuarioTemporal();
                        datos.setNumeroLinea(user);
                        datos.setEmail(email);
                        datos.setClave(password1);
                        datos.setPeticionId(idPeticionCaptcha);
                        datos.setRespuesta(captcha);
                        showProgress(true);
                        mCrearUsuarioTask = new CrearUsuarioTask(datos);
                        mCrearUsuarioTask.execute((Void) null);
                    } else {
                        Toast.makeText(AltaUsuarioLineaActivity.this,
                                getResources().getString(R.string.alta_usuario_error_password), Toast.LENGTH_SHORT).show();
                    }

                } catch(Exception e) {
                    Toast.makeText(AltaUsuarioLineaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        TextView aceptarButton = (TextView) findViewById(R.id.aceptarButton);
        aceptarButton.setOnClickListener(listener);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Platform-Regular.otf");
        ((EditText) findViewById(R.id.email)).setTypeface(tf);
        ((EditText) findViewById(R.id.password)).setTypeface(tf);
        ((EditText) findViewById(R.id.confirmaPassword)).setTypeface(tf);
        ((EditText) findViewById(R.id.captcha)).setTypeface(tf);
        aceptarButton.setTypeface(tf);
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
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * Carga el captcha
     */
    public class CargarCaptchaTask extends AsyncTask<Void, Void, PeticionCaptcha> {

        CargarCaptchaTask(){
        }

        @Override
        protected PeticionCaptcha doInBackground(Void... voids) {
            RestAdapter adapter = getRestAdapter();
            CaptchasInterface service = adapter.create(CaptchasInterface.class);
            PeticionCaptcha captcha = service.crearCaptcha();
            return captcha;
        }

        @Override
        protected void onPostExecute(PeticionCaptcha captcha) {
            if(captcha != null) {
                progressCaptcha.setVisibility(View.GONE);
                String imagenString = captcha.getImagen();
                byte[] imageAsBytes = Base64.decode(imagenString.getBytes(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                mImagenCaptcha.setImageBitmap(bitmap);
                // Guardamos el id del captcha
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putLong("idPeticionCaptcha", captcha.getId());
                editor.commit();
            }
        }
    }

    /**
     * Crea un usuario temporal.
     */
    public class CrearUsuarioTask extends AsyncTask<Void, Void, Long> {

        private CrearUsuarioTemporal datos;

        CrearUsuarioTask(CrearUsuarioTemporal datos) {
            this.datos = datos;
        }

        @Override
        protected Long doInBackground(Void... params) {
            try {
                RestAdapter adapter = getRestAdapter();
                UsuarioInterface service = adapter.create(UsuarioInterface.class);
                Response res = service.crearUsuarioTemporal(datos);

                if (res.getBody() == null) {
                    mensajeServerDefault = "La respuesta del CAPTCHA es incorrecta";
                    return null;
                }

                if(res.getStatus() == 201) {
                    // Guardamos el id del pin generado
                    TypedByteArray input = (TypedByteArray) res.getBody();
                    String json = slurp(input.in(), 1024);
                    JSONObject pinObject = (JSONObject)new JSONParser().parse(json);
                    Long idPin = (Long)pinObject.get("id");
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putLong("pinId", idPin);
                    editor.commit();
                    return idPin;
                } else {
                    return null;
                }
            }catch (Exception e) {
                System.err.println("Error Crear Usuario : " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Long pin) {
            mCrearUsuarioTask = null;
            showProgress(false);
            if (pin != null && pin != 0) {
                Intent i = new Intent(AltaUsuarioLineaActivity.this, ConfirmarAltaUsuarioLineaActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (i != null) {
                    Toast.makeText(AltaUsuarioLineaActivity.this, getResources().getString(R.string.alta_usuario_pin_enviado), Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            } else {

                if (mensajeServerDefault.isEmpty()) {
                    Toast.makeText(AltaUsuarioLineaActivity.this, getResources().getString(R.string.alta_usuario_error_creacion), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AltaUsuarioLineaActivity.this, getResources().getString(R.string.alta_usuario_error_server_creacion), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mCrearUsuarioTask = null;
            showProgress(false);
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
                request.addHeader("Content-Type", "application/json");
            }
        };

        OkHttpClient okClient = new OkHttpClient();
        okClient.setConnectTimeout(15, TimeUnit.SECONDS);
        okClient.setReadTimeout(15, TimeUnit.SECONDS);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Configuraciones.MI_MUNDO_SERVER)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new ApacheClient(getNewHttpClient()))
                .build();

        return adapter;
    }

    private static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                for (;;) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            }
            finally {
                in.close();
            }
        }
        catch (UnsupportedEncodingException ex) {
        }
        catch (IOException ex) {
        }
        return out.toString();
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
