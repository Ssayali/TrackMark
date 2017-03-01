package in.blazonsoftwares.trackmark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import in.blazonsoftwares.trackmark.model.WebServicesAPI;


public class DetailsAcitvity extends AppCompatActivity {
    String Productid;
    TextView lbl_pname,lbl_pprice,lbl_pspecification;
    ImageView img_product;


    //add to
    Spinner spQuantity;
    Button bOrder;
    String imgpath;


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String name = "name";
    public static final String qty = "qty";
    public static final String img = "img";


    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_acitvity);
        Productid= getIntent().getExtras().getString("Productid");

        lbl_pname= (TextView) findViewById(R.id.lbl_pname);
        lbl_pprice= (TextView) findViewById(R.id.lbl_pprice);
        lbl_pspecification= (TextView) findViewById(R.id.lbl_pspecification);
        img_product=(ImageView)findViewById(R.id.img_product);

        getdata();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       ///code for add to cart
        //session = new AddToSessionManagement(getApplicationContext());
        //Toast.makeText(this,"check log...."+session.getallproductdetails(),Toast.LENGTH_LONG).show();

        //Map<String,?> keys = session.productpref.getAll();
        //Toast.makeText(this,"key store in sahared..."+keys,Toast.LENGTH_SHORT).show();

        //for(Map.Entry<String,?> entry : keys.entrySet()){

          //  Toast.makeText(this,"map values...."+entry.getKey() + ": " +
           //         entry.getValue().toString(),Toast.LENGTH_LONG).show();
        //}

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

       // Map<String,?> keys = sharedpreferences.getAll();
        //Toast.makeText(this,"key store in sahared..."+keys,Toast.LENGTH_SHORT).show();


        spQuantity = (Spinner) findViewById(R.id.spQuantity);
        bOrder = (Button) findViewById(R.id.bOrder);
        bOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //session.checkoutuser();
                  //   session.AddProductSession(lbl_pname.getText().toString(),spQuantity.getSelectedItem().toString(),imgpath);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(name,lbl_pname.getText().toString());
                    editor.putString(qty, spQuantity.getSelectedItem().toString());
                    editor.putString(img, imgpath);
                    editor.commit();
                    Toast.makeText(DetailsAcitvity.this,lbl_pname.getText()+" Product Added Successfully.",Toast.LENGTH_LONG).show();
                }
                catch (Exception ex1){
                    Toast.makeText(DetailsAcitvity.this,"error : " + ex1,Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getdata() {
        int productcode=Integer.parseInt(Productid);
        String url = WebServicesAPI.deployment_api+"shop/ProductDetailsByPid?prodId="+productcode;
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
                        lbl_pspecification.setText(vehicle_info.getString(Configvolley.Product_Spec));
                        lbl_pname.setText(vehicle_info.getString(Configvolley.Product_Name));
                        lbl_pprice.setText(vehicle_info.getString(Configvolley.Product_price) + " " + getResources().getString(R.string.Rs));

                        if(vehicle_info.getString(Configvolley.Product_Image).equals("")){
                            Picasso.with(DetailsAcitvity.this).load(R.drawable.logowithout).into(img_product);

                        }
                        else {
                            Picasso.with(this).load(WebServicesAPI.deployment_api+ vehicle_info.getString(Configvolley.Product_Image)).into(img_product);
                            imgpath=WebServicesAPI.deployment_api+vehicle_info.getString(Configvolley.Product_Image);
                        }
                        lbl_pprice.setTextSize(20);
                        lbl_pspecification.setTextSize(20);
                        lbl_pname.setTextSize(35);
                        lbl_pname.setTextColor(Color.parseColor("#1987fa"));
                        lbl_pname.setTypeface(null, Typeface.BOLD);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
