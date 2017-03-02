package in.blazonsoftwares.trackmark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        session = new SessionManagement(getApplicationContext());
        vistormail=session.getUserDetails().get("email");

        imageLoader = ImageLoader.getInstance();
        lvlist=(ListView) findViewById(R.id.lvlist);
        getdata();

        /*lvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/


        checkcart=(CheckBox)findViewById(R.id.checkcart);
        lvlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*this.parent = parent;
                this.listener = listener;
                if(v.getId() == checkcart.getId()) {
                    listener.onClickCheck(parent.orderItemInfoFilterList.get(getAdapterPosition()).getItem(), parent.orderItemInfoFilterList.get(getAdapterPosition()).getProductName(),parent.orderItemInfoFilterList.get(getAdapterPosition()).getQty());
                }*/

            }
        });

    }

    private void getdata()
    {
        //int shopcode=1;
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
             siteModelList=new ArrayList<>();
            for(int i=0;i<jsonObject.length();i++) {
                JSONObject vehicle_info = jsonObject.getJSONObject(i);
                siteModel=new Model();
                siteModel.setCart_Product_Name(vehicle_info.getString(Configvolley.Cart_Product_Name));
                siteModel.setCart_Product_Price(vehicle_info.getString(Configvolley.Cart_Product_Price));
                siteModel.setCart_Product_qty(vehicle_info.getString(Configvolley.Cart_Product_qty));

                siteModelList.add(siteModel);
            }
            showdata(siteModelList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showdata(List<Model> result) {
        if(result!=null) {
            CartList.SiteAdapter adapter = new CartList.SiteAdapter(this, R.layout.cartrow, result);
            lvlist.setAdapter(adapter);
        }
        else
        {
            new AlertDialog.Builder(CartList.this)

                    .setTitle("NO INTERNET CONNECTION")
                    .setMessage("Click Yes for Settings")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();

        }
    }
    public class SiteAdapter extends ArrayAdapter {

        private  List<Model> siteModelList;
        private  int resource;
        private LayoutInflater inflater;
        public SiteAdapter(Context context, int resource, List<Model> objects) {
            super(context, resource, objects);
            siteModelList=objects;
            this.resource=resource;
            inflater= (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CartList.SiteAdapter.ViewHolder holder=null;

            imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
            if(convertView==null)
            {
                holder=new CartList.SiteAdapter.ViewHolder();
                convertView=inflater.inflate(resource,null);

               // holder.Product_Image= (ImageView) convertView.findViewById(R.id.logo);
                holder.Product_No= (TextView) convertView.findViewById(R.id.sitename);
                holder.Product_Name=(TextView)convertView.findViewById(R.id.description);

                convertView.setTag(holder);
            }else
            {
                holder=(CartList.SiteAdapter.ViewHolder)convertView.getTag();
            }

            final ProgressBar pgbar=(ProgressBar)convertView.findViewById(R.id.pgBar);
            holder.Product_No.setText( getResources().getString(R.string.Rs)+" "+siteModelList.get(position).getCart_Product_Price());
            holder.Product_Name.setText(siteModelList.get(position).getCart_Product_Name()+" ("+siteModelList.get(position).getCart_Product_qty()+") ");
          //  holder.productqty.setText(siteModelList.get(position).getProduct_qty());


            holder.Product_Name.setTextSize(25);
            holder.Product_No.setTextSize(17);

            holder.Product_Name.setTextColor(Color.parseColor("#1987fa"));
           /* ImageLoader.getInstance().displayImage(siteModelList.get(position).getProduct_Image(), holder.Product_Image, new ImageLoadingListener() {
                public void onLoadingStarted(String imageUri, View view) {
                    pgbar.setVisibility(view.VISIBLE);
                }


                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    pgbar.setVisibility(view.GONE);
                }


                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    pgbar.setVisibility(view.GONE);
                }


                public void onLoadingCancelled(String imageUri, View view) {
                    pgbar.setVisibility(view.GONE);
                }
            });*/
            return convertView;
        }
        class ViewHolder
        {
           // private ImageView Product_Image;
            private TextView Product_No;
            private TextView Product_Name;


        }
    }
}
