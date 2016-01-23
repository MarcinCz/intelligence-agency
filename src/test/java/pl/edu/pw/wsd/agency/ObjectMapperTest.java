package pl.edu.pw.wsd.agency;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.container.launcher.ContainerLauncher;
import pl.edu.pw.wsd.agency.container.launcher.RunnableContainer;
import pl.edu.pw.wsd.agency.message.content.AgentStatistics;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;
import pl.edu.pw.wsd.agency.message.content.AgentStatus.Location;

public class ObjectMapperTest {

	@Test
	public void shouldMapAgentStatus() throws IOException {
		ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
		AgentStatus status = new AgentStatus();
		status.setLocation(new Location(1, 2));
		status.setSenderId("senderId");
		status.setTimestamp(DateTime.now());
		AgentStatistics statistics = new AgentStatistics();
		statistics = new AgentStatistics();
		statistics.setMessagesReceived(1);
		statistics.setMessagesSent(2);
		status.setStatistics(statistics);
		
		String statusString = mapper.writeValueAsString(status);
		System.out.println(statusString);
		
		AgentStatus statusFromJson = mapper.readValue(statusString, AgentStatus.class);
		
		assertEquals(status.getLocation().getX(), statusFromJson.getLocation().getX(), 0.1);
		assertEquals(status.getLocation().getY(), statusFromJson.getLocation().getY(), 0.1);
		assertEquals(status.getSenderId(), statusFromJson.getSenderId());
		assertEquals(status.getTimestamp().getMillis(), statusFromJson.getTimestamp().getMillis());
		assertEquals(status.getStatistics().getMessagesReceived(), statusFromJson.getStatistics().getMessagesReceived());
		assertEquals(status.getStatistics().getMessagesSent(), statusFromJson.getStatistics().getMessagesSent());
	}
	
	/**
	 * Runs real jade platform, without it AIDs don't work properly - they throw some errors about unknown locale.
	 */
	@Ignore("takes long to execute and currently we don't map AIDs")
	@Test
	public void shouldMapAID() throws IOException, InterruptedException {
		CountDownLatch waitForTestCompletion = new CountDownLatch(1);
		@SuppressWarnings("serial")
		Agent agent = new Agent() {
			@Override
			protected void setup() {
				super.setup();
				testAIDMapping(this.getAID());
				waitForTestCompletion.countDown();
			};
		};
		
		runAgent(agent);
		waitForTestCompletion.await();
	}

	private void testAIDMapping(AID aid) {
		ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
		String aidString;
		try {
			aidString = mapper.writeValueAsString(aid);
			System.out.println(aidString);
			
			AID mappedAID = mapper.readValue(aidString, AID.class);

			assertEquals(aid.getName(), mappedAID.getName());
			assertEquals(aid.getLocalName(), mappedAID.getLocalName());
			assertEquals(aid.getAddressesArray()[0], mappedAID.getAddressesArray()[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void runAgent(Agent agent) {
		new Thread(() -> {
			ContainerLauncher.runMainContainer(false);
			ContainerLauncher.runRemoteContainer(new RunnableContainer() {
				
				@Override
				public List<Agent> getAgentsToRun() {
					List<Agent> agentsToRun = new ArrayList<>();
					agentsToRun.add(agent);
					return agentsToRun;
				}
			});
		}).run();
	}
	
}
