package org.smartcampus.simulation.stdlib.laws;

import java.util.LinkedList;
import java.util.List;

import org.smartcampus.simulation.framework.simulator.Law;




/**
 * @author Jerome Rancati 
 * @creationDate  15 January 2014
 *
 */
public class PolynomialLaw extends Law<Double, Double> {
    List<Double> coeficients;
    

    /**
     * create the PolynomialLaw
     * @param doubles each coefficent of the polynomial sorting from the lower power to the higher one
     * @param doubles
     */
    public PolynomialLaw(Double ...doubles) {
        this.coeficients = new LinkedList<Double>();
        for(Double e:doubles){
            this.coeficients.add(e);
        }
    }
    
    @Override
    public Double evaluate(Double x){
    	double res=0;
        for(int i=0;i<this.coeficients.size(); i++){
            res+=this.coeficients.get(i)*Math.pow(x, i);
        }
        return res;
    }

}

/*
    private double calculate(long time) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);
		double t = c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE)/60;
		if(t<6.5 || t> 18.5) return 0;
		double res=0.01208028907 * Math.pow(t, 6) - 0.8830270156 * Math.pow(t, 5)
				+ 26.18040012 * Math.pow(t, 4) - 401.9522656 * Math.pow(t, 3)
				+ 3359.404392 * Math.pow(t, 2) - 14430.25924 * t + 24839.21865;
		return res>0?res:0;
	}
 */


