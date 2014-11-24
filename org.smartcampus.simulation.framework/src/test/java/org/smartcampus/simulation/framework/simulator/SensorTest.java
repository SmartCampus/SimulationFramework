package org.smartcampus.simulation.framework.simulator;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartcampus.simulation.framework.messages.InitSensorRealSimulation;
import org.smartcampus.simulation.framework.messages.InitSensorVirtualSimulation;
import org.smartcampus.simulation.framework.messages.ReturnMessage;
import org.smartcampus.simulation.framework.messages.StopSimulation;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;

public class SensorTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("AkkaTestModele");
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testSensorReal() {
        new JavaTestKit(system) {
            {
                BooleanSensorTransformation t = new BooleanSensorTransformation();

                final TestActorRef<Sensor<Object, Boolean>> sensorTrue = TestActorRef
                        .create(system, Props.create(Sensor.class, t));

                this.watch(sensorTrue);

                // Init the type of the simulation
                sensorTrue.tell(new InitSensorRealSimulation("http://localhost:8080/value"), this.getRef());

                // Check that the last value is equal to null
                Assert.assertNull(sensorTrue.underlyingActor().getLastReturnedValue());

                // Send a update message, it have to change the last value to True
                sensorTrue.tell(new UpdateSensorSimulation<Object>(1, null),
                        this.getRef());

                // Check if the last value is equal to true
                Assert.assertTrue(sensorTrue.underlyingActor().getLastReturnedValue());

                this.expectMsgClass(ReturnMessage.class);

                // Send a update message, it have to change the last value to false
                sensorTrue.tell(new UpdateSensorSimulation<Object>(1, null),
                        this.getRef());

                // Check if the last value is equal to false
                Assert.assertFalse(sensorTrue.underlyingActor().getLastReturnedValue());

                this.expectMsgClass(ReturnMessage.class);

                // Send a Stop message
                sensorTrue.tell(new StopSimulation(), this.getRef());

                // check if the sensor is dead
                this.expectMsgClass(Terminated.class);
            }
        };
    }

    @Test
    public void testSensorVirtual() {
        new JavaTestKit(system) {
            {
                BooleanSensorTransformation t = new BooleanSensorTransformation();

                final TestActorRef<Sensor<Object, Boolean>> sensorTrue = TestActorRef
                        .create(system, Props.create(Sensor.class, t));

                this.watch(sensorTrue);

                final TestActorRef<DataWriter> dataWriter = TestActorRef.create(system,
                        Props.create(DataWriter.class, "testFile"));

                // Init the type of the simulation
                sensorTrue.tell(new InitSensorVirtualSimulation(dataWriter),
                        this.getRef());

                // Check that the last value is equal to null
                Assert.assertNull(sensorTrue.underlyingActor().getLastReturnedValue());

                // Send a update message, it have to change the last value to True
                sensorTrue.tell(new UpdateSensorSimulation<Object>(1, null),
                        this.getRef());

                // Check if the last value is equal to true
                Assert.assertTrue(sensorTrue.underlyingActor().getLastReturnedValue());

                this.expectMsgClass(ReturnMessage.class);

                // Send a update message, it have to change the last value to false
                sensorTrue.tell(new UpdateSensorSimulation<Object>(1, null),
                        this.getRef());

                // Check if the last value is equal to false
                Assert.assertFalse(sensorTrue.underlyingActor().getLastReturnedValue());

                this.expectMsgClass(ReturnMessage.class);

                // Send a Stop message
                sensorTrue.tell(PoisonPill.getInstance(), this.getRef());

                // check if the sensor is dead
                this.expectMsgClass(Terminated.class);
            }
        };
    }

    /**
     * Send the opposite boolean
     */
    private static class BooleanSensorTransformation implements
            SensorTransformation<Object, Boolean> {

        @Override
        public Boolean transform(final Object o, final Boolean last) {
            if ((last == null) || (last == false)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

}
