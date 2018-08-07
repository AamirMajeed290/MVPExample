package example.code.mvpexample.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



import example.code.mvpexample.R;
import example.code.mvpexample.network.model.Reply;
import example.code.mvpexample.network.model.apipojos.Results;
import example.code.mvpexample.network.model.apipojos.Wifi;
import example.code.mvpexample.presenter.MainActivityContract;
import example.code.mvpexample.presenter.MainActivityPresenter;

import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityContract.view {


    String ssid;
    String connectedSsid;
  ImageView imageView;
    String password;
    private Element[] nets;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;
    private TextView tv;

    Dialog dialog;

    private MainActivityContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        presenter = new MainActivityPresenter(getApplicationContext(), this);
        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        connectedSsid=wifiInfo.getSSID();




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv = (TextView) findViewById(R.id.textView);
                tv.setText("");

                detectWifi();
                Snackbar.make(view, "Scanning wifi ...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//presenter.getWifies();

        // presenter.getWifies("abc");
        //okk


    }


    public void detectWifi() {


        this.wifiManager.startScan();
        this.wifiList = this.wifiManager.getScanResults();


        Toast.makeText(this, "total wifies: " + wifiList.size(), Toast.LENGTH_SHORT).show();

        this.nets = new Element[wifiList.size()];
        for (int i = 0; i < wifiList.size(); i++) {



                nets[i] = new Element(wifiList.get(i).SSID);




        }

        AdapterElements adapterElements = new AdapterElements(this);
        ListView netList = (ListView) findViewById(R.id.lv_ssid);
        netList.setAdapter(adapterElements);
        netList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showdialow(nets[i].getTitle());
            }
        });

        //showdialow();
    }


    public void showdialow(final String ten_wifi) {
        ssid=ten_wifi;
        dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Wifi Password");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);

        Button btnSubmit = (Button) dialog.findViewById(R.id.submit);
        Button btnCancel = (Button) dialog.findViewById(R.id.cancel);
        final EditText edt_Password = (EditText) dialog.findViewById(R.id.edt_password);

        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.cb_show);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    edt_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    edt_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                 password = edt_Password.getText().toString();

                if (TextUtils.isEmpty(password)) {
                    edt_Password.setError("Please Enter Password");
                } else {

                    if(setSsidAndPassword(getApplicationContext(),ssid,password))

                    {

                        presenter.getWifies(ssid);
                    }

                    dialog.dismiss();

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    class AdapterElements extends ArrayAdapter<Object> {
        Activity context;

        public AdapterElements(Activity context) {
            super(context, R.layout.items, nets);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();

            View item = inflater.inflate(R.layout.items, null);
            TextView tvSsid = (TextView) item.findViewById(R.id.tvSSID);
            ImageView imageView=(ImageView)item.findViewById(R.id.imageView2);
            if(('"'+nets[position].getTitle().toString()+'"').equals(connectedSsid.trim()))
            {
                imageView.setImageResource(R.drawable.connectedwifi);
            }


                tvSsid.setText(nets[position].getTitle());



            return item;
        }
    }

    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {

        this.presenter = presenter;


    }

    public void setResults(Results results) {
        for (int i = 0; i < results.getData().size(); i++) {

            Log.i("wifiaaa", "User Id: " + results.getData().get(i).getUserId());
            Log.i("wifiaaa", "User Password: " + results.getData().get(i).getUserPassword());
            Log.i("wifiaaa", "Wifi SSID: " + results.getData().get(i).getWifiSsid());
            Log.i("wifiaaa", "Wifi Password: " + results.getData().get(i).getWifiPassword());
        }


    }

    public void setResults(Wifi wifi) {

        Log.i("wififound", "User Id: " + wifi.getWifiSsid());

        if(!ssid.equals(wifi.getWifiSsid())) {

            Wifi newrecord = new Wifi();
            newrecord.setUserId(getUniqueID());
            newrecord.setUserPassword("Default");
            newrecord.setWifiSsid(ssid);
            newrecord.setWifiPassword(password);

            presenter.addWifi(newrecord);


        }



    }

    public void setResults(Reply reply) {


        Log.i("wifiaaa", reply.getMessage());

    }


    public void setErrorMessage(String errorMessage) {
        Log.i("error", errorMessage);
    }


    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestry();
        }
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
    }
    public String getUniqueID(){
        String myAndroidDeviceId = "Default";
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {

            if (mTelephony.getDeviceId() != null) {
                myAndroidDeviceId = mTelephony.getDeviceId();
            } else {
                myAndroidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return myAndroidDeviceId;
    }

//need fixingsss
    public boolean setSsidAndPassword(Context context, String ssid3, String ssidPassword) {
        try {

            Method getConfigMethod = this.wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(this.wifiManager);


            Log.i("wifipass",wifiConfig.preSharedKey.toString());


            wifiConfig.SSID = ssid3;
            wifiConfig.preSharedKey = ssidPassword;

            Method setConfigMethod = this.wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setConfigMethod.invoke(this.wifiManager, wifiConfig);
            int netId = wifiManager.addNetwork(wifiConfig);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

            return true;
        } catch (Exception e) {

            return false;
        }
    }


/*
    public String currentPassword() {

        String password="";
        try {

            Method getConfigMethod = this.wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(this.wifiManager);
            password=wifiConfig.preSharedKey.toString();


        }

        catch (Exception e) {

          password=e.getMessage();
        }
        return password;
    }

    */
}

