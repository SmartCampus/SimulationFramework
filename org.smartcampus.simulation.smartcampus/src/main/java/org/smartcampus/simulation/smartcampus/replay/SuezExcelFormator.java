package org.smartcampus.simulation.smartcampus.replay;

import org.smartcampus.simulation.stdlib.replay.ExcelFormator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by foerster
 * on 05/02/14.
 */
public class SuezExcelFormator extends ExcelFormator {

    private final SimpleDateFormat sdf;
    private final int numberOfMilliSecondsInADay;

    public SuezExcelFormator() {
        super(2, new String[]{"A", "B"}, 2);

        sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        numberOfMilliSecondsInADay = 86400000;
    }

    @Override
    protected long transform(String[] columns) {
        long timestamp = 0;
        try {
            Date d =  sdf.parse(columns[0]);
            d.setHours(0);
            System.out.println(d);
            timestamp = d.getTime();
            System.out.println(timestamp);
            int hoursToMilli = Integer.valueOf(columns[1]) * 3600000;
            timestamp += hoursToMilli;
            System.out.println(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }
}
