package mra.com.androidchattingapplicationfinalyear.Fragments;

import mra.com.androidchattingapplicationfinalyear.Notification.MyResponse;
import mra.com.androidchattingapplicationfinalyear.Notification.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by mr. A on 18-01-2019.
 */

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:Key=AAAAGEiuj9c:APA91bEOyPBDoW_TZonUVXQAcyKsQLZXWdU6jc1-ysVWUhOnkFC40Hxm2Vq3iL5HSAsmfJU_cwFvOYlPAP_xD-ocO0b9i35Ktco0DLhnMW9q2MzCpu3AfVsAoxilb2QDtc5VU7yUPmtI"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
