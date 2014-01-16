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
     * create the PolynomialLaw.
     * Each monomial has to be written even if it's null
     * @param doubles each coefficent of the polynomial sorting from the lower power to the higher one
     */
    public PolynomialLaw(Double ...doubles) {
        this.coeficients = new LinkedList<Double>();
        for(Double e:doubles){
            this.coeficients.add(e);
        }
    }
    
    @Override
    public double evalute(int x){
        double res = 0;
        for(int i=0;i<getCoeficients().size(); i++){
            res+=getCoeficients().get(i)*Math.pow(x, i);
        }
        return res;
    }
    
    /**
     * Getter for the original ordonnee
     * @return the original ordonnee
     */
    private Double getOriginalOrdonee(){
        return this.coeficients.get(0);
    }

    /**
     * @return the coeficients
     */
    protected List<Double> getCoeficients() {
        return coeficients;
    }

    /**
     * @param coeficients the coeficients to set
     */
    protected void setCoeficients(List<Double> coeficients) {
        this.coeficients = coeficients;
    }
    
}
