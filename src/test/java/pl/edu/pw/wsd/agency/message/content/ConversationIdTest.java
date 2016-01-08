package pl.edu.pw.wsd.agency.message.content;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.pw.wsd.agency.message.envelope.ConversationId;

public class ConversationIdTest {

	@Test
	public void shouldResolveConversationId() {
		String clientMessageConversationId = ConversationId.CLIENT_MESSAGE.generateId();
		assertEquals(ConversationId.CLIENT_MESSAGE, ConversationId.resolveConversationType(clientMessageConversationId));
		
		String unkownId = "fjjdfksldfkalsdjf";
		assertEquals(ConversationId.UNKOWN, ConversationId.resolveConversationType(unkownId));
		
		String nullId = null;
		assertEquals(ConversationId.UNKOWN, ConversationId.resolveConversationType(nullId));
	}

}
