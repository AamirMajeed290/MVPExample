package example.code.mvpexample.network.model;




import example.code.mvpexample.network.model.apipojos.Results;
import example.code.mvpexample.network.model.apipojos.Wifi;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("read_single.php")
    Single<Wifi> getWifi(@Query("wifi_ssid")String wifi_ssid);

    @DELETE("delete.php")
    Single<Reply> deleteWifi(@Query("wifi_ssid")String wifi_ssid);
    @GET("read.php")
Single<Results> getAllWifies();
    @POST("create.php")
    Single<Reply> addWifi(@Body Wifi wifi);


}