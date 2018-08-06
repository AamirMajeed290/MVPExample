package example.code.mvpexample.network.model.apipojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Wifi {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("wifi_ssid")
    @Expose
    private String wifiSsid;
    @SerializedName("wifi_password")
    @Expose
    private String wifiPassword;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_password")
    @Expose
    private String userPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWifiSsid() {
        return wifiSsid;
    }

    public void setWifiSsid(String wifiSsid) {
        this.wifiSsid = wifiSsid;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

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
        this.userPassword = userPassword;}
}



