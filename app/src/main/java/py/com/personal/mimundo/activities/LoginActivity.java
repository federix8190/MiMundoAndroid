package py.com.personal.mimundo.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/*import io.fabric.sdk.android.Fabric;*/
//mport py.com.personal.mimundo.activities.administracion.ModificarPasswordActivity;
//import py.com.personal.mimundo.activities.administracion.ValidarCreacionUsuarioActivity;
import py.com.personal.mimundo.activities.administracion.ModificarPasswordActivity;
import py.com.personal.mimundo.activities.administracion.ValidarCreacionUsuarioActivity;
import py.com.personal.mimundo.disenhos.Configuraciones;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.models.NavDrawerItem;
import py.com.personal.mimundo.security.client.AuthorizationInterface;
import py.com.personal.mimundo.security.client.MySSLSocketFactory;
import py.com.personal.mimundo.security.client.dao.AccessTokenResponse;
import py.com.personal.mimundo.security.client.dao.ErrorOauth;
import py.com.personal.mimundo.services.ListaPaginada;
import py.com.personal.mimundo.services.SampleRetrofitSpiceService;
import py.com.personal.mimundo.services.lineas.models.Linea;
import py.com.personal.mimundo.services.lineas.models.ListaLineas;
import py.com.personal.mimundo.services.lineas.service.LineasInterface;
import py.com.personal.mimundo.services.configuracion.ConfiguracionService;
import py.com.personal.mimundo.services.configuracion.model.RtaVersion;
import py.com.personal.mimundo.services.personas.models.Persona;
import py.com.personal.mimundo.services.usuarios.models.DatosUsuarioLogueado;
import py.com.personal.mimundo.services.usuarios.models.ListaRol;
import py.com.personal.mimundo.services.usuarios.models.Rol;
import py.com.personal.mimundo.services.usuarios.models.Usuario;
import py.com.personal.mimundo.services.usuarios.service.UsuarioService;
import py.com.personal.mimundo.utils.PreferenceUtils;
import py.com.personal.mimundo.utils.StringUtils;
import py.com.personal.mimundo.disenhos.Configuraciones;
import py.com.personal.mimundo.services.SampleRetrofitSpiceService;
import py.com.personal.mimundo.utils.PreferenceUtils;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;
import retrofit.mime.TypedByteArray;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import android.net.Uri;

public class LoginActivity extends Activity {

    private EditText mUsuario;
    private EditText mPassword;
    private TextView mIngresar;
    private LinearLayout mFacebook;
    private TextView mCrearUsuarioLink;
    private TextView mModificarPasswordLink;

    private ScrollView mLoginFormView;
    private RelativeLayout contenedorBotones;
    private RelativeLayout mProgressView;

    private UserLoginTask mAuthTask;
    private UsuarioService datosUsuarioRequest;
    private List<Linea> lineas;
    private int totalLineas;
    private ConfiguracionService serviceVersion;

    @Inject
    private PreferenceUtils preferenceUtils;

    private SpiceManager spiceManager = new SpiceManager(SampleRetrofitSpiceService.class);

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

    public final int appVersion() {
        int versionNumber = -1;
        Context context = getApplicationContext();

        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNumber = pinfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Error al obtener la versión de la aplicación", Toast.LENGTH_LONG).show();
        }
        System.err.println("appVersion : " + versionNumber);
        return versionNumber;
    }

    @Override
    protected void onResume() {
        super.onResume();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        serviceVersion = new ConfiguracionService(LoginActivity.this, new JacksonConverter(objectMapper));

        // verificacion de version
        try {
            LoginActivity.this.getSpiceManager().execute(
                    serviceVersion.utlimaVersion(),
                    new ObtenerVersionAppListener()
            );
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Error al obtener la última versión de la aplicación", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferenceUtils = new PreferenceUtils(LoginActivity.this);
        inicializarVista();
        //Fabric.with(this, new Crashlytics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the
        // Home/Up button, so long as you specify a parent activity in AndroidManifest.xml
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

    private void inicializarVista() {

        System.err.println("inicializarVista");

        try {

            mUsuario = (EditText) findViewById(R.id.usuario_edit_text_login);
            mPassword = (EditText) findViewById(R.id.password_edit_text_login);
            mPassword.setTypeface(Typeface.DEFAULT);
            mPassword.setTransformationMethod(new PasswordTransformationMethod());
            mIngresar = (TextView) findViewById(R.id.ingresar_button);
            mProgressView = (RelativeLayout) findViewById(R.id.progressbar);
            mLoginFormView = (ScrollView) findViewById(R.id.form_login);
            contenedorBotones = (RelativeLayout) findViewById(R.id.contenedor_botones);
            mCrearUsuarioLink = (TextView) findViewById(R.id.crearUsuarioLink);
            mModificarPasswordLink = (TextView) findViewById(R.id.modificar_password);

            String userName = preferenceUtils.getCodigoUsuario();
            if (userName != null && !userName.isEmpty()) {
                mUsuario.setText(userName);
            }
            String password = preferenceUtils.getPassword();
            if (password != null && !password.isEmpty()) {
                mPassword.setText(password);
            }

            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/CarroisGothic-Regular.ttf");
            mIngresar.setTypeface(tf);
            mCrearUsuarioLink.setTypeface(tf);
            mModificarPasswordLink.setTypeface(tf);

            mIngresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mUsuario.getText() != null && !mUsuario.getText().toString().isEmpty()) {
                        if (mPassword.getText() != null && !mPassword.getText().toString().isEmpty()) {
                            showProgress(true);
                            mAuthTask = new UserLoginTask(mUsuario.getText().toString(), mPassword.getText().toString());
                            mAuthTask.execute((Void) null);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    getResources().getString(R.string.error_password_invalido),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this,
                                getResources().getString(R.string.error_usuario_invalido),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mCrearUsuarioLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(LoginActivity.this, ValidarCreacionUsuarioActivity.class);
                    if (i != null) {
                        startActivity(i);
                    }
                }
            });

            mModificarPasswordLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(LoginActivity.this, ModificarPasswordActivity.class);
                    if (i != null) {
                        startActivity(i);
                    }
                }
            });

        } catch (Exception e) {
            System.err.println("Error al inicializar la vista " + e.getMessage() + " - " + preferenceUtils.getNombreUsuario());
            Toast.makeText(LoginActivity.this, "Error al inicializar la vista ", Toast.LENGTH_LONG).show();
        }
    }

    public final class ObtenerVersionAppListener implements RequestListener<List> {

        public ObtenerVersionAppListener(){
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (LoginActivity.this != null) {
                Toast.makeText(LoginActivity.this, "Error al obtener la versión de la aplicación ", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(List datos) {
            if (LoginActivity.this != null && datos != null) {
                RtaVersion rta = (RtaVersion)datos.get(0);

                int versionCode = LoginActivity.this.appVersion(); //version de la app origen

                int versionVigente = Integer.parseInt(rta.getValor()); //última version vigente

                if (versionCode < versionVigente) {

                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("ATENCIóN");
                    alertDialog.setMessage("Existe una versión más reciente");
                    alertDialog.setCancelable(false);

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Descargar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    final String appPackageName = getPackageName();
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    LoginActivity.this.finish();
                                }
                            });
                    alertDialog.show();
                }
            }
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;
        private String messageError;

        UserLoginTask(String user, String password) {
            mUser = user;
            mPassword = password;
            messageError = "";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String server = Configuraciones.OAUTH_SERVER;
                String clientId = Configuraciones.CLIENT_ID;
                String redirect = Configuraciones.REDIRECT_URI;
                System.err.println("ingresar login 2 : " + mUsuario.getText());

                RestAdapter adapter = new RestAdapter.Builder().setClient(new ApacheClient(getNewHttpClient())).setEndpoint(server).build();
                AuthorizationInterface oauth = adapter.create(AuthorizationInterface.class);
                AccessTokenResponse response = oauth.authenticate("implicit", mUser, mPassword, "token", clientId, redirect, null, null);
                System.err.println("ingresar login 3 : " + response);
                if (response == null) {
                    return false;
                }

                Log.d("OAuth Prueba", response.getAccess_token());
                System.err.println("Token de session : " + response.getAccess_token());
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Authorization", response.getAccess_token());
                editor.putString("client_id", clientId);
                editor.commit();

                return true;

            } catch (RetrofitError e) {
                //showProgress(false);
                System.err.println("ingresar login error 1 : " + e.getMessage());
                try {
                    Response res = e.getResponse();
                    TypedByteArray body = (TypedByteArray) res.getBody();
                    String json = slurp(body.in(), 1024);
                    ErrorOauth error = new Gson().fromJson(json, ErrorOauth.class);
                    messageError = error.getError_description();
                    return false;
                } catch (Exception ex) {
                    messageError = getResources().getString(R.string.login_error_desconocido);
                    return false;
                }
            } catch (Exception e) {
                System.err.println("ingresar login error 2 : " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                SharedPreferences.Editor editor = pref.edit();

                if (mUser != null && mUser.charAt(0) == '0') {
                    String nroLinea = mUser.substring(1, mUser.length());
                    if (StringUtils.esLineaTelefonia(nroLinea)) {
                        editor.putString("user", nroLinea);
                    } else {
                        editor.putString("user", mUser);
                    }
                } else {
                   editor.putString("user", mUser);
                }

                //editor.putString("user", mUser);
                editor.putString("password", mPassword);
                editor.commit();
                new LogInTask(mUser, mPassword).execute();
            } else {
                showProgress(false);
                Toast.makeText(LoginActivity.this, messageError, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Guarda los datos del usuario que ha iniciado sesion e inicia el siguiente Activity.
     */
    public class LogInTask extends AsyncTask<Void, Void, Void> {

        private String mUser;
        private String password;

        public LogInTask(String user, String password) {
            mUser = user;
            this.password = password;
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (mUser != null && mUser.charAt(0) == '0') {
                String nroLinea = mUser.substring(1, mUser.length());
                if (StringUtils.esLineaTelefonia(nroLinea)) {
                    preferenceUtils.guardarCodigoUsuario(nroLinea);
                } else {
                    preferenceUtils.guardarCodigoUsuario(mUser);
                }
            } else {
                preferenceUtils.guardarCodigoUsuario(mUser);
            }
            preferenceUtils.guardarPassword(password);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            new ObtenerLineasTask().execute();
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    public class ObtenerLineasTask extends AsyncTask<Void, Void, ListaPaginada<Linea>> {

        @Override
        protected ListaPaginada<Linea> doInBackground(Void... params) {
            try {
                RestAdapter adapter = getRestAdapter(true);
                LineasInterface service = adapter.create(LineasInterface.class);
                ListaPaginada<Linea> datos = service.obtenerLineas(-1);
                return datos;
            } catch (RuntimeException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ListaPaginada<Linea> lista) {
            if (lista != null) {
                // Setea el menu seleccionado al Home
                NavDrawerItem.padreSeleccionado = 0;
                NavDrawerItem.hijoSeleccionado = -1;

                // Cargar lineas del usuario
                lineas = lista.getLista();
                totalLineas = lista.getCantidad();
                System.err.println("Lineas usuario : " + lineas.size());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
                datosUsuarioRequest = new UsuarioService(LoginActivity.this, new JacksonConverter(objectMapper), true);
                getSpiceManager().execute(datosUsuarioRequest.datosUsuarioLogueado(), new DatosUsuarioListener());

            } else {
                Toast.makeText(LoginActivity.this, MensajesDeUsuario.ERROR_OBTENER_LINEAS_USUARIO, Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    public final class DatosUsuarioListener implements RequestListener<DatosUsuarioLogueado> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showProgress(false);
            Toast.makeText(LoginActivity.this, MensajesDeUsuario.ERROR_OBTENER_DATOS_USUARIO, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final DatosUsuarioLogueado datosUsuarioLogueado) {
            if (datosUsuarioLogueado != null) {
                Persona persona = datosUsuarioLogueado.getPersona();
                Usuario user = datosUsuarioLogueado.getUsuario();
                preferenceUtils = new PreferenceUtils(LoginActivity.this);
                if (lineas.size() > 0) {
                    preferenceUtils.guardarLineaSeleccionada(lineas.get(0).getNumeroLinea());
                } else {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, MensajesDeUsuario.ERROR_OBTENER_DATOS_USUARIO, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (user != null) {
                    preferenceUtils.guardarTipoPersona(user.getPersoneria());
                }
                if (persona != null) {
                    //preferenceUtils.guardarTipoPersona(persona.getTipoPersona());
                    preferenceUtils.guardarNombreUsuario(user.getNombres());
                    preferenceUtils.guardarTipoUsuario(user.getTipoUsuario());

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("TipoUsuario", user.getTipoUsuario());
                    editor.commit();

                    getSpiceManager().execute(datosUsuarioRequest.obtenerRoles(user.getUsuario()), new RolesUsuarioListener());
                } else {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, MensajesDeUsuario.ERROR_OBTENER_DATOS_USUARIO, Toast.LENGTH_SHORT).show();
                }
            } else {
                showProgress(false);
                Toast.makeText(LoginActivity.this, MensajesDeUsuario.ERROR_OBTENER_DATOS_USUARIO, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        }
    }

    public final class RolesUsuarioListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showProgress(false);
            Toast.makeText(LoginActivity.this, MensajesDeUsuario.ERROR_OBTENER_ROLES_USUARIO, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final List lista) {
            showProgress(false);
            if (lista != null) {
                // Guardamos los roles del usuario
                List<Rol> roles = lista;
                ListaRol listaRoles = new ListaRol(roles);
                String jsonListaRoles = new Gson().toJson(listaRoles);
                preferenceUtils = new PreferenceUtils(LoginActivity.this);
                preferenceUtils.guardarRolesUsuario(jsonListaRoles);
                // Llamamos al siguiente Activity
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ListaLineas listaLineas = new ListaLineas(lineas);
                String jsonListaLineas = new Gson().toJson(listaLineas);
                Bundle b = new Bundle();
                b.putString("lineas", jsonListaLineas);
                b.putInt("totalLineas", totalLineas);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, MensajesDeUsuario.ERROR_OBTENER_ROLES_USUARIO, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow for very
        // easy animations. If available, use these APIs to fade-in the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            if (mLoginFormView != null) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
            }

            if (contenedorBotones != null) {
                contenedorBotones.setVisibility(show ? View.GONE : View.VISIBLE);
                contenedorBotones.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        contenedorBotones.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
            }

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            if (mLoginFormView != null) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
            if (contenedorBotones != null) {
                contenedorBotones.setVisibility(show ? View.VISIBLE : View.GONE);
            }
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public RestAdapter getRestAdapter(final boolean autenticar) {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
        boolean tokenGenerado = false;

        // Verificar si el Token no ha expirado
        if(autenticar) {
            Date fechaActual = new Date();
            Long fechaUltimoLogin = pref.getLong("UltimoLogin", 0);
            Long timestamp = pref.getLong("Timestamp", 0);
            if(fechaUltimoLogin > 0) {
                if ((fechaActual.getTime() - fechaUltimoLogin) > timestamp) {
                    generarToken(pref);
                    tokenGenerado = true;
                }
            } else {
                generarToken(pref);
                tokenGenerado = true;
            }
        }

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                if(autenticar) {
                    request.addHeader("Authorization", pref.getString("Authorization", ""));
                }
                request.addHeader("client_id", Configuraciones.CLIENT_ID);
            }
        };

        OkHttpClient okClient = new OkHttpClient();
        okClient.setConnectTimeout(15, TimeUnit.SECONDS);
        okClient.setReadTimeout(15, TimeUnit.SECONDS);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Configuraciones.MI_MUNDO_SERVER)
                .setRequestInterceptor(requestInterceptor)
                //.setClient(new OkClient(okClient))
                .setClient(new ApacheClient(getNewHttpClient()))
                .build();

        // Controlar la validez del token de session
        if(autenticar && !tokenGenerado) {
            try {
                LineasInterface service = adapter.create(LineasInterface.class);
                service.obtenerLineas(1);
            } catch (Exception e) {
                if(e.getMessage().equals(getResources().getString(R.string.error_token_session_invalido))) {
                    generarToken(pref);
                }
            }
        }

        return adapter;
    }

    public String generarToken(final SharedPreferences pref) {
        try {
            String server = Configuraciones.OAUTH_SERVER;
            String clientId = Configuraciones.CLIENT_ID;
            String redirect = Configuraciones.REDIRECT_URI;
            String user = pref.getString("user", "");
            String password = pref.getString("password", "");

            RestAdapter adapter = new RestAdapter.Builder()
                    .setClient(new ApacheClient(getNewHttpClient()))
                    .setEndpoint(server)
                    .build();
            AuthorizationInterface oauth = adapter.create(AuthorizationInterface.class);
            AccessTokenResponse response = oauth.authenticate("implicit", user, password, "token",
                    clientId, redirect, null, null);

            // Guardar nuevo token
            if (response != null && response.getAccess_token() != null) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putLong("UltimoLogin", new Date().getTime());
                editor.putLong("Timestamp", response.getExpires_in());
                editor.putString("Authorization", response.getAccess_token());
                editor.putBoolean("pinSesion", false);
                editor.commit();
            }

            return response.getAccess_token();

        } catch (RetrofitError e) {
            try {
                Response res = e.getResponse();
                TypedByteArray body = (TypedByteArray) res.getBody();
                String json = slurp(body.in(), 1024);
                ErrorOauth error = new Gson().fromJson(json, ErrorOauth.class);
                Toast.makeText(LoginActivity.this, error.getError_description(), Toast.LENGTH_SHORT).show();
                return null;
            } catch (Exception ex) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_error_desconocido),
                        Toast.LENGTH_SHORT).show();
                return null;
            }
        }
    }

    private String slurp(final InputStream is, final int bufferSize) {
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
