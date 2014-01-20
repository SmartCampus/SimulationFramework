package org.smartcampus.simulation.framework.simulator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;

/*
 Site internet interessant :
 - Pour savoir comment tester des Acteurs. C'est en Scala mais la logique derrière est bien expliquée:
 https://weblogs.java.net/blog/manningpubs/archive/2012/09/28/testing-actors-akka

 - La syntaxe en java + toutes les possibilitees:
 http://doc.akka.io/docs/akka/snapshot/java/testing.html

 J'ai repris les exemples et les ai mis dans la classe ci-dessous.
 */
public class AkkaTestModele {

	// Classe Interne utilisee dans les exemples
	private static class MyActor extends UntypedActor {
		public void onReceive(Object o) throws Exception {
			if (o.equals("say42")) {
				getSender().tell(42, getSelf());
			} else if (o instanceof Exception) {
				throw (Exception) o;
			}
		}

		public boolean testMe() {
			return true;
		}
	}

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

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	public void testType() {
		// Cree un Props associe a la classe testee
		final Props props = Props.create(MyActor.class);

		//
		// 1) Tester une classe Acteur :
		//

		// Cree un TestActorRef pour la classe testee
		// Il est donc possible de tester l'envoi de message vers la classe
		// testee
		final TestActorRef<MyActor> ref = TestActorRef.create(system, props,
				"testMyActor");
		// Par exemple :
		final Future<Object> future = akka.pattern.Patterns.ask(ref, "say42",
				3000);

		Assert.assertTrue(future.isCompleted());

		try {
			Assert.assertEquals(42, Await.result(future, Duration.Zero()));
		} catch (Exception e) {
			Assert.fail("didn't expect an exception to be thrown");
		}

		// Recupere la classe testee
		// Il est donc possible de tester des methodes internes à la classe
		final MyActor actor = ref.underlyingActor();
		// Par exemple :
		Assert.assertTrue(actor.testMe());

		//
		// 2) Testes Integration :
		//
		new JavaTestKit(system) {
			{
				final Props props = Props.create(MyActor.class);
				final ActorRef subject = system.actorOf(props);

				// can also use JavaTestKit “from the outside”
				final JavaTestKit probe = new JavaTestKit(system);
				// “inject” the probe by passing it to the test subject
				// like a real resource would be passed in production
				subject.tell(probe.getRef(), getRef());
				// await the correct response
				expectMsgEquals(duration("1 second"), "done");
				// the run() method needs to finish within 3 seconds
				new Within(duration("3 seconds")) {
					protected void run() {

						subject.tell("hello", getRef());

						// This is a demo: would normally use expectMsgEquals().
						// Wait time is bounded by 3-second deadline above.
						new AwaitCond() {
							protected boolean cond() {
								return probe.msgAvailable();
							}
						};

						// response must have been enqueued to us before probe
						expectMsgEquals(Duration.Zero(), "world");
						// check that the probe we injected earlier got the msg
						probe.expectMsgEquals(Duration.Zero(), "hello");
						Assert.assertEquals(getRef(), probe.getLastSender());

						// Will wait for the rest of the 3 seconds
						expectNoMsg();
					}
				}; //End of WithIn
			}
		}; // End of JavaTestKit
		
		
	}

	/*
	 * Methodes pouvant etre utilisées :
	 * 
	 * final String hello = expectMsgEquals("hello"); final Object any =
	 * expectMsgAnyOf("hello", "world"); final Object[] all =
	 * expectMsgAllOf("hello", "world"); final int i =
	 * expectMsgClass(Integer.class); final Number j =
	 * expectMsgAnyClassOf(Integer.class, Long.class); expectNoMsg(); final
	 * Object[] two = receiveN(2); Number of receive message.
	 * 
	 * 
	 * Il est possible de donner une Duration en premier parametre. Par default,
	 * c'est 3s.
	 */
}
