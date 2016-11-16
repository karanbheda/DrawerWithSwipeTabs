package com.androidbelieve.drawerwithswipetabs;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Notification {
    private String notification;
    private String nid;
    private String yes,no,maybe,type,date,time;
    public Notification() {
    }
static String Month(Date d)
{
    int i=d.getMonth();
    switch(i)
    {
        case 0: return "Jan";
        case 1: return "Feb";
        case 2: return "Mar";
        case 3: return "Apr";
        case 4: return "May";
        case 5: return "Jun";
        case 6: return "July";
        case 7: return "Aug";
        case 8: return "Sept";
        case 9: return "Oct";
        case 10: return "Nov";
        case 11: return "Dec";
        default:return "Dec";
    }

}
    public Notification(String notification,String yes,String no, String maybe,String type,String nid) {
        this.notification = notification;
        this.yes = yes;
        this.maybe = maybe;
        this.no = no;
        this.type=type;
        this.nid=nid;
    }

    public Notification(String notification,String yes,String no, String maybe,String type,String nid,String timestamp) throws ParseException {
        this.notification = notification;
        this.yes = yes;
        this.maybe = maybe;
        this.no = no;
        this.type=type;
        this.nid=nid;
        Date today=new Date();
        Date date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
        Log.v("timeStamp",timestamp);
        Log.v("date",date.toString());
        if(today.getDay()==date.getDay())
            this.date="Today ";
        else if(today.getDay()==date.getDay()+1)
            this.date="Yesterday ";
        else
        {

            this.date=date.getDay()+" "+Month(date)+" ";
            if(!(today.getYear()==date.getYear()))
                this.date+=date.getYear()+" ";
        }

        this.time=new SimpleDateFormat("HH:mm").format(date);
    }
    public String getNid()
    {
        return nid;
    }
    public String type()
    {
        return type;
    }
    public String getNotification() {
        return notification;
    }
    public boolean getType()
    {
        if(type.equals("BUTTON"))
            return true;
        else
            return false;

    }


    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getYes() {
        return yes;
    }
    public void setType(String s) {
        this.type=s;
        return;
    }
    public void setYes(String yes) {
        this.yes = yes;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String price) {
        this.no = no;
    }

    public String getMaybe() {
        return maybe;
    }

    public void setMaybe(String maybe) {
        this.maybe = maybe;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}