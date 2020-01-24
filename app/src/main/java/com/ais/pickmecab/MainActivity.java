package com.ais.pickmecab;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.location.Location;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.tasks.OnCompleteListener;


import android.os.Looper;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;

import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;


import org.json.JSONException;
import org.json.JSONObject;



import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.List;




public class  MainActivity extends AppCompatActivity implements OnMapReadyCallback , TaskLoadedCallback  {
    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;

    private GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    ProgressDialog progressDialog;

    public static SharedPreferences sh;
    public static SharedPreferences.Editor editor;
    public static String str_login_test;

    BottomSheetBehavior sheetBehavior;
    LinearLayout bottom_sheet;

    Button btn_bottom_sheet, btn_bottom_sheet_dialog;

    Button btn_Accept_Ride,btn_Reject_Ride;

    NotificationDataParser Notify_data;

    ArrayList markerPoints = new ArrayList();
    MarkerOptions origin, destination;
    private Polyline currentPolyline;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery, R.id.nav_home, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Notify_data = new NotificationDataParser();

        // here initializing the shared preference
        //  sh = getSharedPreferences("myprefe", 0);
        // editor = sh.edit();

        str_login_test = SplashActivity.sh.getString("loginTest", null);

        if (str_login_test != null
                && !str_login_test.toString().trim().equals("")) {

            String _pUser = SplashActivity.sh.getString("UserName", "UnKnown");
            String _ploginId = SplashActivity.sh.getString("UserID", "UnKnown");
            View hView = navigationView.getHeaderView(0);
            TextView nav_name = hView.findViewById(R.id.nav_name);
            TextView nav_subname = hView.findViewById(R.id.nav_subname);
            nav_name.setText(_pUser);
            nav_subname.setText(_ploginId);

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Toolbar toolbar;
        // toolbar =  findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);


        Log.d(TAG, "Subscribing to android1 topic");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("android1")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END subscribe_topics]

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        checkLocationPermission();


        // get the bottom sheet view
        bottom_sheet = findViewById(R.id.bottom_sheet);
        // init the bottom sheet behavior
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        btn_bottom_sheet = findViewById(R.id.btn_bottom_sheet);
      //  btn_bottom_sheet_dialog = findViewById(R.id.btn_bottom_sheet_dialog);

        btn_Accept_Ride = bottom_sheet.findViewById(R.id.btn_accept_ride);
        btn_Reject_Ride = bottom_sheet.findViewById(R.id.btn_reject_ride);

        btn_Reject_Ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "Ride Rejected!";
                int duration = Toast.LENGTH_SHORT;
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
               // loadNavigationView(Notify_data.getLat_to(),Notify_data.getLang_to());


                }
            });




        btn_Accept_Ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "Ride Accepted!";
                int duration = Toast.LENGTH_SHORT;



                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();


                LatLng from = new LatLng(Notify_data.getLat_fr(), Notify_data.getLang_fr());
                LatLng to = new LatLng(Notify_data.getLat_to(), Notify_data.getLang_to());

                origin = new MarkerOptions().position(from).title("Pick Up");
                destination = new MarkerOptions().position(to).title("Drop Off");


                mMap.addMarker(origin);
                mMap.addMarker(destination);

                new FetchURL(MainActivity.this).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(origin.getPosition())
                        .zoom(12)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 3500, null);




            }
        });


        // bottomSheet = findViewById(R.id.bottom_sheet);
        //  sheetBehavior = BottomSheetBehavior.from(bottomSheet);


        // click event for show-dismiss bottom sheet
        btn_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    btn_bottom_sheet.setText("Close sheet");
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    btn_bottom_sheet.setText("Expand sheet");
                }
            }
        });
// callback for do something
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        btn_bottom_sheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        btn_bottom_sheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });







        final Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                sendMessage();
            }
        });
        /**
         * Open and hide sheet on button click
         */
/*
        btn_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    btn_bottom_sheet.setText("Close sheet");
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    btn_bottom_sheet.setText("Expand sheet");
                }
            }
        });
*/
        /**
         * Show BottomSheetDialog on click of button
         *//*
        btn_bottom_sheet_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View getSheetLayout = getLayoutInflater().inflate(R.layout.bottom_sheet, null);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(getSheetLayout);
                bottomSheetDialog.show();
            }
        });*/

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("Booking_IDReciver"));

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String BookingID = intent.getStringExtra("BookingID");
            fatchJob(BookingID);
           // Bundle b = intent.getBundleExtra("Location");

          //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } else {
            //Request Location Permission
            checkLocationPermission();
        }

        Bundle b = getIntent().getExtras();
        String  BookingID = ""; // or other values
        if(b != null) {
            BookingID = b.getString("BookingID");

           fatchJob(BookingID);
        }

    }



    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }

    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        MarkerOptions myLocation;
        myLocation = new MarkerOptions().position(sydney).title("Marker in Sydney");
        myLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.drivermarker));
        mMap.addMarker(myLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }

    }

    /**
     * Called when the user touches the button
     */

    private void openProgressDialog() {

        progressDialog= new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Progress Dialog");
        progressDialog.setIcon(R.drawable.user);
        //To show the dialog
        progressDialog.show();

        //To dismiss the dialog
//        progressDialog.dismiss();

    }

    private void closeProgressDialog() {
        progressDialog.dismiss();
    }


    public void fatchJob(String Booking_ID)
    {
        String sURL = "http://ec2-18-217-60-45.us-east-2.compute.amazonaws.com:8090/pickmecab/v1/api/bookings/" + Booking_ID;

                //"https://maps.googleapis.com/maps/api/geocode/json?address="+strAddress+"&key="+getString(R.string.API_KEY);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

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

                        // Process the JSON
                        try {
                            // Get the JSON array

                            JSONObject userData = response.getJSONObject("data");
                            if(userData !=null) {
                                Notify_data.setCustomerName(userData.getJSONObject("customer").getString("firstName"));
                                Notify_data.setM_placeName("New Job");
                                Notify_data.setM_cPhone(userData.getJSONObject("customer").getString("phone"));
                                Notify_data.setM_bPhone("07553349987");
                                Notify_data.setM_duration(userData.getString("startTime"));
                                Notify_data.setM_to(userData.getJSONObject("destinationAddress").getString("street"));
                                Notify_data.setM_From(userData.getJSONObject("pickupAddress").getString("completeAddress"));

                                sendMessage();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            //   return null;

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
    public void getLocationFromAddress(String strAddress, final boolean isTo){


        String sURL = "https://maps.googleapis.com/maps/api/geocode/json?address="+strAddress+"&key="+getString(R.string.API_KEY);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

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

                        // Process the JSON
                        try {
                            // Get the JSON array

                            JSONObject userData = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                            LatLng addressletlng = new LatLng(Double.parseDouble(userData.get("lat").toString()),Double.parseDouble(userData.get("lng").toString()));

                            if (isTo == true)
                            {
                                Notify_data.setLang_to(addressletlng.longitude);
                                Notify_data.setLat_to(addressletlng.latitude);

                            }
                            else
                            {
                                Notify_data.setLang_fr(addressletlng.longitude);
                                Notify_data.setLat_fr(addressletlng.latitude);

                              //  CalculationByDistance(new LatLng(Notify_data.getLat_to(),Notify_data.getLang_to()),addressletlng);



                            }

                           // return addressletlng;
                            update_bottom_shet(Notify_data, bottom_sheet);
                            closeProgressDialog();


                    } catch (JSONException e) {
                        e.printStackTrace();

                    //   return null;

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

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        String str;

        str = "KM-" + kmInDec + "Meter-" + meterInDec;
        Notify_data.setM_duration(str);

        return Radius * c;
    }
/*

    public void parseJSONLocation(LatLng point)
    {
        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
        double lat = point.latitude;
        double lng = point.longitude;

        JSONObject ret = getJSONLocationInfo(lat, lng);
        JSONObject location;
        String location_string = "";

        try {

            //Get JSON Array called "results" and then get the 0th complete object as JSON
            location = ret.getJSONArray("results").getJSONObject(0);
            // Get the value of the attribute whose name is "formatted_string"
            location_string = location.getString("formatted_address");
            //Toast.makeText(getApplicationContext(), "formattted address:" + location_string, Toast.LENGTH_LONG).show();
        } catch (JSONException e1) {
            e1.printStackTrace();

        }
    }

    public JSONObject getJSONLocationInfo(double lat, double lng)
    {
        HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=AIzaSyB3hYTOGwj2FM9rSCYxTag1VkJXpFRmkOc");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
*/

    public void sendMessage() {

        LatLng toLatlng, fromlatLng;
        Context context = getApplicationContext();
        CharSequence text = "New Job Arrived!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


        openProgressDialog();
        getLocationFromAddress(Notify_data.getM_to(),true);
        getLocationFromAddress(Notify_data.getM_From(), false);


      //  update_bottom_shet(Notify_data, bottom_sheet);


    }

    public void loadNavigationView(String _lat,String _lng){
        Uri navigation = Uri.parse("google.navigation:q="+_lat+","+_lng+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        navigationIntent.setPackage("com.google.android.apps.maps");
        startActivity(navigationIntent);
    }
    public void update_bottom_shet(NotificationDataParser _data, LinearLayout bSheet) {
        TextView mytext = bSheet.findViewById(R.id.text_cname);
        mytext.setText(_data.getCustomerName());
        mytext = bSheet.findViewById(R.id.text_title);
        mytext.setText(_data.getM_placeName());
        mytext = bSheet.findViewById(R.id.text_from);
        mytext.setText(_data.getM_From());
        mytext = bSheet.findViewById(R.id.text_to);
        mytext.setText(_data.getM_to());
        mytext = bSheet.findViewById(R.id.text_location);
        mytext.setText(_data.getM_From());
        mytext = bSheet.findViewById(R.id.text_cphone);
        mytext.setText(_data.getM_cPhone());
        mytext = bSheet.findViewById(R.id.text_time);
        mytext.setText(_data.getM_duration());
        mytext = bSheet.findViewById(R.id.text_duration);
        mytext.setText("11AM - 03PM");
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.API_KEY);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
