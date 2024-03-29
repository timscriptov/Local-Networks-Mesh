/**
 * Copyright (C) 2011 The Serval Project
 * <p>
 * This file is part of Serval Software (http://www.servalproject.org)
 * <p>
 * Serval Software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.servalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.apache.http.conn.util.InetAddressUtils;
import org.servalproject.ServalBatPhoneApplication.State;
import org.servalproject.rhizome.RhizomeMain;
import org.servalproject.servald.ServalD;
import org.servalproject.servaldna.keyring.KeyringIdentity;
import org.servalproject.ui.Networks;
import org.servalproject.ui.SettingsActivity;
import org.servalproject.ui.scan.NetDeviceAdapter;
import org.servalproject.ui.scan.Pinger;
import org.servalproject.ui.scan.model.Device;
import org.servalproject.utils.Utils;
import org.servalproject.wizard.Wizard;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Main activity which presents the Serval launcher style screen. On the first
 * time Serval is installed, this activity ensures that a warning dialog is
 * presented and the user is taken through the setup wizard. Once setup has been
 * confirmed the user is taken to the main screen.
 *
 * @author Paul Gardner-Stephen <paul@servalproject.org>
 * @author Andrew Bettison <andrew@servalproject.org>
 * @author Corey Wallis <corey@servalproject.org>
 * @author Jeremy Lakeman <jeremy@servalproject.org>
 * @author Romana Challans <romana@servalproject.org>
 */
public class Main extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Main";
    private static final int PERMISSION_REQUEST = 1;
    private static final int PEER_LIST_RETURN = 0;
    private final NetDeviceAdapter adapter = new NetDeviceAdapter(new ArrayList<>(15), R.layout.device_fragment, this);
    public ServalBatPhoneApplication app;
    boolean registered = false;
    private TextView buttonToggle;
    private ImageView buttonToggleImg;
    private Drawable powerOnDrawable;
    private Button gpsButton;
    private TextView progressTitle;
    private ProgressBar progressBar;
    private LinearLayout details;
    private TextView latitude;
    private TextView longitude;
    private TextView altitude;
    private TextView speed;
    private ImageView shareButton;
    private ImageView copyButton;
    private ImageView viewButton;
    private LocationManager locManager;
    private Location lastLocation;
    private final LocationListener locListener = new LocationListener() {
        public void onLocationChanged(Location loc) {
            updateLocation(loc);
        }

        public void onProviderEnabled(String provider) {
            updateLocation();
        }

        public void onProviderDisabled(String provider) {
            updateLocation();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private Drawable powerOffDrawable;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int stateOrd = intent.getIntExtra(
                    ServalBatPhoneApplication.EXTRA_STATE, 0);
            State state = State.values()[stateOrd];
            stateChanged(state);
        }
    };

    public static String intToIp(int i) {
        return ((i >> 24) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                (i & 0xFF);
    }

    private static Double getDistance(Location one, Location two) {
        int R = 6371000;
        double dLat = toRad(two.getLatitude() - one.getLatitude());
        double dLon = toRad(two.getLongitude() - one.getLongitude());
        double lat1 = toRad(one.getLatitude());
        double lat2 = toRad(two.getLatitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private static double toRad(Double d) {
        return d * Math.PI / 180;
    }

    private void rescan() {

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() && mWifi.isAvailable()) {
            AsyncScan scan = new AsyncScan(this);
            scan.execute(adapter);
        } else {
            Toast.makeText(this, getString(R.string.not_connected_error), Toast.LENGTH_LONG).show();
        }
    }

    // Кнопка открытия карт
    private void openMaps() {
        if (Utils.isInstalled(this, "com.google.android.apps.maps")) {
            Intent i;
            if (lastLocation == null) {
                i = new Intent(android.content.Intent.ACTION_VIEW);
            } else {
                Uri gmmIntentUri = Uri.parse(formatLocation(lastLocation, "geo:{0},{1}"));
                ;
                i = new Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
            }

            i.setPackage("com.google.android.apps.maps");
            startActivity(i);
        } else {
            // Иначе открыть диалог
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Warning");
            dialog.setMessage("Карты не найдены!");//# теперь что скомпилировать
            dialog.setPositiveButton("Установить", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Открыть маркет для загрузки
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton(android.R.string.cancel, null);
            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        // Do nothing until upgrade finished.
        if (app.getState() != State.Running)
            return;

        switch (view.getId()) {
            case R.id.contactsLabel:
                Intent mIntent = new Intent(this, org.servalproject.PeerList.class);
                startActivityForResult(mIntent, PEER_LIST_RETURN);
                break;
            case R.id.messageLabel:
                if (!ServalD.isRhizomeEnabled()) {
                    app.displayToastMessage("Messaging cannot function without an sdcard");
                    return;
                }
                startActivity(new Intent(getApplicationContext(),
                        org.servalproject.messages.MessagesListActivity.class));
                break;
            case R.id.mapsLabel:
                openMaps();
                break;

            /*case R.id.compassLabel:
                startActivity(new Intent(getApplicationContext(),
                        CompassActivity.class));
                break;*/
            case R.id.servalLabel:
                startActivity(new Intent(getApplicationContext(),
                        RhizomeMain.class));
                break;

            //case R.id.flashlightLabel:
            //    startActivity(new Intent(getApplicationContext(),
            //            FlashlightActivity.class));
            //    break;
            case R.id.powerLabel:
                startActivity(new Intent(getApplicationContext(),
                        Networks.class));
                break;
            /*case R.id.ipScanerLabel:
                startActivity(new Intent(getApplicationContext(),
                        ScanActivity.class));
                break;*/
        }
    }

    public void scan(View v) {
        rescan();
    }

    //-----------------------------------------------------
    // Menu related methods
    //-----------------------------------------------------

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.app = (ServalBatPhoneApplication) this.getApplication();

        setContentView(R.layout.main);
        setSupportActionBar(findViewById(R.id.toolbar));

        // Display area
        gpsButton = findViewById(R.id.gpsButton);
        progressTitle = findViewById(R.id.progressTitle);
        progressBar = findViewById(R.id.progressBar);

        // GPS
        details = findViewById(R.id.details);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        altitude = findViewById(R.id.altitude);
        speed = findViewById(R.id.speed);

        // Button area
        shareButton = findViewById(R.id.shareButton);
        copyButton = findViewById(R.id.copyButton);
        viewButton = findViewById(R.id.viewButton);

        // Set default values for preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // adjust the power button label on startup
        buttonToggle = (TextView) findViewById(R.id.btntoggle);
        buttonToggleImg = (ImageView) findViewById(R.id.powerLabel);
        buttonToggleImg.setOnClickListener(this);

        // load the power drawables
        powerOnDrawable = getResources().getDrawable(
                R.drawable.ic_launcher_power);
        powerOffDrawable = getResources().getDrawable(
                R.drawable.ic_launcher_power_off);

        int[] listenTo = {
                R.id.contactsLabel,
                R.id.messageLabel,
                R.id.mapsLabel,

                R.id.servalLabel,

                //R.id.flashlightLabel,
                R.id.powerLabel
        };
        for (int i = 0; i < listenTo.length; i++) {
            this.findViewById(listenTo[i]).setOnClickListener(this);
        }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intentSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(intentSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void stateChanged(State state) {
        switch (state) {
            case Running:
            case Upgrading:
            case Starting:
                // change the image for the power button
                buttonToggleImg.setImageDrawable(
                        app.isEnabled() ? powerOnDrawable : powerOffDrawable);

                TextView pn = (TextView) this.findViewById(R.id.mainphonenumber);
                String id = this.getString(state.getResourceId());
                if (state == State.Running) {
                    try {
                        KeyringIdentity identity = app.server.getIdentity();

                        if (identity.did != null)
                            id = identity.did;
                        else
                            id = identity.sid.abbreviation();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
                pn.setText(id);
                break;
            case RequireDidName:
            case NotInstalled:
            case Installing:
                this.startActivity(new Intent(this, Wizard.class));
                finish();
                app.startBackgroundInstall();
                break;
            case Broken:
                // TODO display error?
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!registered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ServalBatPhoneApplication.ACTION_STATE);
            this.registerReceiver(receiver, filter);
            registered = true;
        }

        stateChanged(app.getState());

        startRequestingLocation();
        updateLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            locManager.removeUpdates(locListener);
        } catch (SecurityException e) {
            Log.e(TAG, "Failed to stop listening for location updates", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (registered) {
            this.unregisterReceiver(receiver);
            registered = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST &&
                grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRequestingLocation();
        } else {
            Toast.makeText(getApplicationContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // ----------------------------------------------------
    // UI
    // ----------------------------------------------------
    private void updateLocation() {
        // Trigger a UI update without changing the location
        updateLocation(lastLocation);
    }

    @SuppressLint("SetTextI18n")
    private void updateLocation(Location location) {
        boolean locationEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean waitingForLocation = locationEnabled && !validLocation(location);
        boolean haveLocation = locationEnabled && !waitingForLocation;

        // Update display area
        gpsButton.setVisibility(locationEnabled ? View.GONE : View.VISIBLE);
        //progressTitle.setVisibility(waitingForLocation ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(waitingForLocation ? View.VISIBLE : View.GONE);
        details.setVisibility(haveLocation ? View.VISIBLE : View.GONE);

        // Update buttons
        shareButton.setEnabled(haveLocation);
        copyButton.setEnabled(haveLocation);
        viewButton.setEnabled(haveLocation);

        if (haveLocation) {
            if (location != null) {
                if (lastLocation != null) {
                    //getString(R.string.accuracy) + ": " + getAccuracy(location) .. Ещчность
                    altitude.setText(getString(R.string.altitude) + ": " + getAltitude(location));
                    speed.setText(getString(R.string.speed) + ": " + getSpeed(location));
                    latitude.setText(getString(R.string.latitude) + ": " + getLatitude(location));
                    longitude.setText(getString(R.string.longitude) + ": " + getLongitude(location));
                }
            }
            lastLocation = location;
        }
    }

    // ----------------------------------------------------
    // Actions
    // ----------------------------------------------------
    public void shareLocation(View view) {
        if (!validLocation(lastLocation)) {
            return;
        }

        String linkChoice = PreferenceManager.getDefaultSharedPreferences(this).getString("prefLinkType", "");

        if (linkChoice.equals(getResources().getString(R.string.always_ask))) {
            new AlertDialog.Builder(this).setTitle(R.string.choose_link)
                    .setCancelable(true)
                    .setItems(R.array.link_names, new onClickShareListener())
                    .create()
                    .show();
        } else {
            shareLocationText(formatLocation(lastLocation, linkChoice));
        }
    }

    public void copyLocation(View view) {
        if (!validLocation(lastLocation)) {
            return;
        }

        String linkChoice = PreferenceManager.getDefaultSharedPreferences(this).getString("prefLinkType", "");

        if (linkChoice.equals(getResources().getString(R.string.always_ask))) {
            new AlertDialog.Builder(this).setTitle(R.string.choose_link)
                    .setCancelable(true)
                    .setItems(R.array.link_names, new onClickCopyListener())
                    .create()
                    .show();
        } else {
            copyLocationText(formatLocation(lastLocation, linkChoice));
        }
    }

    public void viewLocation(View view) {
        if (!validLocation(lastLocation)) {
            return;
        }

        String uri = formatLocation(lastLocation, "geo:{0},{1}?q={0},{1}");

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(Intent.createChooser(intent, getString(R.string.view_location_via)));
    }

    public void openLocationSettings(View view) {
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    // ----------------------------------------------------
    // Helper functions
    // ----------------------------------------------------
    public void shareLocationText(String string) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, string);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.share_location_via)));
    }

    public void copyLocationText(String string) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(getString(R.string.app_name), string);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), R.string.copied, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Failed to get the clipboard service");
            Toast.makeText(getApplicationContext(), R.string.clipboard_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void startRequestingLocation() {
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
            return;
        }

        // GPS enabled and have permission - start requesting location updates
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean validLocation(Location location) {
        if (location == null) {
            return false;
        }

        // Location must be from less than 30 seconds ago to be considered valid
        if (Build.VERSION.SDK_INT < 17) {
            return System.currentTimeMillis() - location.getTime() < 30e3;
        } else {
            return SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos() < 30e9;
        }
    }

    private String getAccuracy(Location location) {
        float accuracy = location.getAccuracy();
        if (accuracy < 0.01) {
            return "?";
        } else if (accuracy > 99) {
            return "99+";
        } else {
            return String.format(Locale.US, "%2.0fm", accuracy);
        }
    }

    private String getAltitude(Location location) {
        double altitude = location.getAltitude();
        /*if (altitude < 0.01) {
            return "?";
        } else if (altitude > 99) {
            return "99+";
        } else {*/
        return String.format(Locale.US, "%2.0fm", altitude);
        //}
    }

    private String getSpeed(Location location) {
        //if(ServalBatPhoneApplication.getPreferences().getString("", "0").equals("0")) {
        return getSpeedKMH(location);
        //} else {
        //    return getSpeedMS(location);
        //}
    }

    private String getSpeedKMH(Location location) {
        float speed = (location.getSpeed() * 3600) / 1000;
        ;
        /*if (speed < 0.01) {
            return "?";
        } else if (speed > 99) {
            return "99+";
        } else {*/
        return String.format(Locale.US, "%2.0fkm/h", speed);
        //}
    }

    private String getSpeedMS(Location location) {
        float speed = location.getSpeed();
        /*if (speed < 0.01) {
            return "?";
        } else if (speed > 99) {
            return "99+";
        } else {*/
        return String.format(Locale.US, "%2.0fm/s", speed);
        //}
    }

    private String getLatitude(Location location) {
        return String.format(Locale.US, "%2.5f", location.getLatitude());
    }

    private String getDMSLatitude(Location location) {
        double val = location.getLatitude();
        return String.format(Locale.US, "%.0f° %2.0f′ %2.3f″ %s",
                Math.floor(Math.abs(val)),
                Math.floor(Math.abs(val * 60) % 60),
                (Math.abs(val) * 3600) % 60,
                val > 0 ? "N" : "S"
        );
    }

    private String getDMSLongitude(Location location) {
        double val = location.getLongitude();
        return String.format(Locale.US, "%.0f° %2.0f′ %2.3f″ %s",
                Math.floor(Math.abs(val)),
                Math.floor(Math.abs(val * 60) % 60),
                (Math.abs(val) * 3600) % 60,
                val > 0 ? "E" : "W"
        );
    }

    private String getLongitude(Location location) {
        return String.format(Locale.US, "%3.5f", location.getLongitude());
    }

    private String formatLocation(Location location, String format) {
        return MessageFormat.format(format,
                getLatitude(location), getLongitude(location));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private static class AsyncScan extends AsyncTask<NetDeviceAdapter, Void, List<Device>> {

        private ProgressDialog progressDialog;
        private NetDeviceAdapter adapter;

        public AsyncScan(Context context) {
            super();
            progressDialog = new ProgressDialog(context);
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle(R.string.scanning);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(List<Device> inetAddresses) {
            super.onPostExecute(inetAddresses);
            adapter.setAddresses(inetAddresses);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    // ----------------------------------------------------
    // DialogInterface Listeners
    // ----------------------------------------------------
    private class onClickShareListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int i) {
            shareLocationText(formatLocation(lastLocation, getResources().getStringArray(R.array.link_values)[i]));
        }
    }

    private class onClickCopyListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int i) {
            copyLocationText(formatLocation(lastLocation, getResources().getStringArray(R.array.link_values)[i]));
        }
    }
}
