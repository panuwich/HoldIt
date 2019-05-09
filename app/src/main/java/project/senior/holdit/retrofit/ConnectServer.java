package project.senior.holdit.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectServer {
    public static final String URL = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/";
    public static retrofit2.Retrofit RETROFIT = null;

    public static retrofit2.Retrofit getClient(){
        if (RETROFIT == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor())
                    .build();



            Gson gson = new GsonBuilder()
                    .setLenient().create();

            RETROFIT = new retrofit2.Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return RETROFIT;
    }
}
