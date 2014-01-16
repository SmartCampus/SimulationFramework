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
    int intervalMin=0;
    int intervalMax=24;
    int majoration=100;
    

    /**
     * create the PolynomialLaw
     * @param doubles each coefficent of the polynomial sorting from the lower power to the higher one
     * @param min the minimum of definition of the polynomial
     * @param max the maximum of definition of the polynomial
     * @param majoration the maximum point  Northing (ordonnee) of the function
     * @param doubles
     */
    public PolynomialLaw(int min, int max, int majoration, Double ...doubles) {
        this.intervalMax=max;
        this.intervalMin=min;
        this.majoration=majoration;
        this.coeficients = new LinkedList<Double>();
        for(Double e:doubles){
            this.coeficients.add(e);
        }
    }
    
    /**
     * evaluate the polynomial at the value x
     * @param x the abscissa of the polynomial function 
     * @return the evaluation of the polynomial function at the abscissa x
     */
    private double evaluate(int x){
        double res = this.coeficients.get(0);
        for(int i=1;i<this.coeficients.size(); i++){
            res+=this.coeficients.get(i)*Math.pow(x, i-1);
        }
        return res;
    }
    
    private boolean inTheInterval(int abscisse){
        if(abscisse>=this.intervalMin && abscisse<=this.intervalMax) return true;
        return false;
    }
    
    @Override
    public double percentage(int abscisse) {
        if (inTheInterval(abscisse)) {
            return evaluate(abscisse)*100/this.majoration;
        } else {
        	return -1;
        }
        
    }


}
