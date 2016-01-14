package pl.edu.pw.wsd.agency.message.propagate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.config.Configuration;

/**
 * Stores messages to propagate.
 * It ensures that every stored message has unique message id.
 * 
 * @param T message content object class
 * @author marcin.czerwinski
 *
 */
public abstract class MessageToPropagateQueue<T> {

    private static final Logger log = LogManager.getLogger();

	private final Class<T> type;
	private List<MessageToPropagate<T>> messages = new ArrayList<>();

	public MessageToPropagateQueue(Class<T> type) {
		this.type = type;
	}
	
	public void queueMessage(ACLMessage msg) {
		T contentObject = readObjectFromContent(msg);
		if(isExpired(contentObject)) {
			return;
		}
		
		if(contentObject != null && checkIfUnique(msg)) {
			messages.add(new MessageToPropagate<T>(contentObject, msg));
		}
	}
	
	/**
	 * Return queued messages as list.
	 * Expired messages are not returned.
	 * @return
	 */
	public List<MessageToPropagate<T>> getQueuedMessages() {
		removeExpiredMessages();
		return messages;
	}
	
	private void removeExpiredMessages() {
		Iterator<MessageToPropagate<T>> iterator = messages.iterator();
		while(iterator.hasNext()) {
			if(isExpired(iterator.next().getContentObject())) {
				iterator.remove();
			}
		}
	}
	
	protected abstract boolean isExpired(T contentObject);
	
	private boolean checkIfUnique(ACLMessage msg) {
		for (MessageToPropagate<T> queuedMsg : messages) {
			if(queuedMsg.getACLMessage().getConversationId().equals(msg.getConversationId())) {
				return false;
			} else {
				AID sender = queuedMsg.getACLMessage().getSender();
				AID sender2 = msg.getSender();
				if (sender != null && sender.getLocalName().equals(sender2.getLocalName())) {
					return false;
				}
			}
		}
		return true;
	}
	
	private T readObjectFromContent(ACLMessage msg) {
		try {
			return Configuration.getInstance().getObjectMapper().readValue(msg.getContent(), type);
		} catch (IOException e) {
		   	log.error("Could not read object from content [" + msg.getContent() + "]", e);
		}
		return null;
	}
}
