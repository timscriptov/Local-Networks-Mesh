/* Copyright (C) 2012 The Serval Project
 *
 * This file is part of Serval Software (http://www.servalproject.org)
 *
 * Serval Software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.servalproject.ui.help;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import org.servalproject.R;

public class HtmlHelpActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "extra.url";
    public static final String EXTRA_TITLTE = "extra.title";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String url = "file:///android_asset/" + getIntent().getStringExtra(EXTRA_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLTE);

        WebView webView = (WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        setTitle(title);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView.loadUrl(url);
        //startCustomTabsInBrowser(this, url);
    }

    @SuppressLint("ResourceAsColor")
    private void startCustomTabsInBrowser(Activity activity, String startUrl) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
                .setNavigationBarColor(ContextCompat.getColor(activity, R.color.colorBackground))
                .setToolbarColor(ContextCompat.getColor(activity, R.color.colorBackground))
                .setSecondaryToolbarColor(ContextCompat.getColor(activity, R.color.colorBackground))
                .build();
        intentBuilder.setShowTitle(true);
        intentBuilder.setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, params);
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(startUrl));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}