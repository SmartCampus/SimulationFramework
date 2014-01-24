package org.smartcampus.simulation.stdlib.replay;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by foerster
 * on 24/01/14.
 */
public class LireTxt {

    private FileReader fr;
    private BufferedReader br;
    private String path;

    public LireTxt(String path){
        this.path = path;
    }



    protected String getNextValue() {
        try {
            return this.br.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return null;
        }
    }

    protected int getnbLine() {
        try {
            int res = 0;

            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);

            // lecture du fichier texte
            while (br.readLine() != null) {
                res++;
            }

            br.close();
            fr.close();

            return res;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return -1;
        }
    }

    protected String beginReplay(final int firstLine){
        try {
            this.fr = new FileReader(path);
            this.br = new BufferedReader(this.fr);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int nbLine = 0;
        String line;
        try {
            while ((line = this.br.readLine()) != null) {
                if (nbLine == firstLine) {
                    return line;
                }
                nbLine++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    protected void close() {
        try {
            this.br.close();
            this.fr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
