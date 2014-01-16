/**
 * 
 */
package org.smartcampus.simulation.framework.simulator;


/**
 * @author Jerome Rancati 
 * @creationDate  15 January 2014
 *
 */
public abstract class Law {
    
    /**
     * Return the percentage of chance to be between the abscissa axe and the curb 
     * @param i the abscissa the point
     * @return the percentage
     * @throws BadAbscissaException 
     */
    public abstract double percentage(int i);
    
}
