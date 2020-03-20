package com.ais.pickmecab.ui.slideshow;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


import android.widget.AdapterView;

import android.widget.Toast;

import com.ais.pickmecab.Constant;
import com.ais.pickmecab.CustomDialog;
import com.ais.pickmecab.CustomListAdapter;
import com.ais.pickmecab.ListItem;
import com.ais.pickmecab.ListItemComparator;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Accepted extends Fragment {

String DriverID = "";
    ArrayList<ListItem> AssignedJobs;

    ProgressDialog progress;
    Date dateto,datefrom;
    public Accepted() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accepted, container, false);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AssignedJobs = new ArrayList<>();
        DriverID = SplashActivity.sh.getString("Driver_db_id",null);


        ((MainActivity)getActivity()).closesheet();
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
        final ListView lv = (ListView) getActivity().findViewById(R.id.user_list_accepted);
        lv.setAdapter(new CustomListAdapter(getActivity(), jobList));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                ListItem user = (ListItem) lv.getItemAtPosition(position);
                Toast.makeText(getActivity(), "Selected :" + " " + user.getBookingID()+", "+ user.getLocation(), Toast.LENGTH_SHORT).show();
                showCustomDialog(user);
            }
        });

       // getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        // To dismiss the dialog
        progress.dismiss();

       
    }

    private void showCustomDialog(ListItem job) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
      /*  ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),
                                "OK was clicked",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/

        CustomDialog cdd=new CustomDialog(getActivity(),job, 0);
        cdd.show();
    }


    private void AddNewJob(JSONObject job)
    {
        String from="",to="",customer="",start_datetime = "",BookingID=null;

        try {

            start_datetime = job.getString("startTime");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
            Date strDate = sdf.parse(start_datetime);
            Date Todatdate = new Date();
            if (Todatdate.before(strDate) || Todatdate.equals(strDate) ) {



             customer = job.getJSONObject("customer").getString("firstName");
             from = job.getJSONObject("pickupAddress").getString("street");
             to = job.getJSONObject("destinationAddress").getString("street");

             BookingID = job.getString("id");
            }
            else
            {
                return ;
            }

        } catch (JSONException e) {
            progress.dismiss();
            e.printStackTrace();
        } catch (ParseException e) {
            progress.dismiss();
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
        if(AssignedJobs.size()>1) {
            Collections.sort(AssignedJobs, new ListItemComparator());
          /*  ArrayList<ListItem> currentJobs;
            currentJobs = new ArrayList<>();
            currentJobs.add(AssignedJobs.get(0));*/

        return AssignedJobs;
        }
        else
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
        String sURL = Constant.WEB_API_PATH+"bookings/driverBookingBystartTime/"+Driver_ID;
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
                                    if((Job.getString("status")).equals("CONFIRMED"))
                                    AddNewJob(Job);

                                }
                                loadallJob();

                            }catch (JSONException e){
                                e.printStackTrace();
                                progress.dismiss();
                            }
                        } catch (JSONException e) {
                            progress.dismiss();
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
                        progress.dismiss();
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }
}
