package in.blazonsoftwares.trackmark;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
 import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
        {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker,mCurrLocationMarker1,mCurrLocationMarker2,mCurrLocationMarker3,mCurrLocationMarker4;
    LocationRequest mLocationRequest;


    //imagage view code
    SupportMapFragment mapFragment;
    String emailname;

    //image url code
    private String ImageUrl = "https://saneenergyproject.files.wordpress.com/2014/03/map-pin.png?w=176&h=300";

    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    ArrayList<String> stringArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        emailname = getIntent().getExtras().getString("emialname");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //all product list bind
        bindallproduct();

        AutoCompleteTextView autocompetetext = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
        autocompetetext.setAdapter(adapter);

        autocompetetext.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mMap.clear();
                bindnewmap(arg0.getItemAtPosition(arg2).toString());
            }
        });



    }

            void bindnewmap(String productname){

                String newproname=productname.replace(" ", "%20");
                String url = "http://trackmark.in/shop/GetShopByProduct?ProductName="+newproname;
                StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSONshopdetails(response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
                requestQueue.add(stringRequest);
            }


            private void showJSONshopdetails(String response){
                try {
                    JSONArray jsonObject = new JSONArray(response);


                    for(int i=0;i<jsonObject.length();i++) {
                        JSONObject vehicle_info = jsonObject.getJSONObject(i);

                      try {

                            Double newShop_Latval = Double.parseDouble(vehicle_info.getString(Configvolley.Latitude));
                            Double newShop_Langval = Double.parseDouble(vehicle_info.getString(Configvolley.Langitude));

                            LatLng latLng2 = new LatLng(newShop_Latval, newShop_Langval);
                            MarkerOptions markerOptions2 = new MarkerOptions();
                            markerOptions2 = new MarkerOptions();
                            markerOptions2.position(latLng2);
                            markerOptions2.title(vehicle_info.getString(Configvolley.Shop_no));
                            markerOptions2.isVisible();
                            markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            mCurrLocationMarker2 = mMap.addMarker(markerOptions2);
                        }
                        catch (Exception ex3){
                            Toast.makeText(MapsActivity.this, "erior   ....." + ex3, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));

            }
            private void bindallproduct() {
                  String url = "http://trackmark.in/shop/ProductNamelist";
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSONproductlist(response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
                requestQueue.add(stringRequest);
            }

            private void showJSONproductlist(String response){
                try {
                    JSONArray jsonObject = new JSONArray(response);
                      for(int i=0;i<jsonObject.length();i++) {
                        JSONObject vehicle_info = jsonObject.getJSONObject(i);
                          stringArrayList.add(vehicle_info.getString(Configvolley.Product_Name));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }




            @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //image set
        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView = (ImageView) mCustomMarkerView.findViewById(R.id.profile_image);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
         mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }



        final  LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        final MarkerOptions markerOptions = new MarkerOptions();
        Glide.with(getApplicationContext()).
                load(ImageUrl)
                .asBitmap()
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap))));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    }
                });


        getdata(location.getLatitude(),location.getLongitude());

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(150);
        circleOptions.strokeColor(Color.BLACK);
        circleOptions.fillColor(0x551ec9fd);
        circleOptions.strokeWidth(2);
        mMap.addCircle(circleOptions);

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

         mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try {
                    String markerloc=marker.getId();
                    if(markerloc.equals("m0")){}
                    else {

                        Intent i = new Intent(MapsActivity.this, ShopDashboard.class);
                        i.putExtra("shopname", marker.getTitle());
                        i.putExtra("emailname", emailname);
                        startActivity(i);
                    }
                }
                catch (Exception ex1){
                    Toast.makeText(MapsActivity.this,"erorr...."+ex1,Toast.LENGTH_LONG).show();
                }
                return false;

            }
        });
    }

    private void  getdata(Double latval,Double lognval){

        String url = "http://trackmark.in/Shop/ShopNearDetails?latval="+latval+"&longval="+lognval;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
            JSONArray jsonObject = new JSONArray(response);


            for(int i=0;i<jsonObject.length();i++) {
                JSONObject vehicle_info = jsonObject.getJSONObject(i);

                try {

                    Double newShop_Latval = Double.parseDouble(vehicle_info.getString(Configvolley.Latitude));
                    Double newShop_Langval = Double.parseDouble(vehicle_info.getString(Configvolley.Langitude));

                    LatLng latLng2 = new LatLng(newShop_Latval, newShop_Langval);
                    MarkerOptions markerOptions2 = new MarkerOptions();
                    markerOptions2 = new MarkerOptions();
                    markerOptions2.position(latLng2);
                    markerOptions2.title(vehicle_info.getString(Configvolley.Shop_no));
                    markerOptions2.isVisible();
                    markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    mCurrLocationMarker2 = mMap.addMarker(markerOptions2);
                }
                catch (Exception ex3){
                    Toast.makeText(MapsActivity.this, "erior   ....." + ex3, Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {
        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }


}
