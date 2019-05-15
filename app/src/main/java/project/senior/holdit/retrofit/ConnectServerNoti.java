package project.senior.holdit.retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectServerNoti {
    public static retrofit2.Retrofit RETROFIT = null;

    public static retrofit2.Retrofit getClient(String url){
        if (RETROFIT == null){
            RETROFIT = new retrofit2.Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return RETROFIT;
    }
}
