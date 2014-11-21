package beltyukov.me.pushnotificationsspike;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;

public interface NotificationServiceApi {
    @POST("/registrations")
    Response registerUser(@Body User user);

    @POST("/notifications/send")
    Response sendNotification(@Query("deviceType") String deviceType, @Query("oas") Integer oas);
}
