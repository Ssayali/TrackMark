package in.blazonsoftwares.trackmark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import in.blazonsoftwares.trackmark.model.WebServicesAPI;

public class DrawerOwnerDetails extends Fragment {
    ProgressDialog pd;
    TextView txt_ucode,txt_uemail,txt_uname,txt_uadd,txt_ucno;

    ImageView callimg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_details, container, false);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.show();
        getdata();


        callimg = (ImageView) view.findViewById(R.id.callimg);
        callimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:" + txt_ucno.getText()));
                startActivity(intent);
            }
        });

        return view;
    }

    private void getdata() {
        int productcode=Integer.parseInt(SessionManagement.KEY_Shopcode);

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void showJSON(String response){
        try {
            JSONArray jsonObject = new JSONArray(response);
            for(int i=0;i<jsonObject.length();i++) {
                JSONObject vehicle_info = jsonObject.getJSONObject(i);

                txt_ucode=(TextView) getActivity().findViewById(R.id.txt_ucode);
                txt_uemail=(TextView) getActivity().findViewById(R.id.txt_uemail);
                txt_uname=(TextView) getActivity().findViewById(R.id.txt_uname);
                txt_uadd=(TextView) getActivity().findViewById(R.id.txt_uadd);
                txt_ucno=(TextView) getActivity().findViewById(R.id.txt_ucno);


                txt_ucode.setText("Code : "+vehicle_info.getString(Configvolley.User_Code));
                txt_uemail.setText("Email : "+vehicle_info.getString(Configvolley.User_Email));
                txt_uname.setText("Name : "+vehicle_info.getString(Configvolley.User_Name));
                txt_uadd.setText("Address : "+vehicle_info.getString(Configvolley.User_Address));
                txt_ucno.setText("Contact No : "+vehicle_info.getString(Configvolley.User_cno));

                txt_ucode.setTextSize(20);
                txt_uemail.setTextSize(20);
                txt_uname.setTextSize(20);
                txt_uadd.setTextSize(20);
                txt_ucno.setTextSize(20);
                txt_uname.setTypeface(null, Typeface.BOLD);
                txt_uemail.setTypeface(null, Typeface.BOLD);
                txt_ucode.setTypeface(null, Typeface.BOLD);
                txt_uadd.setTypeface(null, Typeface.BOLD);
                txt_ucno.setTypeface(null, Typeface.BOLD);
                pd.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Owner Details");
    }


}