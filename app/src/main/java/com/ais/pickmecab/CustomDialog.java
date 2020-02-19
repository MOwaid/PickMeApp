package com.ais.pickmecab;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class CustomDialog  extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no, reject;
    ListItem Job;
    int dialogtype;

    public CustomDialog(Activity a, ListItem jobdata, int type) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        Job = jobdata;
        dialogtype = type;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.Btn_YES);
        no = (Button) findViewById(R.id.Btn_NO);
        reject = (Button) findViewById(R.id.Btn_Reject);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        reject.setOnClickListener(this);

        if (dialogtype == 1 )
        {
            yes.setText("Accept");
            no.setText("Cancel");
            reject.setVisibility(View.VISIBLE);
            TextView newmsg = findViewById(R.id.dlg_msg);
            newmsg.setText("Do you want to accept this Ride?");
        }
        TextView mytext = findViewById(R.id.txt_pickup);
        mytext.setText(Job.getLocation());
        mytext = findViewById(R.id.txt_from);
        mytext.setText(Job.getName());
        mytext = findViewById(R.id.txt_to);
        mytext.setText(Job.getDesignation());

    }

    private void sendMessageToActivity(String Booking_ID, int update) {
        Intent intent = new Intent("Booking_IDReciver");
        // You can also include some extra data.

        intent.putExtra("BookingID", Booking_ID);
        intent.putExtra("UpdateJob",update);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Btn_YES:
                if(dialogtype==0)
                sendMessageToActivity(Job.getBookingID(),3);  // we want to call jobfatch
                else
                    sendMessageToActivity(Job.getBookingID(),1); // we want to call accpet job update status

                dismiss();
                break;

            case R.id.Btn_Reject:
                sendMessageToActivity(Job.getBookingID(),0); // we want to call reject job

                break;
            case R.id.Btn_NO:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }



}