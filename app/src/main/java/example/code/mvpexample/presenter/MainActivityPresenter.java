package example.code.mvpexample.presenter;

import android.content.Context;


import org.json.JSONArray;
import org.json.JSONObject;

import example.code.mvpexample.network.model.ApiClient;
import example.code.mvpexample.network.model.ApiService;
import example.code.mvpexample.network.model.Reply;
import example.code.mvpexample.network.model.apipojos.Results;
import example.code.mvpexample.network.model.apipojos.Wifi;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivityPresenter implements MainActivityContract.Presenter {

    CompositeDisposable compositeDisposable=new CompositeDisposable();
    private ApiService apiService;

    private Context context;
    private MainActivityContract.view view;

    public MainActivityPresenter(Context context, MainActivityContract.view view) {
        this.view=view;
        this.context=context;
        view.setPresenter(this);
    }

    public void getWifies()
    {

        apiService= ApiClient.getClient(context).create(ApiService.class);
        compositeDisposable.add(

                   apiService.getAllWifies()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Results>()

                                       {

                                           @Override
                                           public void onSuccess(Results results) {
                                           view.setResults(results);



                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               view.setErrorMessage(e.getMessage());
                                           }
                                       }
                        )



        );
    }


    public void getWifies(String d)
    {

        apiService= ApiClient.getClient(context).create(ApiService.class);
        compositeDisposable.add(

                apiService.getWifi(d)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Wifi>()

                                       {

                                           @Override
                                           public void onSuccess(Wifi wifi) {
                                               view.setResults(wifi);



                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               view.setErrorMessage(e.getMessage());
                                           }
                                       }
                        )



        );
    }


    public void addWifi(Wifi wifi)
    {

        apiService= ApiClient.getClient(context).create(ApiService.class);
        compositeDisposable.add(

                apiService.addWifi(wifi)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Reply>()

                                       {

                                           @Override
                                           public void onSuccess(Reply reply) {
                                               view.setResults(reply);




                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               view.setErrorMessage(e.getMessage());
                                           }
                                       }
                        )



        );
    }


    @Override
    public void onDestry() {
        compositeDisposable.dispose();
    }
}
