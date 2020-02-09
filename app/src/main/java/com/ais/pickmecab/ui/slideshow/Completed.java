package com.ais.pickmecab.ui.slideshow;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ais.pickmecab.Constant;
import com.ais.pickmecab.CustomListAdapter;
import com.ais.pickmecab.ListItem;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Completed extends Fragment {

    String DriverID = "";
    ArrayList<ListItem> AssignedJobs;
    ProgressDialog progress;
    Date dateto,datefrom;
    public Completed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AssignedJobs = new ArrayList<>();
        DriverID = SplashActivity.sh.getString("Driver_db_id",null);

        progress = new ProgressDialog(getContext());

        datefrom = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(datefrom);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String newDateStr = dateFormat.format(calendar.getTime());
        fetchJob(DriverID, newDateStr, fDate);

    }

    private void loadallJob()
    {
        ArrayList jobList = getListAssignJobs();
        final ListView lv = (ListView) getActivity().findViewById(R.id.user_list_completed);
        lv.setAdapter(new CustomListAdapter(getActivity(), jobList));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                ListItem user = (ListItem) lv.getItemAtPosition(position);
                Toast.makeText(getActivity(), "Selected :" + " " + user.getName()+", "+ user.getLocation(), Toast.LENGTH_SHORT).show();
            }
        });
        progress.dismiss();
    }

    private void AddNewJob(JSONObject job)
    {
        String from="",to="",customer="",start_datetime = "",BookingID=null;

        try {

            customer = job.getJSONObject("customer").getString("firstName");
            from = job.getJSONObject("pickupAddress").getString("completeAddress");
            to = job.getJSONObject("destinationAddress").getString("street");
            start_datetime = job.getString("startTime");
            BookingID = job.getString("id");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListItem Jobitem = new ListItem();
        Jobitem.setName(from);
        Jobitem.setDesignation(to);
        Jobitem.setLocation("Pickup - " +customer);
        Jobitem.setStart_time(start_datetime);
        Jobitem.setBookingID(BookingID);
        AssignedJobs.add(Jobitem);
    }

    private ArrayList getListAssignJobs() {

        return AssignedJobs;
    }
    public void showLoading()
    {

        progress.setTitle("Loading...");
        progress.setMessage("Please wait while loading Jobs...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

    }

    public void fetchJob(String Driver_ID, String to, String from)
    {
        String sURL = Constant.WEB_API_PATH+"bookings/driverBooking/"+Driver_ID;
        showLoading();
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


                        JSONArray Jobs = null;
                        try {
                            Jobs = response.getJSONArray("data");

                            // Do something with response
                            //mTextView.setText(response.toString());

                            // Process the JSON
                            try{
                                // Loop through the array elements
                                for(int i=0;i<Jobs.length();i++){
                                    // Get current json object
                                    JSONObject Job = Jobs.getJSONObject(i);
                                    if((Job.getString("status")).equals("COMPLETED"))
                                        AddNewJob(Job);

                                }
                                loadallJob();

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred

                        Log.e("Error", "Error at sign in : " + error.getMessage());
                        // return null;
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }
}
