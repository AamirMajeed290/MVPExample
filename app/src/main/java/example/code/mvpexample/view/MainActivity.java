package example.code.mvpexample.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.List;

import example.code.mvpexample.R;
import example.code.mvpexample.network.model.Reply;
import example.code.mvpexample.network.model.apipojos.Results;
import example.code.mvpexample.network.model.apipojos.Wifi;
import example.code.mvpexample.presenter.MainActivityContract;
import example.code.mvpexample.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements MainActivityContract.view{

private MainActivityContract.Presenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter=new MainActivityPresenter(getApplicationContext(),this);

        Wifi newrecord=new Wifi();
        newrecord.setUserId("xyzab");
        newrecord.setUserPassword("xyzab");
        newrecord.setWifiSsid("xyzab");
        newrecord.setWifiPassword("Wxyzab");



       //presenter.getWifies();
        presenter.addWifi(newrecord);
       // presenter.getWifies("abc");
        //okk



    }


    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {

        this.presenter=presenter;


    }

    public void setResults(Results results)
    {
        for(int i=0;i<results.getData().size();i++) {

            Log.i("wifiaaa", "User Id: " + results.getData().get(i).getUserId());
            Log.i("wifiaaa", "User Password: " + results.getData().get(i).getUserPassword());
            Log.i("wifiaaa", "Wifi SSID: " + results.getData().get(i).getWifiSsid());
            Log.i("wifiaaa", "Wifi Password: " + results.getData().get(i).getWifiPassword());
        }


    }

    public void setResults(Wifi wifi)
    {

       Log.i("wifiaaa", wifi.getUserId());



    }

    public void setResults(Reply reply)
    {


        Log.i("wifiaaa", reply.getMessage());

    }


    public void setErrorMessage(String errorMessage)
   {
       Log.i("error",errorMessage);
   }



    @Override
    protected void onDestroy() {
        if(presenter!=null) {
            presenter.onDestry();
        }
        super.onDestroy();
    }
}

