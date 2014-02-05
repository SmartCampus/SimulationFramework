package org.smartcampus.simulation.stdlib.replay;

import org.smartcampus.simulation.framework.simulator.FileFormator;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by foerster
 * on 04/02/14.
 */
public abstract class ExcelFormator extends FileFormator {

    private Map<String,Scanner> filesScanners;
    private Scanner timestampScanner;
    private String[] timestampColumns;
    private int sheetNumber;
    private String lastTimestamp;

    public ExcelFormator(){
        super();
        sheetNumber = 1;
        timestampColumns = new String[] {"A"} ;
    }

    public ExcelFormator(int sheetNumber,String[] timestampColumns){
        super();
        this.sheetNumber = sheetNumber;
        this.timestampColumns = timestampColumns;
    }



    @Override
    public long getNextFrequency() {
        if(timestampScanner.hasNextLine()){
            String currentTimestamp = timestampScanner.nextLine();
            long freq =  transform(currentTimestamp) - transform(lastTimestamp);
            lastTimestamp = currentTimestamp;
            return freq;
        } else {
            return 0;
        }
    }


    @Override
    protected Map<String, String> getNextValue() {
        Map<String,String> values = new HashMap<String, String>();
        for(Map.Entry<String,Scanner> entry : filesScanners.entrySet()){
            values.put(entry.getKey(),entry.getValue().nextLine());
        }
        return values;
    }

    @Override
    protected void close() {
        for(Scanner sc : filesScanners.values()){
            sc.close();
        }

    }

    @Override
    protected void beginReplay() {
        ExcelExtractor extractor = new ExcelExtractor(getInput(),sheetNumber,params, Arrays.asList(timestampColumns),2);
        extractor.processSheet();
        filesScanners = new HashMap<String, Scanner>();
        try {
            for(Map.Entry<String,String> entry : extractor.getFilesWriters().entrySet()){
                filesScanners.put(entry.getKey(), new Scanner(new FileReader(extractor.getTimestampRef())));
            }
            this.timestampScanner = new Scanner(new FileReader(extractor.getTimestampRef()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        lastTimestamp = timestampScanner.nextLine();
    }


    @Override
    public boolean hasNextLine() {
        for(Map.Entry<String,Scanner> entry : filesScanners.entrySet()){
            return entry.getValue().hasNextLine();
        }
        return false;
    }
}
