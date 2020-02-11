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
    public Button yes, no;
    ListItem Job;

    public CustomDialog(Activity a, ListItem jobdata) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        Job = jobdata;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.Btn_YES);
        no = (Button) findViewById(R.id.Btn_NO);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        TextView mytext = findViewById(R.id.txt_pickup);
        mytext.setText(Job.getLocation());
        mytext = findViewById(R.id.txt_from);
        mytext.setText(Job.getName());
        mytext = findViewById(R.id.txt_to);
        mytext.setText(Job.getDesignation());

    }

    private void sendMessageToActivity(String Booking_ID) {
        Intent intent = new Intent("Booking_IDReciver");
        // You can also include some extra data.
        intent.putExtra("BookingID", Booking_ID);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Btn_YES:
                sendMessageToActivity(Job.getBookingID());

                dismiss();
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