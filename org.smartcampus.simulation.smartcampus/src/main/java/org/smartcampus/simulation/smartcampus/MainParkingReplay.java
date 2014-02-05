package org.smartcampus.simulation.smartcampus;

import org.smartcampus.simulation.framework.fluentapi.Start;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.smartcampus.replay.SuezExcelFormator;
import org.smartcampus.simulation.stdlib.law.PolynomialLaw;

public class MainParkingReplay {

    public static void main(final String[] args) {
        Law<Double, Double> polynome = new PolynomialLaw(24839.21865, -14430.25924,
                3359.404392, -401.9522656, 26.18040012, -0.8830270156, 0.01208028907);

        Start sim = new StartImpl();

          sim.replay("Parking1", SuezExcelFormator.class).withSensor("katikate", "G")
                  .withSensor("cecceile","H")
          .setInput("/home/foerster/Documents/truc.xlsx")
          .setOutput("swag")
          .start(System.currentTimeMillis()).simulateVirtual();
    }
}
