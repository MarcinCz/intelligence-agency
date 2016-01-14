package pl.edu.pw.wsd.agency.message.propagate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;

public class AgentStatusMessageQueueTest {

	@Test
	public void shouldQueueMessages() throws JsonProcessingException {
		AgentStatusMessageQueue queue = new AgentStatusMessageQueue();
		
		String converstationId = "converstationId";
		DateTime now = DateTime.now();
		queue.queueMessage(createTestMessage(converstationId, now));
		List<MessageToPropagate<AgentStatus>> queuedMessages = queue.getQueuedMessages();
		assertEquals(1, queuedMessages.size());
		assertTrue(now.compareTo(queuedMessages.get(0).getContentObject().getTimestamp()) == 0);
		assertEquals(converstationId, queuedMessages.get(0).getACLMessage().getConversationId());
		
		queue.queueMessage(createTestMessage("id", DateTime.now()));
		assertEquals(2, queuedMessages.size());
	}
	
	@Test
	public void shouldNotAddMessagesWithTheSameConversationId() throws JsonProcessingException {
		AgentStatusMessageQueue queue = new AgentStatusMessageQueue();
		
		String converstationId = "converstationId";
		queue.queueMessage(createTestMessage(converstationId, DateTime.now()));
		assertEquals(1, queue.getQueuedMessages().size());
		
		queue.queueMessage(createTestMessage(converstationId, DateTime.now()));
		assertEquals(1, queue.getQueuedMessages().size());
	}
	
	@Test
	public void shoudRemoveExpiredMessages() throws JsonProcessingException {
		AgentStatusMessageQueue queue = new AgentStatusMessageQueue();
		
		String converstationId = "converstationId";
		queue.queueMessage(createTestMessage(converstationId, DateTime.now().minusSeconds(AgentStatusMessageQueue.SECONDS_TO_EXPIRE + 1)));
		assertEquals(0, queue.getQueuedMessages().size());
	}
	
	private ACLMessage createTestMessage(String converstationId, DateTime timeStamp) throws JsonProcessingException {
		AgentStatus status = new AgentStatus();
		status.setTimestamp(timeStamp);
		
		ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
		msg.setContent(Configuration.getInstance().getObjectMapper().writeValueAsString(status));
		msg.setConversationId(converstationId);
		return msg;
	}

}
