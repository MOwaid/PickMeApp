package com.ais.pickmecab.ui.login;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;
import android.webkit.JavascriptInterface;
import android.content.Context;

import com.ais.pickmecab.SplashActivity;
import com.ais.pickmecab.data.LoginDataSource;
import com.ais.pickmecab.data.LoginRepository;
import com.ais.pickmecab.data.Result;
import com.ais.pickmecab.data.model.LoggedInUser;
import com.ais.pickmecab.R;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;






public class LoginViewModel extends ViewModel {

    public interface VolleyCallBack {
        void onSuccess(JSONObject result);
    }

    String URL = "http://ec2-18-217-60-45.us-east-2.compute.amazonaws.com:8090//pickmecab/v1/api/users/auth/";

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    String userName;
    String passWord;
    Context myContext;

    JSONObject userData;



    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(final String username, String password, Context mContext) {
        // can be launched in a separate asynchronous job

     //   Result<LoggedInUser> result;

        this.userName = username;
        this.passWord = password;
        this.myContext = mContext;


        getAuth(new VolleyCallBack() {

            @Override
            public void onSuccess(JSONObject response) {
                // this is where you will call the geofire, here you have the response from the volley.
                // TODO: handle loggedInUser authentication
                try {

                    if(response != null) {
                        String user = response.getString("userName");
                        String userID = response.getString("userId");
                        loginResult.setValue(new LoginResult(new LoggedInUserView(user)));
                        SplashActivity.editor.putString("UserName", user);
                        SplashActivity.editor.putString("UserID", userID);
                        SplashActivity.editor.commit();
                    }
                    else {
                        loginResult.setValue(new LoginResult(R.string.login_failed));
                    }

                }
                catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }

            }
        });



        /* result = loginRepository.login(username, password, mContext);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }*/
    }



    public void getAuth(final VolleyCallBack callBack){




        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", "0");
            jsonObject.put("loginId", this.userName);
            jsonObject.put("password", this.passWord);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        //http://localhost:8090/
        String URL = "http://ec2-18-217-60-45.us-east-2.compute.amazonaws.com:8090/pickmecab/v1/api/drivers/auth/";


        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this.myContext);

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

                            /*String crappyPrefix = "null";
                            String result = response.toString();
                            if(result.startsWith(crappyPrefix)){
                                result = result.substring(crappyPrefix.length(), result.length());
                            }

                             */
                            if(response.getString("successful").equals("true"))// getJSONObject("data") != null)
                            userData = response.getJSONObject("data");
                            else
                                userData = null;
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


    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
