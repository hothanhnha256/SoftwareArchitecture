package com.example.staff_service.Utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date[] getStartAndEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // Start of day (00:00:00.000)
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date start = cal.getTime();

        // End of day (00:00:00 of next day)
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date end = cal.getTime();

        return new Date[] { start, end };
    }
}
