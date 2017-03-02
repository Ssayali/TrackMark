package in.blazonsoftwares.trackmark;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ShopDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String shopname;
    TextView welcomeusename,txttitle;
    private String emailname;

    // Alert Dialog Manager & session object
    SessionManagement session;



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
    }

    @Override
    public void onBackPressed() {
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
                fragment = new DrawerChatting();
                break;
            case R.id.AboutUs:
                fragment = new DrawerAboutUs();
                break;
            case R.id.ContactUs:
                fragment = new DrawerContactUs();
                break;
            case R.id.GotoMap:
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
