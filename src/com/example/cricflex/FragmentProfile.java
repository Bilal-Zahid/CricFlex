package com.example.cricflex;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.Calendar;

public class FragmentProfile extends Fragment {



    public FragmentProfile(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DatabaseHelper helper = new DatabaseHelper(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        String username = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            username = bundle.getString("username", "not sent");
        }

        TextView pv_name = (TextView)rootView.findViewById(R.id.pv_name);
        pv_name.setText(helper.getName(username));

        TextView pv_username = (TextView)rootView.findViewById(R.id.pv_username);
        pv_username.setText(username);

        TextView pv_bowling_style = (TextView)rootView.findViewById(R.id.pv_bowling_style);
        pv_bowling_style.setText(helper.getBowlingStyle(username));

        TextView pv_career_level = (TextView)rootView.findViewById(R.id.pv_career_level);
        pv_career_level.setText(helper.getCareerLevel(username));


        TextView pv_gender = (TextView)rootView.findViewById(R.id.pv_gender);
        pv_gender.setText(helper.getGender(username));

        TextView pv_location = (TextView)rootView.findViewById(R.id.pv_location);
        pv_location.setText(helper.getLocation(username));

        TextView pv_email = (TextView)rootView.findViewById(R.id.pv_email);
        pv_email.setText(helper.getEmail(username));

        TextView pv_DOB = (TextView)rootView.findViewById(R.id.pv_dateofbirth);
        String DOB = helper.getDOB(username);
        pv_DOB.setText(DOB);

        TextView pv_weight = (TextView)rootView.findViewById(R.id.pv_weight);
        String weight = helper.getWeight(username) + " kg";
        pv_weight.setText(weight);



        String str[] = DOB.split("-");
        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        TextView pv_age = (TextView)rootView.findViewById(R.id.pv_age);
        pv_age.setText(getAge(year,month,day));

        //CircleImageView circleImageView = new CircleImageView(getActivity());
        //circleImageView =



        CircleImageView circleImageView = (CircleImageView) rootView.findViewById(R.id.profilepicture);
        //Bitmap bitmapImage1 = helper.getImage(username);

        //circleImageView.setImageBitmap(bitmapImage1);

        Bitmap b = getImageBitmap(this.getActivity(),username,"jpeg");
        circleImageView.setImageBitmap(b);







        return rootView;
    }
    public Bitmap getImageBitmap(Context context, String name, String extension){
            name=name+"."+extension;
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
    }


    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        System.out.println("Day today ,"+today.get(Calendar.DAY_OF_YEAR)  +"DOB DAY : " +dob.get(Calendar.DAY_OF_YEAR));
        if (today.get(Calendar.DAY_OF_YEAR) < (dob.get(Calendar.DAY_OF_YEAR)-31)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
