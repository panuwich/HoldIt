package project.senior.holdit.retrofit;

import project.senior.holdit.model.NotiResponse;
import project.senior.holdit.model.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotiApi {
    @Headers({
                "Content-Type:application/json",
                "Authorization:key=AAAAm3QH5PA:APA91bGhO9ECLWBgkrr5HMn8Rjo_s37BpAbGB-E_eLt8PJrtrciParTCsavu8VMp1ZH69-mZUU79HJRPL5St2k9-_eiAuDrUC76-eqThqtMQco9vVeI-9-n53PqUBRGyUJlTRwlAHsQA"
    })

    @POST("fcm/send")
    Call<NotiResponse> sendNotification(@Body Sender body);
}
