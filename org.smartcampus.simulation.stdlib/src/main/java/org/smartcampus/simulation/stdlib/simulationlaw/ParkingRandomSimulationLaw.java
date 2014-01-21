package org.smartcampus.simulation.stdlib.simulationlaw;

import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.framework.simulator.SimulationLaw;
import org.smartcampus.simulation.framework.simulator.Simulator;
import org.smartcampus.simulation.stdlib.laws.RandomLaw;
import org.smartcampus.simulation.stdlib.sensors.SensorTransformationBooleanRandom;

public class ParkingRandomSimulationLaw extends SimulationLaw<Object, Object, Boolean> {

    public static void main(final String[] args) {
        Simulator s = new Simulator();
        Law<Object, Object> rand = new RandomLaw();
        s.create("Parking1", ParkingRandomSimulationLaw.class)
                .addSensors("Parking1", new SensorTransformationBooleanRandom(), 5)
                .initSimulation("Parking1", rand);
        s.simulate(10, 10, 1);
    }

    @Override
    protected Object[] computeValue() {
        return null;
    }

    @Override
    protected void onComplete() {
        int res = 0;
        for (Boolean b : this.values) {
            if (b) {
                res++;
            }
        }
        this.sendValue("Average", ((100 * res) / this.values.size()) + "%");
    }
}
