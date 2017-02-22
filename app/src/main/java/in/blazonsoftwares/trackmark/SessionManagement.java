package in.blazonsoftwares.trackmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref,productpref;


    // Editor for Shared preferences
    Editor editor,producteditor;

    // Context
    Context _context,product_context;

    // Shared pref mode
    int PRIVATE_MODE = 0,productPRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Blazon Test";
    private static final String productPREF_NAME = "Blazon Test";


    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static String KEY_EMAIL = "email";

    public static String KEY_Shopcode = "email";


    //product session value
    public static String productcode = "";
    public static String productqty = "";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();


        this.product_context = context;
        productpref = product_context.getSharedPreferences(productPREF_NAME, productPRIVATE_MODE);
        producteditor = productpref.edit();

    }

    SessionManagement() {
    }


    /**
     * Create login session
     */
    public void createLoginSession(String name, String email) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    public void AddProductSession(String productcode, String productqty) {
        // Storing name in pref
        producteditor.putString(productcode, productcode);
        producteditor.putString(productqty, productqty);

        // commit changes
        producteditor.commit();
    }

    public Map<String, String> getallproductdetails() {

        Map<String, ?> allEntries = productpref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            // Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }

        return (Map<String, String>) allEntries;
    }





    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}