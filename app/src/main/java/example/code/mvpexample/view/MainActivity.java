package example.code.mvpexample.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import example.code.mvpexample.Utils.WifiHelper;
import example.code.mvpexample.network.model.Reply;
import example.code.mvpexample.network.model.apipojos.Results;
import example.code.mvpexample.network.model.apipojos.Wifi;
import example.code.mvpexample.presenter.MainActivityContract;
import example.code.mvpexample.presenter.MainActivityPresenter;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MainActivityContract.view {

    SwipeRefreshLayout swipeLayout;
    TextView mTextView;
    String ssid;
    NetworkInfo wifi;
    NetworkInfo mobile;
    WifiConfiguration wc = null;
    WifiInfo wifiInfo;
    int idOfNetwork;
    List<Wifi> savedWifies;
    List<ScanResult> sortedWifiList;
    List<Wifi> availableWifies;
    String bssid;
    ImageView imageView;
    String password = "";


    private WifiManager wifiManager;
    private List<ScanResult> wifiList;
    Dialog addWifiDialog;


    Dialog dialog;

    private MainActivityContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView=findViewById(R.id.textView);
        swipeLayout = findViewById(R.id.swipe_container);
        presenter = new MainActivityPresenter(getApplicationContext(), this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        this.wifiInfo = wifiManager.getConnectionInfo();
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
       this.wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
       this.mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()) {
            setBssid(wifiInfo.getBSSID().replaceAll("\"", ""));
            setSsid(wifiInfo.getSSID().replaceAll("\"", ""));
            presenter.getWifies(getSsid());
        }
        if (wifi.isConnected() || mobile.isConnected()) {
            presenter.getWifies();
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getWifies(getSsid());
                makeSnackBar(swipeLayout, "Scanning wifi ...", "ACTION");
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getWifies(getSsid());

                makeSnackBar(swipeLayout, "Scanning wifi ...", "ACTION");

                // To keep animation for 1 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000); // Delay in millis
            }
        });
    }


    public void detectWifi() {

        this.wifiManager.startScan();
        this.wifiList = this.wifiManager.getScanResults();

        // Create Temporary HashMap
        HashMap<String, ScanResult> map =
                new HashMap<>();

        for (ScanResult scanResult : wifiList) {
            if (scanResult.SSID != null &&
                    !scanResult.SSID.isEmpty()) {
                map.put(scanResult.SSID, scanResult);
            }
        }

// Add to new List
        sortedWifiList = new ArrayList<ScanResult>(map.values());

        // Create Comparator to sort by level
        Comparator<ScanResult> comparator =
                new Comparator<ScanResult>() {

                    @Override
                    public int compare(ScanResult lhs, ScanResult rhs) {
                        return (lhs.level < rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
                    }
                };

// Apply Comparator and sort
        Collections.sort(sortedWifiList, comparator);
        availableWifies = new ArrayList<>();


        for (int i = 0; i <= sortedWifiList.size() - 1; i++) {

            for (int j = 0; j <= savedWifies.size() - 1; j++) {


                if (sortedWifiList.get(i).SSID.replaceAll("\"", "").equals(savedWifies.get(j).getWifiSsid())) {
                    availableWifies.add(savedWifies.get(j));

                }

            }


        }

        if(availableWifies.size()>0)
        {

            if(availableWifies.size()==1)
            {
                mTextView.setText("1 wifi is available for free here");
            }
            else {
                mTextView.setText(availableWifies.size() + " wifies are available for free here");
            }
        }


        AdapterElements adapterElements = new AdapterElements(this);
        ListView netList = (ListView) findViewById(R.id.lv_ssid);
        netList.setAdapter(adapterElements);
        netList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                showdialow(availableWifies.get(i).getWifiSsid(), availableWifies.get(i).getUserId(), availableWifies.get(i).getWifiPassword());
            }
        });

    }


    public void showdialow(final String ssid, final String bssid, final String wifiPassword) {

        setSsid(ssid);
        setBssid(bssid);

        dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Connect");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);
        TextView wifiSsidTextView = (TextView) dialog.findViewById(R.id.wifiSsid);
        wifiSsidTextView.setText(getSsid());
        Button btnSubmit = (Button) dialog.findViewById(R.id.submit);
        Button btnCancel = (Button) dialog.findViewById(R.id.cancel);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


              List<WifiConfiguration> wifiConfiguration=  wifiManager.getConfiguredNetworks();



                  if (!wifiInfo.getSSID().replaceAll("\"", "").equals(getSsid())) {
                      if (wc != null) {
                          wifiManager.removeNetwork(wc.networkId);
                      }

                      setPassword(wifiPassword);

                      wc = new WifiConfiguration();
                      wc.SSID = "\"" + getSsid() + "\"";
                      wc.preSharedKey = "\"" + getPassword() + "\"";
                      wc.hiddenSSID = true;
                      wc.status = WifiConfiguration.Status.ENABLED;

                      wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                      wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                      wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                      wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                      wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                      wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

                      int res = wifiManager.addNetwork(wc);

                      wifiManager.disconnect();


                      if (wifiManager.enableNetwork(res, true)) {
                          wifiManager.reconnect();


                          WifiHelper.setWifiListener(new WifiHelper.WifiConnectionChange() {
                              @Override
                              public void wifiConnected(boolean connected) {


                                  detectWifi();


                              }
                          });

                      } else

                          {

                              presenter.deleteWifi(wc.SSID.replaceAll("\"", ""));


                      }


                  } else {
                      wifiManager.disconnect();

                      if(wifiManager.enableNetwork(wifiInfo.getNetworkId(),true)) {
                          wifiManager.reconnect();


                          WifiHelper.setWifiListener(new WifiHelper.WifiConnectionChange() {
                              @Override
                              public void wifiConnected(boolean connected) {


                                  detectWifi();


                              }
                          });


                      }
              }
                dialog.dismiss();

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
            super(context, R.layout.items, availableWifies.toArray());
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();

            View item = inflater.inflate(R.layout.items, null);
            TextView tvSsid = (TextView) item.findViewById(R.id.tvSSID);
            imageView = (ImageView) item.findViewById(R.id.imageView2);
            WifiInfo wifiInfow = wifiManager.getConnectionInfo();

            String ssid2 = wifiInfow.getSSID();
            ssid2 = ssid2.replaceAll("\"", "");
            if (availableWifies.get(position).getWifiSsid().equals(ssid2) && (availableWifies.get(position).getWifiSsid()) != null) {
                imageView.setImageResource(R.drawable.connectedwifi);
            }

            tvSsid.setText(availableWifies.get(position).getWifiSsid());

            return item;
        }
    }


    public void addWifiDialog() {

        addWifiDialog = new Dialog(MainActivity.this);
        addWifiDialog.setCancelable(false);
        addWifiDialog.setCanceledOnTouchOutside(false);
        addWifiDialog.setTitle("Wifi Password");
        addWifiDialog.setCancelable(true);
        addWifiDialog.setContentView(R.layout.add_wifi_dialog);
        Button btnSubmit = (Button) addWifiDialog.findViewById(R.id.submit);

        TextView mTextViewCurrentWifi=(TextView)addWifiDialog.findViewById(R.id.CurrentWifiSsid);
        mTextViewCurrentWifi.setText(getSsid());

        final EditText edt_Password = (EditText) addWifiDialog.findViewById(R.id.edt_password);

        final CheckBox checkBox1 = (CheckBox) addWifiDialog.findViewById(R.id.cb_show1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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



            public void onClick(View v) {


                String enteredPassword = edt_Password.getText().toString();

                if (TextUtils.isEmpty(enteredPassword)) {
                    edt_Password.setError("Wrong password");
                } else {
                        setPassword(enteredPassword);

                              Wifi newrecord = new Wifi();
                              newrecord.setUserId(getBssid());
                              newrecord.setUserPassword("Default");
                              newrecord.setWifiSsid(getSsid());
                              newrecord.setWifiPassword(getPassword());
                              presenter.addWifi(newrecord);
                              Wifi networkWifi=new Wifi();
                            detectWifi();


                   addWifiDialog.dismiss();

                }

            }
        });

        addWifiDialog.show();

    }

    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {

        this.presenter = presenter;

    }

    public void setResults(Results results) {

        try {
            savedWifies = new ArrayList<Wifi>(results.getData());
            detectWifi();

        } catch (Exception e) {

            presenter.getWifies();
        }

    }

    public void setResults(Wifi wifi) {

        //prevent duplicate data
if(!getSsid().equals(wifi.getWifiSsid())) {

    addWifiDialog();

}
    }

    public void setResults(Reply reply) {

        Log.i("deleyting",reply.getMessage());
        if (reply.equals(null)) {
            Toast.makeText(getApplicationContext(), "Problem with internet connection", Toast.LENGTH_LONG).show();
        }
        else if(reply.getMessage().equals("Post Deleted"))
        {

            Toast.makeText(getApplicationContext(), "SORRY: This connection is under supervision", Toast.LENGTH_SHORT).show();
            detectWifi();

        }
        presenter.getWifies();

        Log.i("replies", reply.getMessage());
    }


    public void setErrorMessage(String errorMessage) {
        Log.i("errors", errorMessage);

    }


    @Override
    protected void onDestroy() {

        if(wc!=null)
        {
            wifiManager.removeNetwork(wc.networkId);
        }
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
                requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 87);
                requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 87);
            }
        }
    }

    public String getUniqueID() {
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



    public void setSsid(String ssid) {
        this.ssid = ssid;

    }

    public void setBssid(String bssid) {
        this.bssid = bssid;

    }

    public void setPassword(String password) {
        this.password = password;

    }

    public String getSsid() {
        return this.ssid;

    }

    public String getBssid() {
        return this.bssid;

    }

    public String getPassword() {
        return this.password;

    }

    public void toasMaking(String message)
    {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void makeSnackBar(View view, String message, String actionMessage)
    {


        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(actionMessage, null).show();
    }

    }



