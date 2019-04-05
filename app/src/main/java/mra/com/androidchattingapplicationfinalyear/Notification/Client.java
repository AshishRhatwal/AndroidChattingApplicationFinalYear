package mra.com.androidchattingapplicationfinalyear.Notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mr. A on 18-01-2019.
 */

public class Client
{
    public static Retrofit retrofit=null;

    public static Retrofit getClient(String uri)
    {
        if(retrofit == null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(uri)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
