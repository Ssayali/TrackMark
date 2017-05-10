package in.blazonsoftwares.trackmark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.blazonsoftwares.trackmark.model.WebServicesAPI;

import static android.R.attr.country;

public class CartList extends AppCompatActivity {

    private ListView lvlist;
    private Model siteModel;
    private ProgressDialog pd;
    private List<Model> siteModelList;
    public ImageLoader imageLoader;

    String vistormail;
    SessionManagement session;

    //cart
    CheckBox checkcart;
    Button btnremoveorder;
    TextView description,txttotalprice;
    MyCustomAdapter dataAdapter = null;
    static int finalamt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        session = new SessionManagement(getApplicationContext());
        vistormail=session.getUserDetails().get("email");


        imageLoader = ImageLoader.getInstance();
        lvlist=(ListView) findViewById(R.id.lvlist);
       getdata();

        btnremoveorder=(Button)findViewById(R.id.btnremoveorder);
        description=(TextView)findViewById(R.id.description);
        checkcart=(CheckBox)findViewById(R.id.checkcart);
        txttotalprice = (TextView) findViewById(R.id.txttotalprice);



        Button myButton = (Button) findViewById(R.id.btnremoveorder);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected to remove from cart ...\n");
                ArrayList<Country> countryList = dataAdapter.countryList;
                for (int i = 0; i < countryList.size(); i++) {
                    Country country = countryList.get(i);
                    if (country.isSelected()) {
                        RemoveFromCart(country.getProductCode());
                        responseText.append("\n" + country.getCode());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });


        myButton = (Button) findViewById(R.id.cartempty);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CartEmpty(vistormail);
            }
        });


        myButton = (Button) findViewById(R.id.btnproccesscheck);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(),
                        "Process Checkout comming soon", Toast.LENGTH_LONG).show();

            }
        });
    }
    private class MyCustomAdapter extends ArrayAdapter<Country> {
        private ArrayList<Country> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country>();
            this.countryList.addAll(countryList);
        }

        private class CartViewHolder {
            TextView code;
            CheckBox check1;
            TextView Price,productqty,producttotal;


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CartViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if(convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.cartrow, null);

                holder = new CartViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.description);
                holder.check1 = (CheckBox) convertView.findViewById(R.id.checkcart);
                holder.Price = (TextView) convertView.findViewById(R.id.sitename);
                holder.productqty = (TextView) convertView.findViewById(R.id.productqty);
                holder.producttotal = (TextView) convertView.findViewById(R.id.producttotal);

                convertView.setTag(holder);

                holder.check1.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Country country = (Country) cb.getTag();
                        country.setSelected(cb.isChecked());
                    }
                });

            }
            else {
                holder = (CartViewHolder) convertView.getTag();
            }

            Country country = countryList.get(position);
            holder.code.setText(country.getCode() );
            holder.check1.setChecked(country.isSelected());
            holder.check1.setTag(country);
            holder.Price.setText(country.getPrice());

            holder.productqty.setText(" x "+country.getqty());
            holder.producttotal.setText(country.getTotal());


            holder.code.setTextSize(25);
            holder.Price.setTextSize(17);
            holder.productqty.setTextSize(17);

            holder.producttotal.setTextSize(19);
            holder.code.setTextColor(Color.parseColor("#1987fa"));
            holder.producttotal.setTextColor(Color.parseColor("#800000"));



            return convertView;
        }

    }


    private void RemoveFromCart(String cardcode)
    {
        String  newcardcode=cardcode.toString().replace(" ", "%20");
        String url = WebServicesAPI.deployment_api+"shop/RemoveFromCart?Cartcode="+newcardcode;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String url = WebServicesAPI.deployment_api+"Shop/getCartDetails?username="+vistormail;
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                int socketTimeout = 30000; // 30 seconds. You can change it
                                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                Toast.makeText(CartList.this,"error...."+error,Toast.LENGTH_SHORT).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(CartList.this);
                requestQueue.add(stringRequest);


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int socketTimeout = 30000; // 30 seconds. You can change it
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        Toast.makeText(CartList.this,"error call...."+error,Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(CartList.this);
        requestQueue.add(stringRequest);

    }

    private void CartEmpty(String visitoremail)
    {
        //String  newvisitoremail=visitoremail.toString().replace(" ", "%20");
        String url = WebServicesAPI.deployment_api+"shop/CartEmpty?visitoremail="+visitoremail;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String url = WebServicesAPI.deployment_api+"Shop/getCartDetails?username="+vistormail;
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                int socketTimeout = 30000; // 30 seconds. You can change it
                                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                Toast.makeText(CartList.this,"error...."+error,Toast.LENGTH_SHORT).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(CartList.this);
                requestQueue.add(stringRequest);


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int socketTimeout = 30000; // 30 seconds. You can change it
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        Toast.makeText(CartList.this,"error call...."+error,Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(CartList.this);
        requestQueue.add(stringRequest);
    }



    private void getdata()
    {
      String url = WebServicesAPI.deployment_api+"Shop/getCartDetails?username="+vistormail;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int socketTimeout = 30000; // 30 seconds. You can change it
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        Toast.makeText(CartList.this,"error...."+error,Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(CartList.this);
        requestQueue.add(stringRequest);
    }



    private void showJSON(String response){

        try {

            JSONArray jsonObject = new JSONArray(response);
            siteModelList = new ArrayList<>();
            finalamt = 0;
            ArrayList<Country> countryList = new ArrayList<Country>();
            if (jsonObject.length() == 0) {
                Toast.makeText(CartList.this,"Please Add Product Into Cart ....",Toast.LENGTH_SHORT).show();

            }
            else
            {

            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject vehicle_info = jsonObject.getJSONObject(i);

                siteModel = new Model();
                siteModel.setCart_Product_Name(vehicle_info.getString(Configvolley.Cart_Product_Name));
                siteModel.setCart_Product_Price(vehicle_info.getString(Configvolley.Cart_Product_Price));
                siteModel.setCart_Product_qty(vehicle_info.getString(Configvolley.Cart_Product_qty));

                String totapprice = "" + (Integer.parseInt(vehicle_info.getString(Configvolley.Cart_Product_Price).trim()) * Integer.parseInt(vehicle_info.getString(Configvolley.Cart_Product_qty).trim()));
                finalamt = finalamt + Integer.parseInt(totapprice);
                Country country = new Country(vehicle_info.getString(Configvolley.Cart_Product_Name), i + "", false, vehicle_info.getString(Configvolley.Cart_Product_Price), vehicle_info.getString(Configvolley.Cart_Product_qty), totapprice, vehicle_info.getString(Configvolley.Cart_Code));
                countryList.add(country);

                siteModelList.add(siteModel);
            }



        }
            lvlist.setAdapter(null);
            txttotalprice.setText(finalamt + " " + getResources().getString(R.string.Rs));
            txttotalprice.setTextColor(Color.parseColor("#2E4925"));
            txttotalprice.setTextSize(23);
            dataAdapter = new MyCustomAdapter(this,
                    R.layout.cartrow, countryList);
            lvlist.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
