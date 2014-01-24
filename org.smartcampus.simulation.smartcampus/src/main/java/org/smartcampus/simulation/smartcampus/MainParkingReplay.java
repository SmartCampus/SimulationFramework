package org.smartcampus.simulation.smartcampus;

import java.util.concurrent.TimeUnit;

import org.smartcampus.simulation.framework.fluentapi.Start;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.stdlib.law.PolynomialLaw;
import org.smartcampus.simulation.stdlib.replay.ReplayTxt;

import scala.concurrent.duration.Duration;

public class MainParkingReplay {

    public static void main(final String[] args) {
        Law<Double, Double> polynome = new PolynomialLaw(24839.21865,
                -14430.25924, 3359.404392, -401.9522656, 26.18040012,
                -0.8830270156, 0.01208028907);

        Start sim = new StartImpl();
        sim.replay("Parking1", ReplayTxt.class).setInput("testReplay.txt")
                .setOutput("http://localhost:8080/collector/value")
                .start(System.currentTimeMillis())
                .duration(Duration.create(30, TimeUnit.SECONDS))
                .frequency(Duration.create(1, TimeUnit.SECONDS)).simulateReal();
    }
}
