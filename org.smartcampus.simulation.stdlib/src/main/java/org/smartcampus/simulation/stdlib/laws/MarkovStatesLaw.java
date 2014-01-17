package org.smartcampus.simulation.stdlib.laws;

import java.util.ArrayList;
import java.util.List;

import javax.management.BadAttributeValueExpException;

import org.smartcampus.simulation.framework.simulator.Law;

public class MarkovStatesLaw extends Law<Integer, Double> {

	private int size;
	private List<Double> sameState;
	private List<Double> previousState;
	private List<Double> nextState;

	public MarkovStatesLaw(List<Double> prev, List<Double> same,
			List<Double> next) throws BadAttributeValueExpException {
		this.size = same.size();
		if (next.size() != (size - 1) || prev.size() != (size - 1)) {
			throw new BadAttributeValueExpException("Bad list sizes");
		}
		sameState = new ArrayList<Double>(same);
		nextState = new ArrayList<Double>(next);
		previousState = new ArrayList<Double>(1);
		previousState.addAll(prev);
	}

	@Override
	protected Double evaluate(Integer... x) throws Exception {
		if (x.length != 2)
			throw new BadAttributeValueExpException("you need to give two ints");
		int i = x[0];
		int j = x[1];
		if (i < 0 || i > size - 1 || j < 0 || j > size - 1) {
			throw new BadAttributeValueExpException(
					"ints must be between 0 and " + (size - 1));
		}
		Double res;
		if (i == j)
			res = sameState.get(i - 1);
		else if (i < j)
			res = nextState.get(i - 1);
		else
			res = previousState.get(i - 1);
		return res!=null?res:0.0;
	}

}
