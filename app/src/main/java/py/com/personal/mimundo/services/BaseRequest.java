package py.com.personal.mimundo.services;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.octo.android.robospice.persistence.DurationInMillis;
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
import java.util.concurrent.TimeUnit;

import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.Configuraciones;
import py.com.personal.mimundo.security.client.AuthorizationInterface;
import py.com.personal.mimundo.security.client.MySSLSocketFactory;
import py.com.personal.mimundo.security.client.dao.ErrorOauth;
import py.com.personal.mimundo.security.client.dao.AccessTokenResponse;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.mime.TypedByteArray;

/**
 * Created by Konecta on 11/09/2014.
 */
public class BaseRequest {

    private static final String MSG_EXPIRO = "No se encontraron sesiones activas para el token";

    protected RestAdapter adapter;
    protected final long CACHE_EXPIRE = DurationInMillis.ONE_MINUTE * 30;
    protected Activity context;
    protected Converter converter;

    public BaseRequest(Activity context, Converter converter) {
        this.context = context;
        this.converter = converter;

        final SharedPreferences pref = context.getSharedPreferences("MiMundoPreferences", 0);
        System.err.println("Token : " + pref.getString("Authorization", ""));

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", pref.getString("Authorization", ""));
                request.addHeader("client_id", Configuraciones.CLIENT_ID);
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
            }
        };

        // create client
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(90 * 1000, TimeUnit.MILLISECONDS);

        adapter = new RestAdapter.Builder()
                .setEndpoint(Configuraciones.MI_MUNDO_SERVER)
                .setConverter(converter)
                //.setClient(new OkClient(okHttpClient))
                .setClient(new ApacheClient(getNewHttpClient()))
                .setRequestInterceptor(requestInterceptor)
                .build();
    }

    public BaseRequest(Activity context, Converter converter, boolean autenticacion) {
        this.context = context;
        this.converter = converter;

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("client_id", Configuraciones.CLIENT_ID);
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
           }
        };

        if (autenticacion) {
            final SharedPreferences pref = context.getSharedPreferences("MiMundoPreferences", 0);
            requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Authorization", pref.getString("Authorization", ""));
                    request.addHeader("client_id", Configuraciones.CLIENT_ID);
                    request.addHeader("Content-Type", "application/json");
                    request.addHeader("Accept", "application/json");
                }
            };
        }

        // create client
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(90 * 1000, TimeUnit.MILLISECONDS);

        adapter = new RestAdapter.Builder()
                .setEndpoint(Configuraciones.MI_MUNDO_SERVER)
                .setConverter(converter)
                //.setClient(new OkClient(okHttpClient))
                .setClient(new ApacheClient(getNewHttpClient()))
                .setRequestInterceptor(requestInterceptor)
                .build();
    }

    protected boolean expiroToken(Exception e, Activity context, Converter converter) {
        if (e.getMessage() != null && (e.getMessage().contains("500") || e.getMessage().contains("401"))) {
            try {
                Response res = ((RetrofitError) e).getResponse();
                TypedByteArray body = (TypedByteArray) res.getBody();
                String json = slurp(body.in(), 1024);
                // Controlar si realmente expiro el token
                if ((e.getMessage().contains("500") && json != null && json.contains(MSG_EXPIRO))
                        || e.getMessage().contains("401")) {
                    generarToken(context);
                    final SharedPreferences pref = context.getSharedPreferences("MiMundoPreferences", 0);
                    RequestInterceptor requestInterceptor = new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addHeader("Authorization", pref.getString("Authorization", ""));
                            request.addHeader("client_id", Configuraciones.CLIENT_ID);
                        }
                    };
                    adapter = new RestAdapter.Builder()
                            .setEndpoint(Configuraciones.MI_MUNDO_SERVER)
                            .setConverter(converter)
                            .setClient(new ApacheClient(getNewHttpClient()))
                            .setRequestInterceptor(requestInterceptor)
                            .build();
                    return true;
                }
                return false;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }

    private void generarToken(Activity context) {
        try {
            String server = Configuraciones.OAUTH_SERVER;
            final SharedPreferences pref = context.getSharedPreferences("MiMundoPreferences", 0);
            String clientId = Configuraciones.CLIENT_ID;
            String redirect = Configuraciones.REDIRECT_URI;
            String user = pref.getString("user", "");
            String password = pref.getString("password", "");
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(server)
                    .setClient(new ApacheClient(getNewHttpClient()))
                    .build();
            AuthorizationInterface oauth = adapter.create(AuthorizationInterface.class);
            AccessTokenResponse response = oauth.authenticate("implicit", user, password, "token", clientId, redirect, null, null);
            if (response != null && response.getAccess_token() != null) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Authorization", response.getAccess_token());
                editor.putLong("UltimoLogin", new Date().getTime());
                editor.putLong("Timestamp", response.getExpires_in());
                editor.putBoolean("pinSesion", false);
                editor.commit();
            }
        } catch (RetrofitError e) {
            try {
                Response res = e.getResponse();
                TypedByteArray body = (TypedByteArray) res.getBody();
                String json = slurp(body.in(), 1024);
                ErrorOauth error = new Gson().fromJson(json, ErrorOauth.class);
                Toast.makeText(context, error.getError_description(), Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(context, "Error, intente de nuevo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected Resultado procesarError(RetrofitError e) {
        try {
            Response res = e.getResponse();
            TypedByteArray body = (TypedByteArray) res.getBody();
            String json = slurp(body.in(), 1024);
            ErrorResponse error = new Gson().fromJson(json, ErrorResponse.class);
            String messageError = error.getMensaje();
            return new Resultado(false, messageError);
        } catch (Exception ex) {
            String messageError = context.getResources().getString(R.string.error_desconocido);
            return new Resultado(false, messageError);
        }
    }

    protected String slurp(final InputStream is, final int bufferSize) {
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
            System.err.println("Error : " + ex.getMessage());
        }
        catch (IOException ex) {
            System.err.println("Error : " + ex.getMessage());
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
