package example.code.mvpexample.presenter;

import org.json.JSONArray;
import org.json.JSONObject;

import example.code.mvpexample.BasePresenter;
import example.code.mvpexample.BaseView;

import example.code.mvpexample.network.model.Reply;
import example.code.mvpexample.network.model.apipojos.Results;
import example.code.mvpexample.network.model.apipojos.Wifi;


public interface MainActivityContract {

    interface Presenter extends BasePresenter  {
       //void onDestry();  // we can write this method here id we dont want to extend BasePresenter calss
        void getWifies(String d);
        void getWifies();
        void addWifi(Wifi wifi);
        void deleteWifi(String s);



    }



    interface view extends BaseView <Presenter>
    {

        void setResults(Wifi wifi);
        void setResults(Reply reply);
        void setResults(Results results);
        void setErrorMessage(String error);




    }

/*

this interface can be implemented to avoid BaseView extention
    interface view <Presenter>
    {
        void setStarWarCharacter(StarWarCharacter starWarCharacter);
        void setErrorMessage(String error);
        void setPresenter(MainActivityContract.Presenter presenter);


    }
*/

}
