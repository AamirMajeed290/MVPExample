package example.code.mvpexample.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import example.code.mvpexample.R;
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

        presenter.getWifies();
        //presenter.getWifies("f45df136seXZ");
        //ssssssssss



    }


    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {

        this.presenter=presenter;


    }

    public void setResults(Results results)
    {
        //this.starWarCharacter=starWarCharacter;
        Log.i("wifiaaa", results.getResults().get(0).getWifiId().toString());


    }

    public void setResults(Wifi wifi)
    {
        //this.starWarCharacter=starWarCharacter;
       Log.i("wifiaaa", wifi.getUserId());


    }



    public void setErrorMessage(String errorMessage)
   {
       Log.i("wifi",errorMessage);
   }



    @Override
    protected void onDestroy() {
        if(presenter!=null) {
            presenter.onDestry();
        }
        super.onDestroy();
    }
}

