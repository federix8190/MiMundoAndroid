package py.com.personal.mimundo.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

/*import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import py.com.testmundo.Constantes;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;*/

import java.io.IOException;

public class ServiceBuilder {

    /*public static Object create(Class<?> cls, final String token) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", token)
                        .header("client_id", Constantes.CLIENT_ID)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.MI_MUNDO_SERVER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit.create(cls);
    }*/
}
