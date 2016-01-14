package pl.edu.pw.wsd.agency.simulation;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.container.launcher.ContainerLauncher;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

public class AgentConversationSimulation {

	private static Agent agent1;
	private static Agent agent2;
	private static CountDownLatch waitForTwoMessages = new CountDownLatch(2);
	
	@SuppressWarnings("serial")
	@Before
	public void setup() {
		agent1 = new Agent();
		agent2 = new Agent();
		agent1.addBehaviour(new TickerBehaviour(agent1, 1000) {
			
			@Override
			protected void onTick() {
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent("Message from " + DateTime.now());
				msg.addReceiver(agent2.getAID());
				msg.setSender(agent1.getAID());
				myAgent.send(msg);
			}
		});
		agent2.addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				ACLMessage msg = myAgent.receive();
				if(msg != null) {
					waitForTwoMessages.countDown();
					System.out.println(msg.getContent());
				} else {
					block();
				}
			}
		});
		runPlatform();
	}
	
	private void runPlatform() {
		new Thread(() -> {
			ContainerLauncher.runMainContainer(false);
			ContainerLauncher.runRemoteContainer(new RunnableContainer() {
				
				@Override
				public List<Agent> getAgentsToRun() {
					List<Agent> agentsToRun = new ArrayList<>();
					agentsToRun.add(agent1);
					agentsToRun.add(agent2);
					return agentsToRun;
				}
			});
		}).start();
	}
	
	@Test
	public void shouldSendTwoMessages() throws InterruptedException {
		waitForTwoMessages.await(20, TimeUnit.SECONDS);
	}
}
