package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;

public class InitReplayParam implements Serializable {

    private static final long serialVersionUID = 6546648934196676095L;
    private final String key;
    private final String value;

    public InitReplayParam(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

}
