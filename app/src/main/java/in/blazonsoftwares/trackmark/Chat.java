package in.blazonsoftwares.trackmark;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;

    String currentDateTimeString;
    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);


        session = new SessionManagement(getApplicationContext());
        String loginuserdetails=session.getUserDetails().get("email").toString().replace(".", "-");

        reference1 = new Firebase("https://trackmark-2a9be.firebaseio.com/messages/" + loginuserdetails + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://trackmark-2a9be.firebaseio.com/messages/" + UserDetails.chatWith + "_" + loginuserdetails);


        messageArea.setSingleLine(false);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                currentDateTimeString = DateFormat.getTimeInstance().format(new Date());


                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    map.put("Time", currentDateTimeString);

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                    messageArea.setText("");
                    messageArea.setFocusable(true);
                }
            }
        });



        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String gettime = map.get("Time").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox(" "+message+"\n  "+gettime, 1);
                }
                else{
                    addMessageBox(" "+message+"\n  "+gettime, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }

    public void addMessageBox(String message, int type){

        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 10);
        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(lp);
        textView.setPadding(20,20,20,20);
        if(type == 1) {
            lp.gravity= Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_right_green);
            textView.setGravity(Gravity.RIGHT);
        }
        else{
            lp.gravity= Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_left_gray);
            textView.setGravity(Gravity.LEFT);
        }
        layout.addView(textView);


        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}