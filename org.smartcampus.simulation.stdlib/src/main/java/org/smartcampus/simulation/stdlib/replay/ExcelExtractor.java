package org.smartcampus.simulation.stdlib.replay;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by foerster
 *
 * on 23/01/14.
 *
 */
public class ExcelExtractor {

    private String filename;
    private int sheetNumber;
    private int offset;
    private Map<String,String> columns ;
    private List<String> timestampColumn;
    private Map<String,Writer> filesWriters;
    private Map<String,File> filesRefs;
    private File timestampFile;
    private Writer timestampWriter;
    private static final String filePrefix = "data_sensor_";


    public ExcelExtractor(String filename,int sheetNumber,Map<String,String> columns,List<String> timestampColumn,int offset){
        this.filename = filename;
        this.sheetNumber = sheetNumber;
        this.columns = columns;
        this.timestampColumn = timestampColumn;
        this.offset = offset;
        this.filesWriters = new HashMap<String, Writer>();
        this.filesRefs = new HashMap<String, File>();
        try {
            for(Map.Entry<String, String> entry : columns.entrySet()){
                File tmp = File.createTempFile(filePrefix + entry.getValue() + "_", ".tmp");
                BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));
                filesWriters.put(entry.getKey(), writer);
                filesRefs.put(entry.getKey(),tmp);
            }
            File tmp = File.createTempFile("data_timestamp_",".tmp");
            timestampWriter = new BufferedWriter(new FileWriter(tmp));
            timestampFile = tmp;

        } catch (IOException e) {
            System.err.println("Cannot create temp file for excel replay");
        }
    }


    public void processSheet(){
        try{
            OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);

        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        // rId2 found by processing the Workbook
        // Seems to either be rId# or rSheet#
        InputStream sheet2 = r.getSheet("rId"+sheetNumber);
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);

        // close all the file writers
        for(Writer w : filesWriters.values()){
            w.flush();
            w.close();
        }
        timestampWriter.flush();
        timestampWriter.close();
        sheet2.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getTimestampRef(){
        return timestampFile.getPath();
    }

    public Map<String,String> getFilesWriters(){
        Map<String,String> paths = new HashMap<String, String>();
        for(Map.Entry<String,File> entry : filesRefs.entrySet()){
            paths.put(entry.getKey(),entry.getValue().getPath());
        }
        return paths;
    }


    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private boolean readThisValue;
        private boolean isTimestamp;
        private Pattern pattern;
        private int currentLine;
        private Map<String,Boolean> needBlank ;
        private String currentColumn;

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
            readThisValue = true;
            pattern = Pattern.compile("([A-Z]+)(.+)");
            currentLine = 1;
            needBlank = new HashMap<String, Boolean>();
            for(String key : columns.keySet()){
                needBlank.put(key,true);
            }
        }

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
                String cellRef = attributes.getValue("r");
                Matcher match = pattern.matcher(cellRef);
                if(match.find()){
                    int line = Integer.valueOf(match.group(2));
                    // au passage à une nouvelle ligne les colonnes non trouvées sont remplacé par des lignes vides
                    if(line > currentLine && line > offset){
                        try {
                            timestampWriter.write("\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fillWithBlank();
                        for(String key : needBlank.keySet()){
                            needBlank.put(key,true);
                        }
                    }
                    currentColumn = match.group(1);
                    currentLine = line;
                    // Figure out if the column is interesting or not
                    readThisValue = columns.keySet().contains(currentColumn) && currentLine >= offset;
                    isTimestamp = timestampColumn.contains(currentColumn) && currentLine >= offset ;
                    if(readThisValue){
                        needBlank.put(currentColumn,false);
                    }
                } else {
                    readThisValue = false;
                }
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                nextIsString = cellType != null && cellType.equals("s");
            }
            // Clear contents cache
            lastContents = "";

        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {


                // Process the last contents as required.
                // Do now, as characters() may be called more than once
                if(nextIsString) {
                    int idx = Integer.parseInt(lastContents);
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    nextIsString = false;
                }

                // v => contents of a cell

                if(name.equals("v")) {
                    try {
                        Writer dest ;
                        if(readThisValue){
                            System.out.println("Not wesh !");
                            dest = filesWriters.get(currentColumn);
                            dest.write(lastContents+"\n");

                        } else if (isTimestamp){
                            dest = timestampWriter;
                            System.out.println("Wesh ?");
                            dest.write(lastContents+" ");
                        }

                     } catch (IOException e) {
                        System.err.println("Cannot write to file its f**king annoying");
                    }
                }
        }




        public void endDocument(){
            fillWithBlank();
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }

        private void fillWithBlank(){
            for(Map.Entry<String,Boolean> entry : needBlank.entrySet()){
                if(entry.getValue()){
                    try {
                        System.out.println("Column : " + entry.getKey() + "  Blank");
                        filesWriters.get(entry.getKey()).write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String,String> columns = new HashMap<String, String>();
        columns.put("G","O2");
        columns.put("H","pH");
        columns.put("I","temp");
        List<String> timestampColumn = new ArrayList<String>();
        timestampColumn.add("A");
        timestampColumn.add("B");
        ExcelExtractor howto = new ExcelExtractor("/home/foerster/Documents/biotime_20120807_092111_nettoye.xlsx", 3,columns,timestampColumn,2);
        howto.processSheet();
    }

}
