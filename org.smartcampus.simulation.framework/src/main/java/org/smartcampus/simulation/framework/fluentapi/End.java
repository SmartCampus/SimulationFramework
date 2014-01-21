package org.smartcampus.simulation.framework.fluentapi;

/**
 * Created by foerster on 21/01/14.
 */
public interface End {
    public void simulateReal(final int begin, final int duration, final int interval);

    public void simulateVirtual(final int begin, final int duration, final int interval);
}
