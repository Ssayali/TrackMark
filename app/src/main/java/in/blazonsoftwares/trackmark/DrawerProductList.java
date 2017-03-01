package in.blazonsoftwares.trackmark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by shree on 02/06/2017.
 */

public class DrawerProductList  extends Fragment {

    //product list bind
    private ListView lvlist;
    private Model siteModel;
    private ProgressDialog pd;
    private List<Model> siteModelList;
    public ImageLoader imageLoader;
    public String newshopcode;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        //add to cart code

        //product list
        imageLoader = ImageLoader.getInstance();
        lvlist=(ListView) view.findViewById(R.id.lvlist);
        lvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getContext(),DetailsAcitvity.class);
                i.putExtra("Productid", siteModelList.get(position).getProduct_MasterId().toString());
                startActivity(i);
            }
        });
        getdata();
     return view;
    }
    private void getdata()
    {
        int shopcode=Integer.parseInt(SessionManagement.KEY_Shopcode);
        String url = WebServicesAPI.deployment_api+"Shop/ProductDetailsByid?shopid="+shopcode;
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){

        try {

            JSONArray jsonObject = new JSONArray(response);

            siteModelList=new ArrayList<>();
            for(int i=0;i<jsonObject.length();i++) {
                JSONObject vehicle_info = jsonObject.getJSONObject(i);
                    siteModel=new Model();
                    siteModel.setProduct_Image(WebServicesAPI.deployment_api+vehicle_info.getString(Configvolley.Product_Image));
                    siteModel.setProduct_No(vehicle_info.getString(Configvolley.Product_No));
                    siteModel.setProduct_Name(vehicle_info.getString(Configvolley.Product_Name));
                    siteModel.setProduct_price(vehicle_info.getString(Configvolley.Product_price));
                    siteModel.setProduct_Spec(vehicle_info.getString(Configvolley.Product_Spec));
                    siteModel.setProduct_Shopcode(vehicle_info.getString(Configvolley.Product_Shopcode));
                    siteModel.setProduct_MasterId(vehicle_info.getString(Configvolley.Product_MasterId));

                siteModelList.add(siteModel);
            }
            showdata(siteModelList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showdata(List<Model> result) {
        if(result!=null) {

            SiteAdapter adapter = new SiteAdapter(getContext(), R.layout.row, result);
            lvlist.setAdapter(adapter);
        }
        else
        {
            new AlertDialog.Builder(getContext())

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
            ViewHolder holder=null;

            imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
            if(convertView==null)
            {
                holder=new ViewHolder();
                convertView=inflater.inflate(resource,null);

                holder.Product_Image= (ImageView) convertView.findViewById(R.id.logo);
                holder.Product_No= (TextView) convertView.findViewById(R.id.sitename);
                holder.Product_Name=(TextView)convertView.findViewById(R.id.description);
                convertView.setTag(holder);
            }else
            {
                holder=(ViewHolder)convertView.getTag();
            }

            final ProgressBar pgbar=(ProgressBar)convertView.findViewById(R.id.pgBar);
            holder.Product_No.setText( getResources().getString(R.string.Rs)+" "+siteModelList.get(position).getProduct_price());
            holder.Product_Name.setText(siteModelList.get(position).getProduct_Name());
            holder.Product_Name.setTextSize(25);
            holder.Product_No.setTextSize(17);
            holder.Product_Name.setTextColor(Color.parseColor("#1987fa"));
            ImageLoader.getInstance().displayImage(siteModelList.get(position).getProduct_Image(), holder.Product_Image, new ImageLoadingListener() {
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
            });
            return convertView;
        }
        class ViewHolder
        {

            private ImageView Product_Image;
            private TextView Product_No;
            private TextView Product_Name;


        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Track Mark Product List");
    }
}
