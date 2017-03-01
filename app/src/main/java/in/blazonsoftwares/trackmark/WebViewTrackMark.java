package in.blazonsoftwares.trackmark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.blazonsoftwares.trackmark.model.WebServicesAPI;

public class WebViewTrackMark extends AppCompatActivity {

    private static WebView myWebView;
    private static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_track_mark);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_Web_view);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_Web_view);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        if(isNetworkAvailable(getApplicationContext())) {
            //myWebView.loadUrl(RestAPI.main_link+link);
            myWebView.loadUrl(WebServicesAPI.deployment_api);
            //System.out.println("Sachin = "+RestAPI.main_link+link);
            myWebView.getSettings().setJavaScriptEnabled(true);

            WebSettings settings = myWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setBuiltInZoomControls(false);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setDatabaseEnabled(true);

            progressDialog = ProgressDialog.show(this, "",
                    "Loading Track Mark... Please Wait", true);

            myWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(myWebView, url);
                    progressDialog.dismiss();
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }
            });
        } else {
            new android.support.v7.app.AlertDialog.Builder(WebViewTrackMark.this)
                    .setCancelable(false)
                    .setTitle("No Internet Connection!")
                    .setIcon(R.drawable.logo)
                    .setMessage("Internet connectivity not available")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(WebViewTrackMark.this, NavigateActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                            finish();
                        }
                    })
                    .show();
        }
    }

    public void onBackPressed() {
        if(myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            Intent intent = new Intent(WebViewTrackMark.this, NavigateActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Intent intent = new Intent(WebViewTrackMark.this, NavigateActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
