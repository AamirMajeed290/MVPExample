package example.code.mvpexample.network.model.apipojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {

    @SerializedName("data")
    @Expose
    private List<Wifi> data = null;

    public List<Wifi> getData() {
        return data;
    }

    public void setData(List<Wifi> data) {
        this.data = data;
    }


}
