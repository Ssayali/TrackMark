package in.blazonsoftwares.trackmark;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class NavigateActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    SessionManagement session;

    // Alert Dialog Manager & session object
    AlertDialogManager alert = new AlertDialogManager();
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

    }

    //login click
    public void loginmember(View v) {
        String message;
        int color;
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                pd = new ProgressDialog(NavigateActivity.this);
                pd.setMessage("Loading");
                pd.show();

                session = new SessionManagement(getApplicationContext());
                try{
                    if(session.isLoggedIn()) {

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                pd.dismiss();
                                Intent i = new Intent(NavigateActivity.this, MapsActivity.class);
                                i.putExtra("emialname",session.KEY_EMAIL);
                                startActivity(i);

                            }

                        }, 1500);
                    }
                    else {
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                pd.dismiss();
                                Intent i = new Intent(NavigateActivity.this, LoginActivity.class);
                                startActivity(i);

                            }

                        }, 1500);
                    }
                }
                catch (Exception ex2){
                    Toast.makeText(NavigateActivity.this,"error...."+ex2,Toast.LENGTH_LONG).show();
                }


            }
            else {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("Goto Settings Page To Enable GPS",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent callGPSSettingIntent = new Intent(
                                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(callGPSSettingIntent);
                                    }
                                });
                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }


        }
        else
        {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.loadmember1), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    public void loginagent(View v) {

        String message;
        int color;
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
        Intent i = new Intent(NavigateActivity.this, WebViewTrackMark.class);
        startActivity(i);
        }
        else
        {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.loadmember), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.btn_check), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(NavigateActivity.this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
