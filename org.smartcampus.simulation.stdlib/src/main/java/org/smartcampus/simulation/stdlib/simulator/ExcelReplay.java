package org.smartcampus.simulation.stdlib.simulator;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by foerster
 * on 23/01/14.
 */
public class ExcelReplay {

    private File tmp;
    private BufferedWriter writer;

    public ExcelReplay(){
        try {
            tmp = File.createTempFile("excel_data",".tmp");
            writer = new BufferedWriter(new FileWriter(tmp));
        } catch (IOException e) {
            System.err.println("Cannot create temp file for excel replay");
        }
    }

    public File getFileRef(){
        return tmp;
    }

    public void processOneSheet(String filename,String sheetNumber,String colName,int startLine,int numberOfLines) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst,colName,startLine,numberOfLines);

        // rId2 found by processing the Workbook
        // Seems to either be rId# or rSheet#
        InputStream sheet2 = r.getSheet("rId"+sheetNumber);
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        writer.flush();
        writer.close();
        sheet2.close();
    }


    public XMLReader fetchSheetParser(SharedStringsTable sst,String colName,int startLine,int numberOfLine) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader();
        ContentHandler handler = new SheetHandler(sst,colName,startLine,numberOfLine,writer);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private String colName;
        private int startLine;
        private int numberOfLine;
        private boolean readThisValue;
        private Pattern pattern;
        private int currentLine;
        private Writer writer;

        private SheetHandler(SharedStringsTable sst,String colName,int startLine,int numberOfLine,Writer writer) {
            this.sst = sst;
            this.colName = colName;
            readThisValue = true;
            this.startLine=startLine;
            this.numberOfLine = numberOfLine;
            pattern = Pattern.compile("([A-Z]+)(.+)");
            currentLine = 1;
            this.writer = writer;
        }

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
                String cellRef = attributes.getValue("r");
                Matcher match = pattern.matcher(cellRef);
                if(match.find()){
                    String colRef = match.group(1);
                    int rowRef = Integer.valueOf(match.group(2));
                    readThisValue = colRef.equals(colName);
                    readThisValue = readThisValue && rowRef >= startLine && rowRef <= startLine + numberOfLine ;
                } else {
                    readThisValue = false;
                }
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if(cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            // Clear contents cache
            lastContents = "";

        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {
            if(readThisValue){
                // Process the last contents as required.
                // Do now, as characters() may be called more than once
                if(nextIsString) {
                    int idx = Integer.parseInt(lastContents);
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    nextIsString = false;
                }

                // v => contents of a cell
                // Output after we've seen the string contents
                if(name.equals("v")) {
                    try {
                        writer.write(lastContents+"\n");
                    } catch (IOException e) {
                        System.err.println("Cannot write to file its f**king annoying");
                    }
                }
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }
    }

    public static void main(String[] args) throws Exception {
        ExcelReplay howto = new ExcelReplay();
        //howto.processOneSheet(args[0]);
        howto.processOneSheet("/home/foerster/Documents/biotime_20120807_092111_nettoye.xlsx", "2","G",2,500000);
        System.out.println(howto.getFileRef().getAbsolutePath());
    }

}
