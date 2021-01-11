package py.com.personal.mimundo.services;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.retrofit.RetrofitObjectPersisterFactory;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import py.com.personal.mimundo.activities.R;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;

import py.com.personal.mimundo.disenhos.Configuraciones;
import retrofit.converter.Converter;
import retrofit.converter.JacksonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * Created by Konecta on 09/09/2014.
 */
public class SampleRetrofitSpiceService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = Configuraciones.MI_MUNDO_SERVER;
    private final OkHttpClient client = new OkHttpClient();

    // TODO inject - configuration
    Converter converter = new JacksonConverter(new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));

    // TODO inject - cache folder
    File cacheFolder = null;

    public static final String NOTIFICATION_CHANNEL_ID_SERVICE = "py.com.personal.mimundo.services.MyService";
    public static final String NOTIFICATION_CHANNEL_ID_INFO = "py.com.personal.mimundo.download_info";

    @Override
    public void onCreate() {
        super.onCreate();
        cacheFolder = new File(getCacheDir(), "HttpCache");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, "App Service", NotificationManager.IMPORTANCE_DEFAULT));
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_INFO, "Download Info", NotificationManager.IMPORTANCE_DEFAULT));
        } else {
            Notification notification = new Notification();
            startForeground(1, notification);
        }

        /*addRetrofitInterface(LineasInterface.class);
        addRetrofitInterface(GruposFacturacionInterface.class);
        addRetrofitInterface(FinanciacionesInterface.class);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.lauch_app)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        RetrofitObjectPersisterFactory persisterFactory = null;
        try {
            persisterFactory = new RetrofitObjectPersisterFactory(application, converter, cacheFolder);
        } catch (CacheCreationException e) {
            e.printStackTrace();
        }
        cacheManager.addPersister(persisterFactory);
        return cacheManager;
    }

    @Override
    public Builder createRestAdapterBuilder() {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MiMundoPreferences", 0);
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(15, TimeUnit.SECONDS);

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", pref.getString("Authorization", ""));
                request.addHeader("client_id", Configuraciones.CLIENT_ID);
                request.addHeader("Cache-Control", "max-stale=3600");
            }
        };

        RestAdapter.Builder adapter = new RestAdapter.Builder()
                .setEndpoint(Configuraciones.MI_MUNDO_SERVER)
                .setRequestInterceptor(requestInterceptor);
                //.setClient(new OkClient(client));

        return adapter;
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

}

