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
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.servalproject.R;
import org.servalproject.ServalBatPhoneApplication;

/**
 * help screens - guide to using Serval
 *
 * @author Romana Challans <romana@servalproject.org>
 */

public class HtmlHelp extends AppCompatActivity {
    static final String assetPrefix = "file:///android_asset/";
    int viewId = R.layout.htmlhelp;
    private WebView helpBrowser;
    private String startPage;

    /**
     * Called when the activity is first created.
     */
    // Since we're only loading our own assets from the asset folder in this
    // view, there shouldn't be any security issues.
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewId);
        helpBrowser = (WebView) findViewById(R.id.help_browser);
        helpBrowser.getSettings().setJavaScriptEnabled(true);
        helpBrowser.addJavascriptInterface(new AppInfo(), "appinfo");
        helpBrowser.setWebViewClient(new Client());
        helpBrowser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        helpBrowser.setBackgroundColor(Color.BLACK);
        helpBrowser.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @SuppressLint("ResourceAsColor")
    private void startCustomTabsInBrowser(Activity activity, String startUrl) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        /*CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
                .setNavigationBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
                .setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
                .setSecondaryToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
                .build();*/
        intentBuilder.setShowTitle(true);
        //intentBuilder.setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, params);
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(startUrl));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = this.getIntent();
        startPage = assetPrefix + intent.getStringExtra("page");
        helpBrowser.clearHistory();
        helpBrowser.loadUrl(startPage);
    }

    @Override
    public void onBackPressed() {
        WebBackForwardList history = helpBrowser.copyBackForwardList();
        int index = history.getCurrentIndex();
        for (int offset = -1; index + offset >= 0; offset--) {
            if (!history.getItemAtIndex(index + offset).getUrl()
                    .equals("about:blank")) {
                helpBrowser.goBackOrForward(offset);
                return;
            }
        }
        super.onBackPressed();
    }

    public class Client extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            ServalBatPhoneApplication.getContext().displayToastMessage(description);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(assetPrefix) || url.equals("about:blank"))
                return false;

            // Load the uri using the full internet browser app.
            //Uri uri = Uri.parse(url);
            //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //startActivity(intent);
            startCustomTabsInBrowser(HtmlHelp.this, url);
            return true;
        }
    }

    class AppInfo {
        public String getVersion() {
            return HtmlHelp.this.getString(R.string.version);
        }
    }
}
