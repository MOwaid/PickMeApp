package com.ais.pickmecab;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListItem {
    private String name;
    private String designation;
    private String location;
    private String start_time;
    private String BookingID;

    public String getStart()
    {
        return start_time;
    }

    public String getStart_time() {
        if(start_time != null)
        {
            String string = start_time;
            if (start_time.contains("T")) {
                // Split it.

            String[] sDate = string.split("T");
            String part1 = sDate[0];
            String part2 = sDate[1];
            start_time ="Date: "+ part1;
                    if(part2.contains(".")) {
                        String[] sTime;
                        sTime = part2.split("\\.");
                        String starttime = sTime[0];
                         start_time= start_time +" Start at: "+starttime;
                         return  start_time;
                    }

                }

        }

        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }





}

