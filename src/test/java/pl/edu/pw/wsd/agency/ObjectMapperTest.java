package pl.edu.pw.wsd.agency;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.AgentStatistics;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;

public class ObjectMapperTest {

	@Test
	public void shouldMapAgentStatus() throws IOException {
		ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
		AgentStatus status = new AgentStatus();
		status.setLocation(new Point2D(1, 2));
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
		
		assertEquals(status.getLocation(), statusFromJson.getLocation());
		assertEquals(status.getSenderId(), statusFromJson.getSenderId());
		assertEquals(status.getTimestamp().getMillis(), statusFromJson.getTimestamp().getMillis());
		assertEquals(status.getStatistics().getMessagesReceived(), statusFromJson.getStatistics().getMessagesReceived());
		assertEquals(status.getStatistics().getMessagesSent(), statusFromJson.getStatistics().getMessagesSent());
	}
	
}
