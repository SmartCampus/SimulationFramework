package org.smartcampus.simulation.stdlib.laws;

import java.util.LinkedList;
import java.util.List;

import org.smartcampus.simulation.framework.simulator.Law;



/**
 * @author Jerome Rancati 
 * @creationDate  15 January 2014
 *
 */
public class PolynomialLaw extends Law {
    List<Double> coeficients;
    

    /**
     * create the PolynomialLaw
     * @param doubles each coefficent of the polynomial sorting from the lower power to the higher one
     * @param min the minimum of definition of the polynomial
     * @param max the maximum of definition of the polynomial
     * @param majoration the maximum point  Northing (ordonnee) of the function
     * @param doubles
     */
    public PolynomialLaw(Double ...doubles) {
        this.coeficients = new LinkedList<Double>();
        for(Double e:doubles){
            this.coeficients.add(e);
        }
    }
    
    @Override
    public double evalute(int x){
        double res = this.coeficients.get(0);
        for(int i=1;i<this.coeficients.size(); i++){
            res+=this.coeficients.get(i)*Math.pow(x, i-1);
        }
        return res;
    }

}
