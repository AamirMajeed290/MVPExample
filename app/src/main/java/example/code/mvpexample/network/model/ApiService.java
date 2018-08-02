package example.code.mvpexample.network.model;



import example.code.mvpexample.network.model.apipojos.Results;
import example.code.mvpexample.network.model.apipojos.Wifi;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("wifi/{sSID}/")
    Single<Wifi> getWifi(@Path("sSID")String sSID);
    @GET("wifi/")
Single<Results> getAllWifies();


}