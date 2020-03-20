package com.ais.pickmecab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;




public class ListItemComparator implements Comparator<ListItem>
{
    @Override
    public int compare(ListItem left, ListItem right) {


        Date strDate = null;
        try {

            Date mydate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
         strDate = sdf.parse(left.getStart());

            mydate = sdf.parse(right.getStart());
            /* For Ascending order*/
            return strDate.compareTo(mydate);

        } catch (ParseException e) {
            e.printStackTrace();
        }




return 0;



    }
}