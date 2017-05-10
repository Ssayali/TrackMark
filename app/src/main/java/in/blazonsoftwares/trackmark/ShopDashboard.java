package in.blazonsoftwares.trackmark;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.blazonsoftwares.trackmark.model.WebServicesAPI;

public class ShopDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String shopname;
    TextView welcomeusename,txttitle;
    private String emailname;

    // Alert Dialog Manager & session object
    SessionManagement session;
    static String ownermail="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shopname = getIntent().getExtras().getString("shopname");
        emailname = getIntent().getExtras().getString("emailname");

        SessionManagement newss=new SessionManagement();
        newss.KEY_Shopcode=shopname;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        welcomeusename = (TextView) header.findViewById(R.id.welcomename);
        welcomeusename.setText(emailname);

        displaySelectedScreen(R.id.ShopDetails);
        bindshopmailid(shopname);
    }


    private void bindshopmailid(String Shopcode) {
        int productcode=Integer.parseInt(Shopcode);
        String url = WebServicesAPI.deployment_api +"USER/UseralldetailsByshopid?shopid="+productcode;
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
        RequestQueue requestQueue = Volley.newRequestQueue(ShopDashboard.this);
        requestQueue.add(stringRequest);
    }
    private void showJSON(String response){
        try {
            JSONArray jsonObject = new JSONArray(response);
            for(int i=0;i<jsonObject.length();i++) {
                JSONObject vehicle_info = jsonObject.getJSONObject(i);
                ownermail=vehicle_info.getString(Configvolley.User_Email);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        MapsActivity mymap=new MapsActivity();
        mymap.checkpass="";
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shop_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MapsActivity mymap=new MapsActivity();
        mymap.checkpass="";
        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.ShopDetails:
                fragment = new DrawerShopDetails();
                break;
            case R.id.OwnerDetails:
                fragment = new DrawerOwnerDetails();
                break;
            case R.id.ProductDetails:
                fragment = new DrawerProductList();
                break;
            case R.id.Chatting:
                ownermail= ownermail.replace(".", "-");
                UserDetails.chatWith=""+ownermail;
                Intent i1 = new Intent(ShopDashboard.this, Chat.class);
                startActivity(i1);
                break;
            case R.id.AboutUs:
                fragment = new DrawerAboutUs();
                break;
            case R.id.ContactUs:
                fragment = new DrawerContactUs();
                break;
            case R.id.GotoMap:
                MapsActivity mymap=new MapsActivity();
                mymap.checkpass="";
                Intent i = new Intent(ShopDashboard.this, MapsActivity.class);
                i.putExtra("emailname",emailname);
                startActivity(i);
                break;
            case R.id.VisitorDetails:
                 i = new Intent(ShopDashboard.this, VisitorDetails.class);
                i.putExtra("emailname",emailname);
                startActivity(i);
                break;
            case R.id.Logout:
                session = new SessionManagement(getApplicationContext());
                session.logoutUser();
                i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.CartDetails:
                i = new Intent(getApplicationContext(), CartList.class);
                startActivity(i);
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);

            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }
}
