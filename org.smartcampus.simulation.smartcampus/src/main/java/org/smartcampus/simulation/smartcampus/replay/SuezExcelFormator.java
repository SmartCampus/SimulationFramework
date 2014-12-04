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

    public SuezExcelFormator() {
        super(2, new String[]{"A", "B"}, 2);

        sdf = new SimpleDateFormat("MM/dd/yyyy h");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    protected long transform(String[] columns) {
        long timestamp = 0;
        try {
            Date d = sdf.parse(columns[0] + " " + Integer.valueOf(columns[1]));
            timestamp = d.getTime();
            long offset = TimeZone.getDefault().getOffset(timestamp);
            timestamp += offset;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}