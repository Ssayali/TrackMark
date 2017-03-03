package in.blazonsoftwares.trackmark;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CheckboxAdapter extends ArrayAdapter<String> {
    private LayoutInflater mInflater;

    private String[] mStrings;
    private TypedArray mIcons;
    private int mViewResourceId;

    ArrayList<String> selectedStrings = new ArrayList<String>();

    public CheckboxAdapter(Context ctx,int viewResourceId,String[] strings){
        super(ctx,viewResourceId,strings);

        mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mStrings = strings;

        mViewResourceId = viewResourceId;
    }

    public int getCount(){
        return mStrings.length;
    }

    public String getItem(int position){
        return mStrings[position];
    }

    public long getItemId(int position){
        return 0;
    }
    ArrayList<String> getSelectedString(){
        return selectedStrings;
    }

    public View getView(int position,View convertView,ViewGroup parent){
        convertView = mInflater.inflate(mViewResourceId, null);

        CheckBox tv = (CheckBox)convertView.findViewById(R.id.checkcart);
        //tv.setText(mStrings[position]);



        tv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedStrings.add(buttonView.getText().toString());
                }else{
                    selectedStrings.remove(buttonView.getText().toString());
                }

            }
        });

        return convertView;
    }
}