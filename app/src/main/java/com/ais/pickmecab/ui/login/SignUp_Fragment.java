package com.ais.pickmecab.ui.login;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ais.pickmecab.CustomToast;
import com.ais.pickmecab.R;
import com.ais.pickmecab.Utils;
import com.ais.pickmecab.ui.login.LoginActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUp_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    JSONObject userData;

    String getFullName;
    String getEmailId ;
    String getMobileNumber;
    String getLocation;
    String getPassword;
    String getConfirmPassword;


    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fullName = (EditText) view.findViewById(R.id.fullName);
        emailId = (EditText) view.findViewById(R.id.userEmailId);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        location = (EditText) view.findViewById(R.id.location);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        login = (TextView) view.findViewById(R.id.already_user);
        terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                new LoginActivity().replaceLoginFragment();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
         getFullName = fullName.getText().toString();
         getEmailId = emailId.getText().toString();
         getMobileNumber = mobileNumber.getText().toString();
         getLocation = location.getText().toString();
         getPassword = password.getText().toString();
         getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");

            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password don't match.");

            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please select Terms and Conditions.");

            // Else do signup or do your stuff
        else {
           // Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
            //        .show();

            getAuth(new LoginViewModel.VolleyCallBack() {

                @Override
                public void onSuccess(JSONObject response) {
                    // this is where you will call the geofire, here you have the response from the volley.
                    // TODO: handle loggedInUser authentication
                   // try {

                        if(response != null) {
                            Toast.makeText(getActivity(), "Registration Successful. Please Login.", Toast.LENGTH_SHORT)
                                    .show();
                            new LoginActivity().replaceLoginFragment();
                        }
                        else {
                            //  loginResult.setValue(new LoginResult(R.string.login_failed));
                        }

                  //  }
                   // catch (JSONException e) {
                        // TODO Auto-generated catch block
                     //   e.printStackTrace();
                        // loginResult.setValue(new LoginResult(R.string.login_failed));
                  //  }

                }
            });

        }

    }
    public void getAuth(final LoginViewModel.VolleyCallBack callBack){




        JSONObject jsonObject= new JSONObject();
        try {

            jsonObject.put("firstName", getFullName);
            jsonObject.put("lastName", getFullName);
            jsonObject.put("loginId", getEmailId);
            jsonObject.put("password", getPassword);
         //   jsonObject.put("mobilePhone", getMobileNumber);
          //  jsonObject.put("address", getLocation);



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        //http://localhost:8090/
        String URL = "http://ec2-18-217-60-45.us-east-2.compute.amazonaws.com:8090/pickmecab/v1/api/drivers/";


        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // Initialize a new JsonObjectRequest instance


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());


                        // Process the JSON
                        try {
                            // Get the JSON array

                            userData = response.getJSONObject("data");
                            callBack.onSuccess(userData);
                            // Loop through the array elements
                         /*   for (int i = 0; i < array.length(); i++) {
                                // Get current json object
                                JSONObject student = array.getJSONObject(i);

                                // Get the current student (json object) data
                                String UserName = student.getString("Username");
                                String password = student.getString("password");
                                //  String age = student.getString("age");

                                // Display the formatted json data in text view
                                //  mTextView.append(firstName +" " + lastName +"\nage : " + age);
                                // mTextView.append("\n\n");
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callBack.onSuccess(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred

                        Log.e("Error", "Error at sign in : " + error.getMessage());
                        callBack.onSuccess(null);
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);


    }
}