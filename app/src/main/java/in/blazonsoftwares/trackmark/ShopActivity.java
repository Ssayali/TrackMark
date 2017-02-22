package in.blazonsoftwares.trackmark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShopActivity extends AppCompatActivity {
String shopname;

    TextView txtshopname,txtshopaddress,txtshopcno,txtshopowner,txtshopownercno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shopname = getIntent().getExtras().getString("shopname");

        txtshopname=(TextView)findViewById(R.id.txtshopname);
        txtshopaddress=(TextView)findViewById(R.id.txtshopaddress);
        txtshopcno=(TextView)findViewById(R.id.txtshopcno);
        txtshopowner=(TextView)findViewById(R.id.txtshopowner);
        txtshopownercno=(TextView)findViewById(R.id.txtshopownercno);


        txtshopname.setText(shopname);
        txtshopaddress.setText("Shop Adress");
        txtshopcno.setText("Shop Contact No");
        txtshopowner.setText("Shop Owner Name");
        txtshopownercno.setText("Shop Owner Name Contact No");

    }
    public void btnclose_Click(View v) {
        try{
            Intent i = new Intent(ShopActivity.this, MapsActivity.class);
            startActivity(i);

        }
        catch (Exception ex){

        }

    }

    public void btnChat_Click(View v) {
        try{

            Toast.makeText(ShopActivity.this, "Chat Is Comming Soon...." ,
                    Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){

        }

    }




}
