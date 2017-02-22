package in.blazonsoftwares.trackmark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by shree on 02/14/2017.
 */

public class AddToSessionManagement extends Activity {

    SharedPreferences productpref;
    SharedPreferences.Editor producteditor;
    Context product_context;
    int productPRIVATE_MODE = 0;
    private static final String productPREF_NAME = "Track mark add to cart";
    public static  String productcode = "";
    public static  String productqty = "";
    public static  String productimg = "";


    public AddToSessionManagement(Context product_context){

        this.product_context = product_context;
        productpref = product_context.getSharedPreferences(productPREF_NAME, productPRIVATE_MODE);
        producteditor = productpref.edit();
    }

    AddToSessionManagement(){}

    public void AddProductSession(String productcode, String productqty,String productimg) {
        // Storing name in pref
        producteditor.putString(this.productcode, productcode);
        producteditor.putString(this.productqty, productqty);
        producteditor.putString(this.productimg, productimg);


        // commit changes
        producteditor.commit();
    }

    public void getallproductdetails() {
        Toast.makeText(AddToSessionManagement.this,"Method call ok....",Toast.LENGTH_SHORT).show();

       /* Map<String,?> keys = productpref.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values....",entry.getKey() + ": " +
                    entry.getValue().toString());
        }*/

    }

    public void checkoutuser(){
        // Clearing all data from Shared Preferences
        producteditor.clear();
        producteditor.commit();


    }

}
