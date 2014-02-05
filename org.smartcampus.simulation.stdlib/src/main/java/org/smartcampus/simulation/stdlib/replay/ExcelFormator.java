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
    private int offset;
    private String lastTimestamp;

    public ExcelFormator(){
        super();
        sheetNumber = 1;
        timestampColumns = new String[] {"A"} ;
        offset = 2;

    }

    public ExcelFormator(int sheetNumber,String[] timestampColumns,int offset){
        super();
        this.sheetNumber = sheetNumber;
        this.timestampColumns = timestampColumns;
        this.offset = offset;
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
            System.out.println();
            String line = entry.getValue().nextLine();
            if(!line.isEmpty()){
                values.put(entry.getKey(),line);
            }
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
        Map<String,String> columns = new HashMap<String, String>();
        for(Map.Entry<String,String> entry : params.entrySet()){
            columns.put(entry.getValue(),entry.getKey());
        }
        ExcelExtractor extractor = new ExcelExtractor(getInput(),sheetNumber,columns, Arrays.asList(timestampColumns),offset);
        extractor.processSheet();
        filesScanners = new HashMap<String, Scanner>();
        try {
            for(Map.Entry<String,String> entry : extractor.getFilesWriters().entrySet()){
                filesScanners.put(entry.getKey(), new Scanner(new FileReader(entry.getValue())));
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
