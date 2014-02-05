package org.smartcampus.simulation.framework.fluentapi.replay;

import org.smartcampus.simulation.framework.simulator.FileFormator;

/**
 * Created by foerster on 21/01/14.
 */
public interface StartReplay {

    public ReplayWrapper0 replay(String string, Class<? extends FileFormator> class1);
}
