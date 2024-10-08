package com.ais.pickmecab;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.tasks.OnCompleteListener;

import android.os.Handler;
import android.os.Looper;

import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.location.LocationManagerCompat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ais.pickmecab.Constant;

import com.google.firebase.messaging.RemoteMessage;


public class  MainActivity extends AppCompatActivity implements  OnMapReadyCallback, TaskLoadedCallback  {
    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;

    private GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    JSONArray AllBooking;
    JSONObject Booking;
    ImageView mIcon;
    CountDown timer;
    RoundedBitmapDrawable mDrawable;

    public JSONObject getM_pDriver() {
        return m_pDriver;
    }

    public void setM_pDriver(JSONObject m_pDriver) {
        this.m_pDriver = m_pDriver;
    }

    JSONObject m_pDriver = null;

    private boolean firstTimeFlag = true;

    ProgressDialog progressDialog;


    public static String str_login_test;

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetDialog summarySheetDlg;

    View getSheetLayout,getSummaryLayout;
    LinearLayout bottom_sheet;
    Button btn_bottom_sheet, btn_bottom_sheet_dialog;
    FloatingActionButton fab;

    Button btn_Accept_Ride,btn_Reject_Ride,btn_close_dlg,Btn_close_summay;
    Button btn_begin_Ride, btn_End_Ride,btn_drv_arrived,btn_POB,btn_no_show, btn_via;
    Switch mySwitch;

    NotificationDataParser Notify_data;
    ArrayList markerPoints = new ArrayList();
    MarkerOptions origin, destination;
    MarkerOptions DriverGuider;
    Marker PointA, PointB;
    private Polyline currentPolyline;

    Bitmap mMarkerIcon;
    private List<LatLng> mPathPolygonPoints;

    int mIndexCurrentPoint;
    Boolean JobinProgress = false;
    Menu item;
    String  BookingID=null;
    String FunctionType;
    String currentBookingID="";
    String DriverID = null, DriverStatus = "";
    NavController navController;
     float start_rotation = 0;
     int stopcount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DriverGuider = null;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setTitle("my title");
       /* mIcon =  findViewById(R.id.image_profile);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.circle);

        mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        mDrawable.setCircular(true);

        mDrawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.DST_OVER);
        mIcon.setImageDrawable(mDrawable);*/

      /*  ImageButton btnNav = findViewById(R.id.NavBtn);
        btnNav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (DriverGuider !=null)
                    loadNavigationView(DriverGuider);
            }
        });*/


         fab = findViewById(R.id.Nav);
        fab.setImageResource(R.drawable.direction_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DriverGuider !=null)
                loadNavigationView(DriverGuider);

            }
        });


        getSheetLayout = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(getSheetLayout);
        bottomSheetDialog.setCancelable(false);


        getSummaryLayout = getLayoutInflater().inflate(R.layout.summary_sheet, null);
        summarySheetDlg = new BottomSheetDialog(MainActivity.this);
        summarySheetDlg.setContentView(getSummaryLayout);
        summarySheetDlg.setCancelable(false);

        // get the bottom sheet view
        bottom_sheet = findViewById(R.id.bottom_sheet);
        // init the bottom sheet behavior
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        btn_bottom_sheet = findViewById(R.id.btn_bottom_sheet);
        //btn_bottom_sheet_dialog = findViewById(R.id.btn_bottom_sheet_dialog);

        Btn_close_summay = getSummaryLayout.findViewById(R.id.btn_Summclose);

        btn_Accept_Ride = getSheetLayout.findViewById(R.id.btn_accept_ride);
        btn_close_dlg =  getSheetLayout.findViewById(R.id.btn_close);
        btn_Reject_Ride = getSheetLayout.findViewById(R.id.btn_reject_ride);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery, R.id.nav_home, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    /*    NavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
               bottom_sheet.setVisibility(View.GONE);
              /*  switch (menuItem.getItemId()) {
                    case R.id.navItem1:
                        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                        startActivity(alarm);
                        break;
                    case R.id.navItem2:
                        try {
                            export();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });*/



        Notify_data = new NotificationDataParser();
        JobinProgress = false;

        // here initializing the shared preference
        //  sh = getSharedPreferences("myprefe", 0);
        // editor = sh.edit();

        str_login_test = SplashActivity.sh.getString("loginTest", null);
        DriverID = SplashActivity.sh.getString("Driver_db_id",null);

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

        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.the_car);
        mIndexCurrentPoint = 0;


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

        FirebaseMessaging.getInstance().subscribeToTopic(DriverID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


       // FirebaseMessaging.getInstance().send();
        // [END subscribe_topics]

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        checkLocationPermission();



        btn_Reject_Ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "Ride Rejected!";
                int duration = Toast.LENGTH_SHORT;
               // sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
                UpdateJob(BookingID,0);
                resetJobSheet();
                JobinProgress = false;
                bottomSheetDialog.dismiss();

               // loadNavigationView(Notify_data.getLat_to(),Notify_data.getLang_to());


                }
            });


        btn_close_dlg.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 bottomSheetDialog.dismiss();

                                             }
                                         });

        Btn_close_summay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 summarySheetDlg.dismiss();

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
                DriverGuider = origin;

               PointA =  mMap.addMarker(origin);
                PointB = mMap.addMarker(destination);

                new FetchURL(MainActivity.this).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(origin.getPosition())
                        .zoom(13)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5500, null);
                ZoomMarkerIn(to,from);

                UpdateJob(BookingID, 1);
                bottomSheetDialog.dismiss();
             /*   set_driver_status("OnJob");
                btn_bottom_sheet.setVisibility(View.GONE);
                btn_begin_Ride.setVisibility(View.VISIBLE);
                //btn_bottom_sheet.setText("1 Job Arrived");
                btn_Accept_Ride.setVisibility(View.INVISIBLE);
                btn_Reject_Ride.setVisibility(View.INVISIBLE);
                JobinProgress = false;*/
               resetJobSheet();
            }
        });


        // bottomSheet = findViewById(R.id.bottom_sheet);
        //  sheetBehavior = BottomSheetBehavior.from(bottomSheet);


        // click event for show-dismiss bottom sheet
      /*  btn_bottom_sheet.setOnClickListener(new View.OnClickListener() {
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
        });*/
// callback for do something
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        btn_bottom_sheet.setText("Close Job");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        btn_bottom_sheet.setText("Waiting for Job");
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




        btn_End_Ride = findViewById(R.id.btn_Ride_end);
        btn_End_Ride.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                UpdateJob(BookingID,2);
                    resetJobSheet();
                JobinProgress = false;
                stopcount = 0;
                btn_via.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                loadridesummary(Notify_data, getSummaryLayout);
                summarySheetDlg.show();
            }
        });



        mySwitch = findViewById(R.id.action_status);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked) {
                    mySwitch.setText("Online");
                    set_driver_status("ONLINE");

                }
                else {
                    mySwitch.setText("Offline");
                    set_driver_status("Offline");

                }
            }
        });


        btn_drv_arrived = findViewById(R.id.btn_drv_arrived);
        btn_drv_arrived.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                set_driver_status("ARRIVED WAITING");
                btn_End_Ride.setVisibility(View.VISIBLE);
                btn_POB.setVisibility(View.VISIBLE);
                btn_drv_arrived.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });

        btn_via = findViewById(R.id.btn_drv_Via);
        btn_POB = findViewById(R.id.btn_POB);

        btn_POB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                set_driver_status("POB");
                if(Notify_data.viaLatLngArray.size() > 0)
                {
                    MarkerOptions stops =  new MarkerOptions().position(Notify_data.viaLatLngArray.get(0)).title("Stop Over");
                    DriverGuider =  stops;
                    stopcount = 1;
                    btn_via.setVisibility(View.VISIBLE);
                    btn_End_Ride.setVisibility(View.VISIBLE);
                    btn_POB.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                }
                else {
                    DriverGuider = destination;
                    btn_End_Ride.setVisibility(View.VISIBLE);
                    btn_POB.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                }


            }
        });

        btn_via.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                int no_of_stops = Notify_data.viaLatLngArray.size();
                if( no_of_stops> 0) {
                    if (no_of_stops > stopcount) {
                        MarkerOptions stops = new MarkerOptions().position(Notify_data.viaLatLngArray.get(stopcount)).title("Stop Over");
                        Resources res = getResources();
                        stopcount ++;
                        String title = res.getString(R.string.stop_info, stopcount);// String.format("%2$d Stop Arrived", stopcount);
                        btn_via.setText(title);

                        DriverGuider = stops;
                    }
                    else
                    {
                        DriverGuider = destination;
                        btn_End_Ride.setVisibility(View.VISIBLE);
                        btn_via.setVisibility(View.GONE);
                        btn_POB.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }
                }

              /*  set_driver_status("POB");
                DriverGuider = destination;
                btn_End_Ride.setVisibility(View.VISIBLE);
                btn_POB.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);*/


            }
        });



        btn_no_show = findViewById(R.id.btn_NOSHOW);
        btn_no_show.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

               resetJobSheet();
                JobinProgress = false;
                fab.setVisibility(View.GONE);

            }
        });



        btn_begin_Ride = findViewById(R.id.button2);
        btn_begin_Ride.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                stopcount = 0;
                draw_route(mCurrLocationMarker.getPosition(),DriverGuider.getPosition(),17);
                set_driver_status("OnTheWay");
                btn_End_Ride.setVisibility(View.VISIBLE);
                btn_drv_arrived.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                JobinProgress = true;
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
         */
        btn_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                bottomSheetDialog.show();



            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("Booking_IDReciver"));


     //   fetchDriver(DriverID);




    }

    public void closesheet()
    {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
           return;
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
        if(bottomSheetDialog!=null)
        bottomSheetDialog.dismiss();
    }

public void loadprofilepic(){



        try {


           JSONObject drv = getM_pDriver();
            if (drv != null) {
                 final String imageString = drv.getString("photo");
               /* byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                mDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodedByte);
                mDrawable.setCircular(true);
                mDrawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.DST_OVER);

                mIcon.setImageDrawable(mDrawable);*/
            }

        }catch (JSONException e) {
                //progress.dismiss();
                e.printStackTrace();
            }
}




    public void sendNotification(JSONObject notification) {


      /*  String NOTIFICATION_TITLE;
        String NOTIFICATION_MESSAGE;
        String TOPIC;


        TOPIC = "/topics/android1"; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = "Driver Location update";
        NOTIFICATION_MESSAGE = "My Location 51.0087,39.0001";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG1, "onCreate: " + e.getMessage() );
        }*/
       // sendNotification(notification);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constant.FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(Constant.TAG1, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(Constant.TAG1, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", Constant.serverKey);
                params.put("Content-Type", Constant.contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void send_fcm_msg(String Topic, LatLng Location , String Status, String JustUpdate)
    {

        JSONObject body = new JSONObject();
        try {
            body.put("to", "/topics/" + Topic);
            body.put("priority", "high");
            //body.put("android_channel_id", "pickmecab_updates");


            JSONObject notification = new JSONObject();
            notification.put("body", "Driver location update request");
            notification.put("title", "Driver location updated");

            body.put("notification", notification);
            JSONObject data = new JSONObject();
            data.put("title", "driver location changed");
            data.put("body", "driver location updated");
            //  data.put("message", "New Job");
            // data.put("click_action","new_ride");
            data.put("drvid", DriverID);
            data.put("lat", (Location.latitude));
            data.put("lng", (Location.longitude));
            data.put("drvStatus", Status);
            data.put("UpdateJob",JustUpdate);


            body.put("data", data);
        }
        catch (JSONException e) {
            Log.e(Constant.TAG1, "onCreate: " + e.getMessage() );
        }
        sendNotification(body);

/*

        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder("DriverAPP" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(123))
                .addData("priority", "high")
                .addData("location", "55.006,1.00")
                .addData("driverID", DriverID)
                .addData("DriverStatus", DriverStatus)
                .addData("to","/topics/android1")
                .build());
*/

    //    String response = FirebaseMessaging.getInstance().send(message);
// Response is a message ID string.
      //  System.out.println("Successfully sent message: " + response);

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if (JobinProgress) {
                Toast.makeText(context, "New job can't be started a job is already going on.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                BookingID = intent.getStringExtra("BookingID");
                int updatetype = intent.getIntExtra ("UpdateJob",1);

                if(updatetype == 3) {
                    fatchJob(BookingID,1);
                    To_HomeFragment();
                }

                if(updatetype == 2) {
                    fatchJob(BookingID,0);
                    To_HomeFragment();
                }
                else
                   UpdateJob(BookingID,updatetype);
                // Bundle b = intent.getBundleExtra("Location");

                //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    };
public void To_HomeFragment()
    {

        navController.navigate(R.id.nav_home);

    }

    void StartNow()
    {
        CharSequence text = "Ride Accepted!";
        int duration = Toast.LENGTH_SHORT;



        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();


        LatLng from = new LatLng(Notify_data.getLat_fr(), Notify_data.getLang_fr());
        LatLng to = new LatLng(Notify_data.getLat_to(), Notify_data.getLang_to());

        origin = new MarkerOptions().position(from).title("Pick Up");
        destination = new MarkerOptions().position(to).title("Drop Off");
        DriverGuider = origin;

        PointA =  mMap.addMarker(origin);
        PointB = mMap.addMarker(destination);

        new FetchURL(MainActivity.this).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");

        CameraPosition googlePlex = CameraPosition.builder()
                .target(origin.getPosition())
                .zoom(13)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5500, null);
        ZoomMarkerIn(to,from);

        UpdateJob(BookingID, 1);
        btn_bottom_sheet.setVisibility(View.GONE);
        btn_begin_Ride.setVisibility(View.VISIBLE);
             /*   set_driver_status("OnJob");
                btn_bottom_sheet.setVisibility(View.GONE);
                btn_begin_Ride.setVisibility(View.VISIBLE);
                //btn_bottom_sheet.setText("1 Job Arrived");
                btn_Accept_Ride.setVisibility(View.INVISIBLE);
                btn_Reject_Ride.setVisibility(View.INVISIBLE);
                JobinProgress = false;*/
    }

void ZoomMarkerIn(LatLng One, LatLng Two)
{

    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    /*for (Marker marker : markers) {
        builder.include(marker.getPosition());
    }*/

    builder.include(One);
    builder.include(Two);
    LatLngBounds bounds = builder.build();

    int padding = 50; // offset from edges of the map in pixels
    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

    mMap.animateCamera(cu);
}
    void draw_route(LatLng LLA, LatLng LLB, int zoom)
    {
        new FetchURL(MainActivity.this).execute(getUrl( LLA, LLB, "driving"), "driving");

        CameraPosition googlePlex = CameraPosition.builder()
                .target(LLA)
                .zoom(zoom)
                .bearing(0)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5500, null);


        btn_bottom_sheet.setVisibility(View.GONE);
        btn_begin_Ride.setVisibility(View.GONE);
        btn_End_Ride.setVisibility(View.VISIBLE);
    }

public void getDrvStatus(final String status)
{


    String userName = "";
    String passWord ="";
    String userDbId = "";

   // sh = getSharedPreferences("myprefe", 0);
   // editor = sh.edit();

    // check here if user is login or not
    str_login_test = SplashActivity.sh.getString("loginTest", null);

    if (str_login_test != null
            && !str_login_test.toString().trim().equals("")) {

        userName = SplashActivity.sh.getString("UserID",null);
        passWord = SplashActivity.sh.getString("UserPwd",null);
        userDbId = SplashActivity.sh.getString("Driver_db_id",null);


    }

    JSONObject jsonObject= new JSONObject();
    try {
        jsonObject.put("id", userDbId);
        jsonObject.put("loginId", userName);
        jsonObject.put("password", passWord);


    } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();

    }

    //http://localhost:8090/

    String URL = Constant.WEB_API_PATH+"drivers/"+userDbId;


    // Initialize a new RequestQueue instance
    RequestQueue requestQueue = Volley.newRequestQueue(this);

    // Initialize a new JsonObjectRequest instance


    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET,
            URL,
            null,
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
                        {
                            JSONObject resObject= new JSONObject();
                            resObject = response.getJSONObject("data");
                            SetDrvStatus(resObject,status);

                        }

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

                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Do something when error occurred

                    Log.e("Error", "Error at sign in : " + error.getMessage());

                }
            }
    );

    // Add JsonObjectRequest to the RequestQueue
    requestQueue.add(jsonObjectRequest);


}


    public void SetDrvStatus(JSONObject sStatus, String strStatus){






        try {

            sStatus.put ("status", strStatus);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonObjectRequest instance


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                sStatus,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());


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

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred

                        Log.e("Error", "Error at sign in : " + error.getMessage());

                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);


    }





  /*  public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }*/

  private boolean isLocationEnabled(Context context) {
      LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

      return LocationManagerCompat.isLocationEnabled(locationManager);
  }






    @Override
    protected void onResume() {
        super.onResume();
        if(isLocationEnabled(this)) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else
        {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.Cancel,null)
                    .show();
        }
        if (JobinProgress) {
            Toast.makeText(getApplicationContext(), "New job can't be started a job is already going on.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Bundle b = getIntent().getExtras();
            BookingID = ""; // or other values
            if (b != null) {
                BookingID = b.getString("BookingID");
                if(currentBookingID.equals(BookingID)) {
                  // do nothing
                }
                else {
                    currentBookingID = BookingID;
                    fatchJob(BookingID, 0);
                }
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFusedLocationClient != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
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

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();

                    mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                            .flat(true)
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.the_car))
                            .anchor(0.5f, 0.5f)
                            .position(latLng));
                }



              /*   */

//Place current location marker

                if (  mMap != null) {
                    if(btn_begin_Ride.getVisibility()!=View.VISIBLE)
                        animateCamera(location);
                    firstTimeFlag = false;
                }
                showMarker(location);

               animateMarker(mCurrLocationMarker, location,start_rotation); // Helper method for smooth
               //CameraPosition currentposition=mMap.getCameraPosition();






            // mCurrLocationMarker.setRotation(location.bearing);
                //move map camera

              // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                if (btn_End_Ride.getVisibility() == View.VISIBLE) {

                    draw_route(latLng, DriverGuider.getPosition(),17);
                }
                else {
                    if (currentPolyline != null)
                        currentPolyline.remove();
                }


            }
        }
    };

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));
    }

    private void showMarker(@NonNull Location currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (mCurrLocationMarker == null)
            mCurrLocationMarker =  mMap.addMarker(new MarkerOptions()
                    .flat(true)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.the_car))
                    .anchor(0.5f, 0.5f)
                    .position(latLng));
        else
            MarkerAnimation.animateMarkerToGB(mCurrLocationMarker, latLng, new LatLngInterpolator.Spherical());
    }
    public void animateMarker(final Marker marker, final Location location, final float st) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = st;//marker.getRotation();
        final long duration = 2000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

              //  marker.setPosition(new LatLng(lat, lng));


                marker.setRotation(-rotation > 180 ? rotation / 2 : rotation);
                start_rotation = -rotation > 180 ? rotation / 2 : rotation;

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
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

       /* //Use custom menu instead
        MenuInflater inflater = getMenuInflater();
        //Inflate the custom menu
        inflater.inflate(R.menu.custommenu, menu);
        //reference to the item of the menu
        MenuItem mitem=menu.findItem(R.id.item1);
        Spinner spin =(Spinner) mitem.getActionView();
        setupSpinner(spin);*/

        this.item = menu;
        return true;


    }

public void set_driver_status(String  status)
{
    item.findItem(R.id.text_status).setTitle(status);
    if(status.equals("ONLINE")) status = "ACTIVE";
    getDrvStatus(status);
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.text_status:
                // User chose the "Settings" item, show the app settings UI...


            return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(true);


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000); // two minute interval
        mLocationRequest.setFastestInterval(10000);
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
        progressDialog.setMessage("Job Loading");
        progressDialog.setTitle("Loading Job please wait..");
        progressDialog.setIcon(R.drawable.user);
        //To show the dialog
        progressDialog.show();
        progressDialog.setCancelable(false);

        //To dismiss the dialog
//        progressDialog.dismiss();

    }

    private void closeProgressDialog() {
        progressDialog.dismiss();
    }

    public void UpdateDriverIDonJob(JSONObject data, String Drv_ID)
    {
        try {

            data.getJSONObject("driver").put("id", Drv_ID);
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
        String URL = Constant.WEB_API_PATH+"bookings/";


        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonObjectRequest instance


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());


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

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred

                        Log.e("Error", "Error at sign in : " + error.getMessage());

                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }

    public void UpdateJob(String Booking_ID, final int Accpet_reject) {


        //http://localhost:8090/

        String URL;

        switch(Accpet_reject)
        {
            case 1:
                URL = Constant.WEB_API_PATH+"bookings/accept/"+Booking_ID;
                break;
            case 2:
                URL  = Constant.WEB_API_PATH+"bookings/complete/"+Booking_ID;
                break;
            default:
                URL = Constant.WEB_API_PATH+"bookings/reject/"+Booking_ID;
                  break;

        }



        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonObjectRequest instance


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());


                        // Process the JSON
                       try {
                            // Get the JSON array
                            JSONObject resObject= new JSONObject();
                            resObject = response.getJSONObject("data");

                            if(Accpet_reject==1) {
                                String status = resObject.getString("status");
                                if ( status == "BOOKED" ||status == "CANCELLED" || status == "DELETED") {

                                    Toast.makeText(getApplicationContext(), "Oops too late. This job is already expired.", Toast.LENGTH_SHORT).show();
                                    resetJobSheet();

                                }
                            }

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

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred

                        Log.e("Error", "Error at sign in : " + error.getMessage());

                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);




}
    public void fatchJob(String Booking_ID, final int iStartNow)
    {
        String sURL = Constant.WEB_API_PATH+"bookings/" + Booking_ID;

        openProgressDialog();
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
                                Notify_data.setM_From(userData.getJSONObject("pickupAddress").getString("street"));
                                Notify_data.setNoppl(userData.getString("numberOfPassenger"));
                                Notify_data.setNobags (userData.getString("vi"));
                                Notify_data.setInst(userData.getString("instructions"));
                                Notify_data.setMiles (userData.getString("site"));
                                Notify_data.setPrice(userData.getString("price"));
                                Notify_data.setFlightNo(userData.getString("flightNo"));

                                Notify_data.setBookingNo(userData.getString("prefixID"));
                               // Notify_data.setPrice ("Not Allowed");
                                Notify_data.setWarning_timer ("No Timer");
                                Notify_data.setStart_time(userData.getString("startTime"));


                                JSONArray viaAddres = userData.getJSONArray("viaAddress");

                                if(viaAddres !=null)
                                {
                                    String street = "";

                                    for (int i=0; i<viaAddres.length(); i++)
                                    {
                                        JSONObject viaObj = viaAddres.getJSONObject(i);
                                        street = viaObj.getString("street");

                                        Notify_data.viaAddress.add(street);
                                        String lat = viaObj.getString("latitude");
                                        String lng = viaObj.getString("longitude");

                                            if(lat != null && !lat.equals("null") && lng != null && !lng.equals("null")) {
                                                LatLng vialatlang = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                                                Notify_data.viaLatLngArray.add(vialatlang);
                                            }
                                            else
                                            {
                                                closeProgressDialog();
                                                AlertError();
                                                return;
                                            }


                                    }
                                }





                                String value = userData.getJSONObject("pickupAddress").getString("latitude");
                                if(value != null && !value.equals("null"))
                                Notify_data.setLat_fr(Double.parseDouble(value));
                                else
                                {
                                    closeProgressDialog();
                                    AlertError();
                                    return;
                                }
                                value = userData.getJSONObject("pickupAddress").getString("longitude");
                                if(value != null && !value.equals("null"))
                                Notify_data.setLang_fr(Double.parseDouble(value));
                                else
                                {
                                    closeProgressDialog();
                                    AlertError();
                                    return;
                                }
                                value = userData.getJSONObject("destinationAddress").getString("latitude");
                                if(value != null && !value.equals("null"))
                                Notify_data.setLat_to(Double.parseDouble(value));
                                else
                                {
                                    closeProgressDialog();
                                    AlertError();
                                    return;
                                }
                                value = userData.getJSONObject("destinationAddress").getString("longitude");
                                if(value != null && !value.equals("null"))
                                    Notify_data.setLang_to(Double.parseDouble(value));
                                else
                                {
                                    closeProgressDialog();
                                    AlertError();
                                    return;
                                }
                                CalculationByDistance( new LatLng(Notify_data.getLat_fr(),Notify_data.getLang_fr()),new LatLng(Notify_data.getLat_to(),Notify_data.getLang_to()));


                                if(iStartNow == 1)
                                {
                                    StartNow();
                                    closeProgressDialog();
                                }
                                else {
                                    sendMessage();
                                    closeProgressDialog();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            closeProgressDialog();
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
                        closeProgressDialog();

                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }

    public void AlertError()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Invalid job data");
        builder.setMessage("This job details are invalid. Job can't be loaded please contact the base !");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "Job was not loaded",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        //setting the view of the builder to our custom view that we already inflated
       // builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
/*
        new AlertDialog.Builder(this)
                .setTitle("Invalid job data")
                .setMessage("This job details are invalid. Job can't be loaded please contact base !")
                .setPositiveButton(android.R.string.ok, null)
                .show();*/
    }




    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        /*

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

        str = kmInDec+" KM " + meterInDec + " Meters";
        Notify_data.setM_duration(str);

        return Radius * c;

         */



        origin = new MarkerOptions().position(StartP).title("Pick Up");
        destination = new MarkerOptions().position(EndP).title("Drop Off");

        new FetchURL(MainActivity.this).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");

return 0;
    }


    public void sendMessage() {

        Context context = getApplicationContext();
        CharSequence text = "New Job Arrived!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


        update_bottom_shet(Notify_data, bottom_sheet);
        //closeProgressDialog();

        update_bottom_dlg(Notify_data, getSheetLayout);


    }

    public void loadNavigationView(MarkerOptions dest){
        Uri navigation = Uri.parse("google.navigation:q="+dest.getPosition().latitude+","+dest.getPosition().longitude+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        navigationIntent.setPackage("com.google.android.apps.maps");
        startActivity(navigationIntent);
    }

    public void resetJobSheet()
    {
        TextView mytext =  getSheetLayout.findViewById(R.id.text_cname);
        mytext.setText("Waiting...");
        mytext = getSheetLayout.findViewById(R.id.text_title);
        mytext.setText("Waiting for Job ...");
        mytext = getSheetLayout.findViewById(R.id.text_from);
        mytext.setText("");
        mytext = getSheetLayout.findViewById(R.id.text_to);
        mytext.setText("");
        mytext = getSheetLayout.findViewById(R.id.text_location);
        mytext.setText("Waiting for job location ...");
        mytext = getSheetLayout.findViewById(R.id.text_cphone);
        mytext.setText("Waiting... ");
        mytext = getSheetLayout.findViewById(R.id.text_duration);
        mytext.setText("Waiting...");

        mytext = getSheetLayout.findViewById(R.id.text_time);
        mytext.setText("0:00");
        mytext = getSheetLayout.findViewById(R.id.text_ppl);
        mytext.setText("1");

        mytext = getSheetLayout.findViewById(R.id.text_bags);
        mytext.setText("0");


        mytext = getSheetLayout.findViewById(R.id.text_inst);
        mytext.setText("NA");

        mytext = getSheetLayout.findViewById(R.id.text_miles);
        mytext.setText("0");

        mytext = getSheetLayout.findViewById(R.id.text_flightNo);
        mytext.setText("");

        mytext = getSheetLayout.findViewById(R.id.text_cost);
        mytext.setText("0.00£");


        btn_bottom_sheet.setText("Waiting Job");



        btn_Accept_Ride.setVisibility(View.GONE);
        btn_Reject_Ride.setVisibility(View.GONE);
        btn_begin_Ride.setVisibility(View.GONE);
        btn_bottom_sheet.setVisibility(View.VISIBLE);
        btn_drv_arrived.setVisibility(View.GONE);
        btn_no_show.setVisibility(View.GONE);
        btn_POB.setVisibility(View.GONE);
        btn_End_Ride.setVisibility(View.INVISIBLE);
        btn_End_Ride.setVisibility(View.GONE);
        btn_close_dlg.setVisibility(View.VISIBLE);
        set_driver_status("ONLINE");
        clearMapp();
        if(timer !=null) {
            timer.cancel();
            TextView my_text = getSheetLayout.findViewById(R.id.text_timer);
            my_text.setText("00:00");
        }
        btn_bottom_sheet.clearAnimation();
        btn_bottom_sheet.setBackgroundColor(R.color.black);

        bottomSheetDialog.dismiss();
    }

    public void loadridesummary(NotificationDataParser _data, View bSheet)
    {
        TextView mytext = bSheet.findViewById(R.id.text_from);
        mytext.setText(_data.getM_From());
        mytext = bSheet.findViewById(R.id.text_to);
        mytext.setText(_data.getM_to());

        mytext = bSheet.findViewById(R.id.text_ppl);
        mytext.setText(Notify_data.getNoppl());

        mytext = bSheet.findViewById(R.id.text_bags);
        mytext.setText(Notify_data.getNobags());


        mytext = bSheet.findViewById(R.id.text_inst);
        mytext.setText(Notify_data.getInst());

        mytext = bSheet.findViewById(R.id.text_miles);
        mytext.setText(Notify_data.getMiles());

        mytext = bSheet.findViewById(R.id.text_flightNo);
        mytext.setText(Notify_data.getFlightNo());
        mytext = bSheet.findViewById(R.id.text_cost);
        mytext.setText(Notify_data.getPrice()+"£");


    }
    public void update_bottom_dlg(NotificationDataParser _data, View bSheet) {
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
        mytext.setText(_data.getStart_time());
        mytext = bSheet.findViewById(R.id.text_ppl);
        mytext.setText(Notify_data.getNoppl());

        mytext = bSheet.findViewById(R.id.text_bags);
        mytext.setText(Notify_data.getNobags());


        mytext = bSheet.findViewById(R.id.text_inst);
        mytext.setText(Notify_data.getInst());

        mytext = bSheet.findViewById(R.id.text_miles);
        mytext.setText(Notify_data.getMiles());

        mytext = bSheet.findViewById(R.id.text_flightNo);
        mytext.setText(Notify_data.getFlightNo());

        mytext = bSheet.findViewById(R.id.text_cost);
        mytext.setText(Notify_data.getPrice()+"£");

        TextView my_text = bSheet.findViewById(R.id.text_timer);
         timer = new CountDown(30000, 1000,btn_bottom_sheet,this );
        timer.start();
        animate_button(btn_bottom_sheet);
        btn_bottom_sheet.setText("+ New Job Arrived");
        btn_Accept_Ride.setVisibility(View.VISIBLE);
        btn_Reject_Ride.setVisibility(View.VISIBLE);
        btn_close_dlg.setVisibility(View.GONE);




    }


    public  void animate_button(Button btnani)
    {
       /* final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(btnani,
                "backgroundColor",
                new ArgbEvaluator(),
                0xFFFFFFFF,
                0xff78c5f9);
        backgroundColorAnimator.setDuration(60000);
        backgroundColorAnimator.start();*/

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        btnani.startAnimation(anim);

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
        mytext.setText(_data.getStart_time());
        mytext = bSheet.findViewById(R.id.text_ppl);
        mytext.setText(Notify_data.getNoppl());

        mytext = bSheet.findViewById(R.id.text_bags);
        mytext.setText(Notify_data.getNobags());


        mytext = bSheet.findViewById(R.id.text_inst);
        mytext.setText(Notify_data.getInst());

        mytext = bSheet.findViewById(R.id.text_miles);
        mytext.setText(Notify_data.getMiles());



        btn_bottom_sheet.setText("+ New Job Arrived");
        btn_Accept_Ride.setVisibility(View.VISIBLE);
        btn_Reject_Ride.setVisibility(View.VISIBLE);




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

        String duration = (String)values[2];
        String time = (String)values[1];

        TextView mytext = bottom_sheet.findViewById(R.id.text_duration);
        //mytext = bottom_sheet.findViewById(R.id.text_duration);
        mytext.setText(duration);

        mytext = bottom_sheet.findViewById(R.id.text_time);
        mytext.setText(time);

        String text = "Total " + duration + "Time To Destination: " + time;
        Snackbar.make(findViewById(R.id.overview_coordinator_layout), text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


      //  animateCarMove(mCurrLocationMarker, currentPolyline.getPoints().get(0), currentPolyline.getPoints().get(1), 10000);
    }

public void clearMapp()
{
    if(currentPolyline != null)
    currentPolyline.remove();
    if (PointA != null ) {
        PointA.remove();
    }
    if(PointB !=null)
    PointB.remove();
}
    private void animateCarMove(final Marker marker, final LatLng beginLatLng, final LatLng endLatLng, final long duration) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();

        final Interpolator interpolator = new LinearInterpolator();

        // set car bearing for current part of path
        float angleDeg = (float)(180 * getAngle(beginLatLng, endLatLng) / Math.PI);
        Matrix matrix = new Matrix();
        matrix.postRotate(angleDeg);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));

        handler.post(new Runnable() {
            @Override
            public void run() {
                // calculate phase of animation
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = ((LinearInterpolator) interpolator).getInterpolation((float) elapsed / duration);
                // calculate new position for marker
                double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * t + beginLatLng.longitude;

                marker.setPosition(new LatLng(lat, lng));

                // if not end of line segment of path
                if (t < 1.0) {
                    // call next marker position
                    handler.postDelayed(this, 16);
                } else {
                    // call turn animation
                    nextTurnAnimation();
                }
            }
        });
    }

    private void nextTurnAnimation() {
        mIndexCurrentPoint++;

        if (mIndexCurrentPoint < mPathPolygonPoints.size() - 1) {
            LatLng prevLatLng = mPathPolygonPoints.get(mIndexCurrentPoint - 1);
            LatLng currLatLng = mPathPolygonPoints.get(mIndexCurrentPoint);
            LatLng nextLatLng = mPathPolygonPoints.get(mIndexCurrentPoint + 1);

            float beginAngle = (float)(180 * getAngle(prevLatLng, currLatLng) / Math.PI);
            float endAngle = (float)(180 * getAngle(currLatLng, nextLatLng) / Math.PI);

            animateCarTurn(mCurrLocationMarker, beginAngle, endAngle, 10000);
        }
    }

    private void animateCarTurn(final Marker marker, final float startAngle, final float endAngle, final long duration) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final Interpolator interpolator = new LinearInterpolator();

        final float dAndgle = endAngle - startAngle;

        Matrix matrix = new Matrix();
        matrix.postRotate(startAngle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(rotatedBitmap));

        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = ((LinearInterpolator) interpolator).getInterpolation((float) elapsed / duration);

                Matrix m = new Matrix();
                m.postRotate(startAngle + dAndgle * t);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), m, true)));

                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    nextMoveAnimation();
                }
            }
        });
    }

    private void nextMoveAnimation() {
        if (mIndexCurrentPoint <  mPathPolygonPoints.size() - 1) {
            animateCarMove(mCurrLocationMarker, mPathPolygonPoints.get(mIndexCurrentPoint), mPathPolygonPoints.get(mIndexCurrentPoint+1), 10000);
        }
    }

    private double getAngle(LatLng beginLatLng, LatLng endLatLng) {
        double f1 = Math.PI * beginLatLng.latitude / 180;
        double f2 = Math.PI * endLatLng.latitude / 180;
        double dl = Math.PI * (endLatLng.longitude - beginLatLng.longitude) / 180;
        return Math.atan2(Math.sin(dl) * Math.cos(f2) , Math.cos(f1) * Math.sin(f2) - Math.sin(f1) * Math.cos(f2) * Math.cos(dl));
    }
}
