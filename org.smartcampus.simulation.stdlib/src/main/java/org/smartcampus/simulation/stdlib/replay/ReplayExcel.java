package org.smartcampus.simulation.stdlib.replay;

import org.smartcampus.simulation.framework.simulator.Replay;

import java.io.File;

/**
 * Created by foerster
 * on 24/01/14.
 */
public class ReplayExcel extends Replay {

    private LireTxt reader;

    public ReplayExcel(){
        ExcelExtractor extractor = new ExcelExtractor();
        extractor.processOneSheet(getInput(),"2","G");
        String path = extractor.getFileRef().getAbsolutePath();
        reader = new LireTxt(path);
    }

    @Override
    protected String getNextValue() {
        return reader.getNextValue();
    }

    @Override
    protected int getnbLine() {
        return reader.getnbLine();
    }

    @Override
    protected void close() {
        reader.close();
    }

    @Override
    protected String beginReplay(int i) {
        return reader.beginReplay(i);
    }
}
