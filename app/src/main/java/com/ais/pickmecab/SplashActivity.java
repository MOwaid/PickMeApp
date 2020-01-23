package com.ais.pickmecab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.ais.pickmecab.ui.login.LoginActivity;


public class SplashActivity extends AppCompatActivity {



    public static SharedPreferences sh;
    public static SharedPreferences.Editor editor;
    public static String str_login_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        String BookingID = "";
        Boolean isBooking_id_found = false;
        super.onCreate(savedInstanceState);
        // here initializing the shared preference
        sh = getSharedPreferences("myprefe", 0);
        editor = sh.edit();

        // check here if user is login or not
        str_login_test = sh.getString("loginTest", null);

        if (str_login_test != null
                && !str_login_test.toString().trim().equals("")) {
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    String value = getIntent().getExtras().getString(key);
                    Log.d("ABC", "Key: " + key + " Value: " + value);
                    if (key.equals("booking-id"))
                    {
                        isBooking_id_found = true;
                        BookingID = value;
                    }

                }
            }



            Intent send = new Intent(getApplicationContext(),
                        MainActivity.class);
            if(isBooking_id_found){
                        Bundle b = new Bundle();
                        b.putString("BookingID",BookingID);
                        send.putExtras(b);
            }
            startActivity(send);
            }

        /*
         * if user login test is false on oncreate then redirect the
         * user to login & registration page
         */
        else {

            Intent send = new Intent(getApplicationContext(),
                    LoginActivity.class);
            startActivity(send);

        }

        finish();
    }
}
