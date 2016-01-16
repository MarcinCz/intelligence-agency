package pl.edu.pw.wsd.agency.message.propagate;

import jade.lang.acl.ACLMessage;

/**
 * Wrapper for message to propagate.
 * It contains ACLMessage and it's mapped content.
 * @author marcin.czerwinski
 *
 * @param <T>
 */
public class MessageToPropagate<T> {

	private T contentObject;
	private ACLMessage aclMessage;
	
	public MessageToPropagate(T contentObject, ACLMessage message) {
		this.contentObject = contentObject;
		this.aclMessage = message;
	}

	public T getContentObject() {
		return contentObject;
	}
	public void setContentObject(T contentObject) {
		this.contentObject = contentObject;
	}
	public ACLMessage getACLMessage() {
		return aclMessage;
	}
	public void setACLMessage(ACLMessage message) {
		this.aclMessage = message;
	}
	
}
