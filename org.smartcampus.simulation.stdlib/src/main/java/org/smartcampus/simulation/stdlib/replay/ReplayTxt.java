package org.smartcampus.simulation.stdlib.replay;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.smartcampus.simulation.framework.simulator.Replay;

public class ReplayTxt extends Replay {

    private LireTxt reader;


    public ReplayTxt(){
        reader = new LireTxt(getInput());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getNextValue() {
        return reader.getNextValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getnbLine() {
        return reader.getnbLine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String beginReplay(final int firstLine) {
        return reader.beginReplay(firstLine);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected void close() {
        reader.close();
    }

}
