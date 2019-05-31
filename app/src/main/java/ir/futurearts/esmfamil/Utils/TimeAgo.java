package ir.futurearts.esmfamil.Utils;

import android.content.Context;
import android.content.res.Resources;

import java.util.Calendar;
import java.util.Date;

public class TimeAgo {
    protected Context context;

    public TimeAgo(Context context) {
        this.context = context;
    }

    public String timeAgo(Date date) {
        return timeAgo(date.getTime());
    }

    public String timeAgo(long millis) {
        long diff = new Date().getTime() - millis;

        Resources r = context.getResources();


        double seconds = Math.abs(diff) / 1000.0;
        double minutes = seconds / 60;
        double hours = minutes / 60;
        double days = hours / 24;
        double years = days / 365;

        String words;

        if (seconds < 45) {
            words = "لحظاتی پیش";
        } else if (seconds < 90) {
            words = "دقایقی پیش";
        } else if (minutes < 45) {
            words ="دقایقی پیش";
        } else if (minutes < 90) {
            words = "ساعاتی پیش";
        } else if (hours < 24) {
            words =  "ساعاتی پیش";
        } else if (hours < 48) {
            words = "دیروز";
        }
        else if(days<14){
            words= "هفته پیش";
        }else if (days < 30) {
            words =  "چند هفته پیش";
        }else{
            words = "خیلی وقت پیش";
        }

        return words;
    }
}
