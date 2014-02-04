package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;
import org.smartcampus.simulation.framework.simulator.FileFormator;

public class InitReplay implements Serializable {
    private static final long serialVersionUID = 5549572936348818131L;
    private Class<? extends FileFormator> formator;

    public InitReplay(final Class<? extends FileFormator> f) {
        this.formator = f;
    }

    public Class<? extends FileFormator> getFormator() {
        return this.formator;
    }

}
