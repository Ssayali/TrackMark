package in.blazonsoftwares.trackmark;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.blazonsoftwares.trackmark.model.WebServicesAPI;

public class VisitorDetails extends AppCompatActivity {
String useremail="";

    EditText visitoremail,visitorcode,visitoraddress,visitocno,txt_pass,txt_conpass;
    Button btn_updateddetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_details);
        useremail=getIntent().getExtras().getString("emailname");


        visitoremail=(EditText)findViewById(R.id.visitoremail);
        visitorcode=(EditText)findViewById(R.id.visitorcode);
        visitoraddress=(EditText)findViewById(R.id.visitoraddress);
        visitocno=(EditText)findViewById(R.id.visitocno);
        txt_pass=(EditText)findViewById(R.id.txt_pass);
        txt_conpass=(EditText)findViewById(R.id.txt_conpass);



        binddata(useremail);

        btn_updateddetails=(Button)findViewById(R.id.btn_updateddetails);
        btn_updateddetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                     //   String url = WebServicesAPI.deployment_api+"shop/UpdatVisitor?User_Email='"+visitoremail.getText().toString()+"'?User_password=''?User_Name=''?User_Address='"+visitoraddress.getText().toString()+"'?User_cno='"+visitocno.getText().toString()+"'";
                        String url = WebServicesAPI.deployment_api+"shop/UpdatVisitor?User_Email=om@gmail.com&User_password=123456&User_Name=s&User_Address=pune&User_cno=8888888";
                        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(VisitorDetails.this, "Record Updated Successfully..." + response, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(VisitorDetails.this, LoginActivity.class);
                                startActivity(i);
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        int socketTimeout = 30000; // 30 seconds. You can change it
                                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                        Toast.makeText(VisitorDetails.this,"error call...."+error,Toast.LENGTH_LONG).show();
                                    }
                                });
                        RequestQueue requestQueue = Volley.newRequestQueue(VisitorDetails.this);
                        requestQueue.add(stringRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void binddata(String useremail) {
        try
        {
            String url = WebServicesAPI.deployment_api+"shop/getVisitorDetails?username="+useremail;
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
        catch (Exception ex3){
            Toast.makeText(VisitorDetails.this,"this is error"+ex3,Toast.LENGTH_SHORT).show();
        }
    }
    private void showJSON(String response){
        try {
            JSONArray jsonObject = new JSONArray(response);
            for(int i=0;i<jsonObject.length();i++) {
                JSONObject vehicle_info = jsonObject.getJSONObject(i);
                visitoremail.setText(vehicle_info.getString(Configvolley.Member_Email));
                visitorcode.setText(vehicle_info.getString(Configvolley.Member_Code));
                visitoraddress.setText(vehicle_info.getString(Configvolley.Member_Address));
                visitocno.setText(vehicle_info.getString(Configvolley.Member_cno));
                txt_pass.setText(vehicle_info.getString(Configvolley.Member_password));
                txt_conpass.setText(vehicle_info.getString(Configvolley.Member_password));
                visitoremail.setTextSize(20);
                visitorcode.setTextSize(20);
                visitorcode.setTextSize(20);
                visitoraddress.setTextSize(20);
                visitocno.setTextSize(20);
                txt_pass.setTextSize(20);
                txt_conpass.setTextSize(20);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
