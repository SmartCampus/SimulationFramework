package org.smartcampus.simulation.stdlib.replay;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.smartcampus.simulation.framework.simulator.Replay;

public class ReplayTxt extends Replay {

    @Override
    protected String getvalue(final int l) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void getnbLine() {
        // TODO Auto-generated method stub

    }

    /**
     * read the input file and set the value to send to the collector server HTTP
     */
    private void readFile() {
        // lecture du fichier texte
        try {
            InputStream ips = new FileInputStream(this.getInput());
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                this.valueToSend = line;
                // TODO
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
