package org.smartcampus.simulation.stdlib.replay;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.smartcampus.simulation.framework.simulator.FileFormator;

public class ReplayTxt extends FileFormator {

    protected FileReader fr;
    protected BufferedReader br;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getNextValue() {
        try {
            return this.br.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getNbLine() {
        try {
            int res = 0;

            this.fr = new FileReader(this.getInput());
            this.br = new BufferedReader(this.fr);

            // lecture du fichier texte
            while (this.br.readLine() != null) {
                res++;
            }

            this.br.close();
            this.fr.close();

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected String beginReplay(final int firstLine) {
        try {
            this.fr = new FileReader(this.getInput());
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

    /**
     * {@inheritDoc}
     */
    @Override
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
