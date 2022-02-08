package org.servalproject.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.servalproject.LogActivity;
import org.servalproject.PreparationWizard;
import org.servalproject.R;
import org.servalproject.ServalBatPhoneApplication;
import org.servalproject.servaldna.keyring.KeyringIdentity;
import org.servalproject.ui.help.HtmlHelp;

import java.io.File;

public class SettingsActivity extends PreferenceActivity {

    public Preference notificationSound;
    public Preference resetDetails;
    public Preference meshTesting;
    public Preference viewLogs;
    public Preference help;
    public Preference website;

    private final int RINGTONE_PICKER_ACTIVITY = 1;

    private static final OnPreferenceChangeListener prefsListener = (pref, value) -> {
        String valueString = value.toString();

        if (pref instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) pref;
            int index = listPreference.findIndexOfValue(valueString);

            pref.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
        }
        return true;
    };

    // ----------------------------------------------------
    // Android Lifecycle
    // ----------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar();

        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference("prefLinkType"));
        bindPreferenceSummaryToValue(findPreference("streamType"));

        notificationSound = findPreference("notificationSound");
        notificationSound.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference p1) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                        RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
                        "Select MeshMS Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
                String current = ServalBatPhoneApplication.getPreferences().getString(
                        "meshms_notification_sound", null);
                if (current != null) {
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                            current);
                }
                Uri def = RingtoneManager.getActualDefaultRingtoneUri(SettingsActivity.this,
                        RingtoneManager.TYPE_NOTIFICATION);
                if (def != null) {
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, def);
                }
                startActivityForResult(intent, RINGTONE_PICKER_ACTIVITY);
                return true;
            }
        });

        resetDetails = findPreference("resetDetails");
        resetDetails.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference p1) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout ll = new LinearLayout(SettingsActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setPadding(40, 0, 40, 0);
                ll.setLayoutParams(layoutParams);

                AppCompatTextView accName = new AppCompatTextView(SettingsActivity.this);
                accName.setText("Имя:");

                AppCompatTextView accNameLabel = new AppCompatTextView(SettingsActivity.this);

                AppCompatTextView accNumber = new AppCompatTextView(SettingsActivity.this);
                accNumber.setText("Номер:");

                AppCompatTextView accNumberLabel = new AppCompatTextView(SettingsActivity.this);

                AppCompatTextView accId = new AppCompatTextView(SettingsActivity.this);
                accId.setText("ID:");

                AppCompatTextView accIdLabel = new AppCompatTextView(SettingsActivity.this);

                String PNid = "There is no phone number to display";
                String SIDid = "There is no ServalID to display";
                String NMid = "There is no name to display";

                try {
                    KeyringIdentity identity = ServalBatPhoneApplication.getContext().server.getIdentity();
                    if (identity.did != null)
                        PNid = identity.did;
                    if (identity.name != null)
                        NMid = identity.name;
                    if (identity.sid != null)
                        SIDid = identity.sid.abbreviation();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // set values to display
                accNumberLabel.setText(PNid); // Phone number
                accIdLabel.setText(SIDid); // Serval ID
                accNameLabel.setText(NMid); // Name

                ll.addView(accName);
                ll.addView(accNameLabel);
                ll.addView(accNumber);
                ll.addView(accNumberLabel);
                ll.addView(accId);
                ll.addView(accIdLabel);

                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
                dialog.setTitle("Внимание");
                dialog.setView(ll);
                dialog.setMessage("Вы действительно хотите очистить свои данные?");//# теперь что скомпилировать
                dialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(SettingsActivity.this,
                                org.servalproject.wizard.SetPhoneNumber.class));
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton(android.R.string.cancel, null);
                dialog.show();
                return true;
            }
        });

        meshTesting = findPreference("meshTesting");
        meshTesting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference p1) {
                // Reset Wi-fi Settings Screen
                // Clear out old attempt_ files
                File varDir = new File(
                        ServalBatPhoneApplication.getContext().coretask.DATA_FILE_PATH
                                +
                                "/var/");
                if (varDir.isDirectory())
                    for (File f : varDir.listFiles()) {
                        if (!f.getName().startsWith("attempt_"))
                            continue;
                        f.delete();
                    }
                // Re-run wizard
                Intent prepintent = new Intent(SettingsActivity.this,
                        PreparationWizard.class);
                prepintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(prepintent);
                return true;
            }
        });

        viewLogs = findPreference("viewLogs");
        viewLogs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference p1) {
                startActivity(new Intent(SettingsActivity.this, LogActivity.class));
                return true;
            }
        });

        help = findPreference("help");
        help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference p1) {
                Intent intent = new Intent(getApplicationContext(),
                        HtmlHelp.class);
                intent.putExtra("page", "helpindex.html");
                startActivity(intent);
                return true;
            }
        });

        website = findPreference("website");
        website.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference p1) {
                Intent intent = new Intent(getApplicationContext(),
                        HtmlHelp.class);
                intent.putExtra("page", "index.html");
                startActivity(intent);
                return true;
            }
        });
    }

    //-----------------------------------------------------
    // Preferences related methods
    //-----------------------------------------------------
    private static void bindPreferenceSummaryToValue(Preference pref) {
        pref.setOnPreferenceChangeListener(prefsListener);

        prefsListener.onPreferenceChange(pref, PreferenceManager
                .getDefaultSharedPreferences(pref.getContext())
                .getString(pref.getKey(), ""));
    }

    // ----------------------------------------------------
    // Helper functions
    // ----------------------------------------------------
    private void setToolbar() {
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        root.addView(toolbar, 0);
        toolbar.setTitle(R.string.settings);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}