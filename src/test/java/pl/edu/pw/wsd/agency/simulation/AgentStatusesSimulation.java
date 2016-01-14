package pl.edu.pw.wsd.agency.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.agent.EntityLocationAgent;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.container.launcher.ContainerLauncher;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;

/**
 * Test runs jade platform and checks if agent statuses functionality works
 * @author marcin.czerwinski
 *
 */
@Ignore("Requires running jade platform")
public class AgentStatusesSimulation {

	private static ClientAgent clientAgent;
	private static TransmitterAgent transmitterAgent1;
	private static TransmitterAgent transmitterAgent2;
	private static SupervisorAgent supervisorAgent;
	
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	@Before
	public void setup() {
		clientAgent = new ClientAgent("statusesSimulation/ClientAgent.properties");
		transmitterAgent1 = new TransmitterAgent("statusesSimulation/transmitterAgent1.properties");
		transmitterAgent2 = new TransmitterAgent("statusesSimulation/transmitterAgent2.properties");
		supervisorAgent = new SupervisorAgent("statusesSimulation/supervisorAgent.properties");
		
		runPlatform();
	}
	
	private void runPlatform() {
		executor.submit(() -> {
			ContainerLauncher.runMainContainer(false);
			ContainerLauncher.runRemoteContainer(new RunnableContainer() {
				
				@Override
				public List<Agent> getAgentsToRun() {
					List<Agent> agentsToRun = new ArrayList<>();
					agentsToRun.add(clientAgent);
					agentsToRun.add(transmitterAgent1);
					agentsToRun.add(transmitterAgent2);
					agentsToRun.add(supervisorAgent);
					agentsToRun.add(new EntityLocationAgent());
					agentsToRun.add(new LocationRegistryAgent());
					return agentsToRun;
				}
			});
		});
	}

	@Test
	public void shouldPropagateStatusesToSupervisor() throws InterruptedException {
		executor.awaitTermination(100, TimeUnit.SECONDS);
		Assert.assertEquals(3, supervisorAgent.getAgentStatuses().keySet().size());
	}
}
