package org.smartcampus.simulation.stdlib.laws;

import java.util.LinkedList;
import java.util.List;
import org.smartcampus.simulation.framework.simulator.Law;

/**
 * @author Jerome Rancati
 * @creationDate 15 January 2014
 * 
 */
public class PolynomialLaw extends Law<Double, Double> {

    List<Double> coeficients;

    /**
     * create the PolynomialLaw. Each monomial has to be written even if it's 0
     * 
     * @param doubles
     *            each coefficent of the polynomial sorting from the lower power
     *            to the higher one
     */
    public PolynomialLaw(final Double... doubles) {
        this.coeficients = new LinkedList<Double>();
        for (Double e : doubles) {
            this.coeficients.add(e);
        }
    }

    @Override
    /**
     * @inheritDoc
     * Must receive 1 double
     */
    public Double evaluate(final Double... x) {
        double res = 0;
        if (x == null) {
            return 0.;
        }
        double n = x[0];
        for (int i = 0; i < this.coeficients.size(); i++) {
            res += this.coeficients.get(i) * Math.pow(n, i);
        }
        return res;
    }

    /**
     * @return the coeficients
     */
    protected List<Double> getCoeficients() {
        return this.coeficients;
    }

    /**
     * Getter for the original ordonnee
     * 
     * @return the original ordonnee
     */
    protected Double getOriginalOrdonee() {
        return this.coeficients.get(0);
    }

}
