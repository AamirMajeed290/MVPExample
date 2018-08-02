package example.code.mvpexample.network.model.apipojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Wifi {



    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userPassword")
    @Expose
    private String userPassword;
    @SerializedName("wifiId")
    @Expose
    private String wifiId;
    @SerializedName("wifiPassword")
    @Expose
    private String wifiPassword;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getWifiId() {
        return wifiId;
    }

    public void setWifiId(String wifiId) {
        this.wifiId = wifiId;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

}



