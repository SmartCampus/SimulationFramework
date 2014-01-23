package org.smartcampus.simulation.framework.simulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import org.smartcampus.simulation.framework.messages.SendValue;

/**
 * @inheritDoc
 * 
 *             This class allow to write in a file
 */
public class DataWriter extends DataMaker {
    private final String path;

    public DataWriter(final String s) {
        super(s);
        this.path = System.getProperty("user.dir") + "/" + this.output + ".txt";
        File tmp = new File(this.path);
        if (tmp.exists()) {
            tmp.delete();
        }
    }

    @Override
    /**
     * @inheritDoc
     */
    public void onReceive(final Object o) throws Exception {
        if (o instanceof SendValue) {
            SendValue sendValue = (SendValue) o;
            this.log.debug("J'ecris le " + new Timestamp(sendValue.getTime())
                    + " dans un fichier la valeur : { n : " + sendValue.getName()
                    + ", v :" + sendValue.getValue() + ", t:" + sendValue.getTime()
                    + "} ");
            this.ecrire("{ n : " + sendValue.getName() + ", v :" + sendValue.getValue()
                    + ", t:" + sendValue.getTime() + "} "
                    + new Timestamp(sendValue.getTime()) + "\n");
        }
    }

    private void ecrire(final String texte) {
        try {
            FileWriter fw = new FileWriter(this.path, true);

            BufferedWriter output = new BufferedWriter(fw);

            output.write(texte);

            output.flush();

            output.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

}
