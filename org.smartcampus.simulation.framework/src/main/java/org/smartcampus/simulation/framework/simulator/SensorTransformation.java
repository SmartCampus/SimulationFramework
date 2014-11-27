package org.smartcampus.simulation.framework.simulator;

/**
 * Implementing this interface allows an object to be the target for the method transform.
 * The generics parameter :
 * - T correspond to the return of the associative Law's method 'evaluate'
 * - R correspond to the HTTP request value
 */
public abstract class SensorTransformation<T, R> {
    /**
     * 
     * Transform a T value into a R result.
     * 
     * @param val
     *            , the return type of the associative Law
     * @param lastVal
     *            , the last value return by the sensor
     * @return the HTTP request value
     */
    public abstract R transform(T val, R lastVal);

    /**
     * For the sensors whitch send value on event
     * @param lastVal the last value calculated by the Law
     * @param currentValue the value just calculated by the Law.
     * @return      true   if the currentValue param has to be sent
     *              false  if the currentValue param doesn't have to to be sent (not changing state compare to the previous one)
     */
    public boolean hasToSendData(R lastVal, R currentValue){return true;}
}
