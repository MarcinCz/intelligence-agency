package pl.edu.pw.wsd.agency.message.propagate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		
		MessageToPropagate<T> acquiredMsg = new MessageToPropagate<T>(contentObject, msg);
		if(contentObject != null && checkIfUnique(acquiredMsg)) {
			removeMessageToBeReplaced(acquiredMsg);
			messages.add(acquiredMsg);
		}
	}
	
	private void removeMessageToBeReplaced(MessageToPropagate<T> acquiredMsg) {
		Iterator<MessageToPropagate<T>> iterator = messages.iterator();
		while(iterator.hasNext()) {
			MessageToPropagate<T> msg = iterator.next();
			if(shouldReplaceObject(msg.getContentObject(), acquiredMsg.getContentObject())) {
				iterator.remove();
				return;
			}
		}
	}

	/**
	 * Return queued messages as list.
	 * Expired messages are not returned.
	 * @return
	 */
	public List<MessageToPropagate<T>> getQueuedMessages() {
		removeExpiredMessages();
		return new ArrayList<>(messages);
	}
	
	public void remove(MessageToPropagate<T> msg) {
		messages.remove(msg);
	}
	
	private void removeExpiredMessages() {
		Iterator<MessageToPropagate<T>> iterator = messages.iterator();
		while(iterator.hasNext()) {
			if(isExpired(iterator.next().getContentObject())) {
				iterator.remove();
			}
		}
	}
	
	private boolean checkIfUnique(MessageToPropagate<T> acquiredMsg) {
		for (MessageToPropagate<T> queuedMsg : messages) {
			boolean sameConversationID = queuedMsg.getACLMessage().getConversationId().equals(acquiredMsg.getACLMessage().getConversationId());
			if(sameConversationID) {
				return false;
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
	
	protected abstract boolean isExpired(T contentObject);
	
	/**
	 * Compares new object with currently queued object to check if the new one should be stored.
	 * May be helpful if for example we don't want to store to objects with the same attribute set in content object.
	 * @retun true if the new object should be ignored
	 */
	protected abstract boolean shouldReplaceObject(T currentObject, T newObject);
	
}
