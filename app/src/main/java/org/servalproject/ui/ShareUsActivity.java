package org.servalproject.ui;

import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.servalproject.R;
import org.servalproject.ServalBatPhoneApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class ShareUsActivity extends AppCompatActivity {
    private static final String TAG = "ShareUsActivity";
    TextView shareWifi;

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void updateHelpText() {
        ServalBatPhoneApplication app = (ServalBatPhoneApplication) this
                .getApplication();

        String ssid = null;
        InetAddress addr = null;

        try {
            if (app.nm.control.wifiManager.isWifiEnabled()) {
                NetworkInfo networkInfo = app.nm.control.connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                WifiInfo connection = app.nm.control.wifiManager.getConnectionInfo();
                if (networkInfo != null && networkInfo.isConnected() && connection != null) {
                    int iAddr = connection.getIpAddress();
                    addr = Inet4Address.getByAddress(new byte[]{
                            (byte) iAddr,
                            (byte) (iAddr >> 8),
                            (byte) (iAddr >> 16),
                            (byte) (iAddr >> 24),
                    });
                    ssid = connection.getSSID();
                }
                String helpText = null;
                if (addr != null && ssid != null && app.webServer != null) {
                    helpText = getString(R.string.share_wifi, ssid, "http://" + addr.getHostAddress()
                            + ":" + app.webServer.port + "/");
                } else {
                    helpText = getString(R.string.share_wifi_off);
                }
                shareWifi.setText(helpText);
            } else/* if (app.nm.control.wifiApManager.isWifiApEnabled())*/ {
                /*WifiConfiguration conf = app.nm.control.wifiApManager.getWifiApConfiguration();
                if (conf != null && conf.SSID != null)
                    ssid = conf.SSID;

                // TODO FIXME get the real AP network address
                addr = Inet4Address.getByAddress(new byte[]{
                        (byte) 192, (byte) 168, 43, 1,
                });*/

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Warning");
                dialog.setMessage("Подключитесь к Wi-Fi!");//# теперь что скомпилировать
                dialog.setPositiveButton("Ок", null);
                dialog.show();
            }
        } catch (UnknownHostException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shareus);

        Button shareUs = (Button) findViewById(R.id.share_us_button);
        shareUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (ServalBatPhoneApplication.getContext() != null) {
                    ServalBatPhoneApplication.getContext().shareViaBluetooth();
                }
            }
        });

        shareWifi = (TextView) findViewById(R.id.share_wifi);
        updateHelpText();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

}
