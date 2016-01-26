package pl.edu.pw.wsd.agency.simulation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.config.properties.PropertiesClientAgentConfiguration;
import pl.edu.pw.wsd.agency.config.properties.PropertiesSupervisorConfiguration;
import pl.edu.pw.wsd.agency.config.properties.PropertiesTransmitterConfiguration;
import pl.edu.pw.wsd.agency.container.launcher.ContainerLauncher;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;

/**
 * Test runs jade platform and checks if agent statuses functionality works
 * @author marcin.czerwinski
 *
 */
//@Ignore("Requires running jade platform")
public class AgentStatusesSimulation {

    private static ClientAgent clientAgent;
    private static TransmitterAgent transmitterAgent1;
    private static TransmitterAgent transmitterAgent2;
    private static SupervisorAgent supervisorAgent;

    private CountDownLatch waitForStatusesFromEveryAgent = new CountDownLatch(1);

    @SuppressWarnings("serial")
    @Before
    public void setup() {
        clientAgent = new ClientAgent(new PropertiesClientAgentConfiguration("statusesSimulation/ClientAgent.properties"));
        transmitterAgent1 = new TransmitterAgent(new PropertiesTransmitterConfiguration("statusesSimulation/transmitterAgent1.properties"));
        transmitterAgent2 = new TransmitterAgent(new PropertiesTransmitterConfiguration("statusesSimulation/transmitterAgent2.properties"));
        supervisorAgent = new SupervisorAgent(new PropertiesSupervisorConfiguration("statusesSimulation/supervisorAgent.properties")) {
            @Override
            public void updateStatuses(List<AgentStatus> readAgentStatuses) {
                super.updateStatuses(readAgentStatuses);
                Map<String, AgentStatus> agentStatuses = getAgentStatuses();
                if (agentStatuses.size() == 3) {
                    waitForStatusesFromEveryAgent.countDown();
                }
            }
        };

        runPlatform();
    }

    private void runPlatform() {
        new Thread(() -> {
            ContainerLauncher.runMainContainer(false);
            ContainerLauncher.runRemoteContainer(new RunnableContainer() {

                @Override
                public List<Agent> getAgentsToRun() {
                    List<Agent> agentsToRun = new ArrayList<>();
                    agentsToRun.add(clientAgent);
                    agentsToRun.add(transmitterAgent1);
                    agentsToRun.add(transmitterAgent2);
                    agentsToRun.add(supervisorAgent);
//                    agentsToRun.add(new ViewAgent());
                    agentsToRun.add(new LocationRegistryAgent());
                    return agentsToRun;
                }
            });
        }).start();
    }

    @Test
    public void shouldPropagateStatusesToSupervisor() throws InterruptedException {
        waitForStatusesFromEveryAgent.await(100, TimeUnit.SECONDS);
        assertEquals(3, supervisorAgent.getAgentStatuses().size());
    }
}
