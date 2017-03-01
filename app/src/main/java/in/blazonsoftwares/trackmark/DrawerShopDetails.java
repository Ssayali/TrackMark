package in.blazonsoftwares.trackmark;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.blazonsoftwares.trackmark.model.WebServicesAPI;

public class DrawerShopDetails extends Fragment {
    TextView txtshopname,txtshopaddress,txtshopcno;
    ImageView shopimg;
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_shop_details, container, false);
        View view = inflater.inflate(R.layout.fragment_shop_details, container, false);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.show();
        getdata();
        return view;
    }

    private void getdata() {
        int productcode=Integer.parseInt(SessionManagement.KEY_Shopcode);
        String url = WebServicesAPI.deployment_api+"Shop/ShopallDetailsByid?shopid="+productcode;
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
            for(int i=0;i<jsonObject.length();i++) {
                JSONObject vehicle_info = jsonObject.getJSONObject(i);

                txtshopname=(TextView) getActivity().findViewById(R.id.txtshopname);
                txtshopaddress=(TextView) getActivity().findViewById(R.id.txtshopaddress);
                txtshopcno=(TextView) getActivity().findViewById(R.id.txtshopcno);
                shopimg=(ImageView) getActivity().findViewById(R.id.shopimg);

                txtshopname.setText(vehicle_info.getString(Configvolley.Shop_Name));
                txtshopaddress.setText(vehicle_info.getString(Configvolley.Shop_Add));
                txtshopcno.setText(vehicle_info.getString(Configvolley.Shop_Cno));

                if(vehicle_info.getString(Configvolley.Shop_Image).equals("")){
                    Picasso.with(getContext()).load(R.drawable.logowithout).into(shopimg);
                }
                else {
                    Picasso.with(getContext()).load(WebServicesAPI.deployment_api + vehicle_info.getString(Configvolley.Shop_Image)).into(shopimg);
                }
                txtshopname.setTextSize(30);
                txtshopcno.setTextSize(20);
                txtshopaddress.setTextSize(20);
                txtshopname.setTextColor(Color.parseColor("#1ec9fd"));
                txtshopname.setTypeface(null, Typeface.BOLD);
                pd.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Shop Details");


    }
}