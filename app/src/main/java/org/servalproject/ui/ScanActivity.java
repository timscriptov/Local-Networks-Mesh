package org.servalproject.ui;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.apache.http.conn.util.InetAddressUtils;
import org.servalproject.R;
import org.servalproject.ui.scan.NetDeviceAdapter;
import org.servalproject.ui.scan.Pinger;
import org.servalproject.ui.scan.model.Device;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ScanActivity extends AppCompatActivity {

    private final NetDeviceAdapter adapter = new NetDeviceAdapter(new ArrayList<Device>(15), R.layout.device_fragment, this);

    public static String intToIp(int i) {
        return ((i >> 24) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                (i & 0xFF);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainView);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;

        int numrows = (int) Math.floor(dpWidth / 300);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(numrows, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        rescan();
    }

    private void rescan() {

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() && mWifi.isAvailable()) {
            AppCompatDialog dialog = new AppCompatDialog(this);
            dialog.setTitle(R.string.scanning);
            dialog.setCancelable(false);
            dialog.show();
            AsyncScan scan = new AsyncScan(dialog, getString(R.string.scanning_your_network));
            scan.execute(adapter);
        } else {
            Toast.makeText(this, getString(R.string.not_connected_error), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                rescan();
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy
        super.onDestroy();
    }

    private static class AsyncScan extends AsyncTask<NetDeviceAdapter, Void, List<Device>> {

        private NetDeviceAdapter adapter;
        private final AppCompatDialog mDialog;

        public AsyncScan(AppCompatDialog dialog, String string) {
            super();
            this.mDialog = dialog;
        }

        public static String getLocalIpv4Address() {
            try {
                String ipv4;
                List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
                if (nilist.size() > 0) {
                    for (NetworkInterface ni : nilist) {
                        List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                        if (ialist.size() > 0) {
                            for (InetAddress address : ialist) {
                                if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {
                                    return ipv4;
                                }
                            }
                        }
                    }
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            }
            return "";
        }

        @Override
        protected List<Device> doInBackground(NetDeviceAdapter... voids) {
            String ipString = getLocalIpv4Address();

            if (ipString == null) {
                return new ArrayList<>(1);
            }
            int lastdot = ipString.lastIndexOf(".");
            ipString = ipString.substring(0, lastdot);

            List<Device> addresses = Pinger.getDevicesOnNetwork(ipString);
            adapter = voids[0];
            return addresses;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(List<Device> inetAddresses) {
            super.onPostExecute(inetAddresses);
            adapter.setAddresses(inetAddresses);
            adapter.notifyDataSetChanged();
            mDialog.cancel();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}