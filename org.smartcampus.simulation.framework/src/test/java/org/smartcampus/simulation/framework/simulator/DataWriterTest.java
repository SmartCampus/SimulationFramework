package org.smartcampus.simulation.framework.simulator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartcampus.simulation.framework.messages.SendValue;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;

public class DataWriterTest {

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
    public void testDataWriter() {
        new JavaTestKit(system) {
            {
                final ActorRef dataWriter = system.actorOf(Props.create(DataWriter.class,
                        "testFile2"));

                dataWriter.tell(new SendValue("Name", "Value", 123456789), this.getRef());
                dataWriter.tell(new SendValue("Name", "Value", 123456789), this.getRef());
                dataWriter.tell(new SendValue("Name", "Value", 123456789), this.getRef());
                dataWriter.tell(new SendValue("Name", "Value", 123456789), this.getRef());
                dataWriter.tell(new SendValue("Name", "Value", 123456789), this.getRef());

                dataWriter.tell("hello", this.getRef());
                this.expectNoMsg(duration("1 second"));

                String path = System.getProperty("user.dir") + "/testFile2.txt";
                File tmp = new File(path);
                Assert.assertTrue(tmp.exists());
                Assert.assertEquals(5, DataWriterTest.this.nbLine(path));

                final ActorRef dataWriter2 = system.actorOf(Props.create(
                        DataWriter.class, "testFile2"));

                dataWriter2.tell(new SendValue("Name", "Value", 123456789), this.getRef());
                dataWriter2.tell(new SendValue("Name", "Value", 123456789), this.getRef());
                dataWriter2.tell(new SendValue("Name", "Value", 123456789), this.getRef());

                this.expectNoMsg(duration("1 second"));

                tmp = new File(path);
                Assert.assertTrue(tmp.exists());
                Assert.assertEquals(3, DataWriterTest.this.nbLine(path));
            }
        };

    }

    private int nbLine(final String name) {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(name));

            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            is.close();
            return ((count == 0) && !empty) ? 1 : count;
        } catch (IOException e) {
            System.err.println("Impossible d'ouvrir le fichier : ");
            e.printStackTrace();
            return -1;
        }
    }

}
