package com.ais.pickmecab.ui.profile;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ais.pickmecab.Constant;
import com.ais.pickmecab.MainActivity;
import com.ais.pickmecab.R;
import com.ais.pickmecab.SplashActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class profile extends Fragment {

    public ImageView btn_take_img;
    ImageView mIcon;
    RoundedBitmapDrawable mDrawable;
    ProgressDialog progressDialog;

    private ProfileViewModel mViewModel;
    JSONObject m_pDriver = null;

    public static profile newInstance() {
        return new profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel

        mIcon =  getActivity().findViewById(R.id.ivProfile);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating, please wait...");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);

         mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        mDrawable.setCircular(true);

        mDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.DST_OVER);
        mIcon.setImageDrawable(mDrawable);


        btn_take_img = getActivity().findViewById(R.id.edit_img);

        loadProfile();
        btn_take_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

               /* Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);*/

            }
        });


    }

    public void loadProfile()
    {


        String DrvID = SplashActivity.sh.getString("Driver_db_id",null);
        if(!DrvID.equals("null"))
        fetchDriver(DrvID);
    }
public void fillProfile(JSONObject drv) {

        try {
            m_pDriver = drv;

            if(m_pDriver==null) return;

            TextView mytext = getActivity().findViewById(R.id.name);
            mytext.setText(drv.getString("firstName") +" "+ drv.getString("lastName"));
            mytext =  getActivity().findViewById(R.id.location);
            Object obj = drv.get("address");

            if(obj!=null && !obj.toString().equals("null")) {
                JSONObject addr = drv.getJSONObject("address");
                mytext.setText(addr.getJSONObject("county").toString());
                mytext = getActivity().findViewById(R.id.designation);
                mytext.setText(addr.getJSONObject(("state")).toString());
            }
            mytext =  getActivity().findViewById(R.id.gender);
            mytext.setText(drv.getString("sex"));
            mytext =  getActivity().findViewById(R.id.text_ethnicity);
            mytext.setText(drv.getString("ethnicity"));

            mytext =  getActivity().findViewById(R.id.text_loginid);
            mytext.setText(drv.getString("loginId"));

            mytext =  getActivity().findViewById(R.id.mobileNumber);
            mytext.setText(drv.getString("mobilePhone"));

            mytext =  getActivity().findViewById(R.id.email);
            mytext.setText(drv.getString("email"));

            mytext =  getActivity().findViewById(R.id.approved_by);
            mytext.setText(drv.getString("status"));

            final String imageString = drv.getString("photo");
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            mDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodedByte);
            mDrawable.setCircular(true);
            mDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.DST_OVER);

            mIcon.setImageDrawable(mDrawable);



        }catch (JSONException e) {
            //progress.dismiss();
            e.printStackTrace();
    }



}
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
           /* case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    mIcon.setImageURI(selectedImage);
                }

                break;*/
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();


                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        mDrawable.setCircular(true);
                        mDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.DST_OVER);

                        mIcon.setImageDrawable(mDrawable);

                        uploadImage(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                break;
        }
    }

    public void uploadImage(Bitmap bitmap)
    {

        if(m_pDriver==null) return;
        progressDialog.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);



        JSONObject Photo = null;
        try {


            Photo =m_pDriver;
            Photo.put ("photo", imageString);
           /*  jsonObject.put("lastName", getFullName);
            jsonObject.put("loginId", getEmailId);
            jsonObject.put("password", getPassword);*/
            //   jsonObject.put("mobilePhone", getMobileNumber);
            //  jsonObject.put("address", getLocation);



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        //http://localhost:8090/
        String URL = Constant.WEB_API_PATH+"drivers/";


        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonObjectRequest instance


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                Photo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        progressDialog.dismiss();



                        // Process the JSON
                        try {
                            // Get the JSON array
                            JSONObject resObject= new JSONObject();
                            resObject = response.getJSONObject("data");

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
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred

                        Log.e("Error", "Error at sign in : " + error.getMessage());
                        progressDialog.dismiss();
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);


    }

    public void fetchDriver(String Driver_ID)
    {


        progressDialog.show();

        String sURL = Constant.WEB_API_PATH+"/drivers/"+Driver_ID;
        //showLoading();

        //"https://maps.googleapis.com/maps/api/geocode/json?address="+strAddress+"&key="+getString(R.string.API_KEY);


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                sURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        progressDialog.dismiss();
                        JSONObject Driver = null;
                        try {
                            Driver = response.getJSONObject("data");

                            if(Driver !=null) {
                                fillProfile(Driver);

                            }

                            // Do something with response


                        } catch (JSONException e) {
                            //progress.dismiss();
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        progressDialog.dismiss();
                        Log.e("Error", "Error at sign in : " + error.getMessage());
                        // return null;
                        //  progress.dismiss();
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }

}
