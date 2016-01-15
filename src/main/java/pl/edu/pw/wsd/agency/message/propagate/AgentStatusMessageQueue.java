package pl.edu.pw.wsd.agency.message.propagate;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import pl.edu.pw.wsd.agency.message.content.AgentStatus;

public class AgentStatusMessageQueue extends MessageToPropagateQueue<AgentStatus> {

	static final int SECONDS_TO_EXPIRE = 60;
	
	public AgentStatusMessageQueue() {
		super(AgentStatus.class);
	}

	@Override
	protected boolean isExpired(AgentStatus contentObject) {
		//messages older than 1 minute are expired (short time because of simulation)
		return Seconds.secondsBetween(contentObject.getTimestamp(), DateTime.now()).getSeconds() > SECONDS_TO_EXPIRE;
	}

	@Override
	protected boolean shouldReplaceObject(AgentStatus currentObject, AgentStatus newObject) {
		boolean sameSenderId = currentObject.getSenderId().equals(newObject.getSenderId());
		boolean newerDate = newObject.getTimestamp().isAfter(currentObject.getTimestamp());
		if(sameSenderId && newerDate) {
			return true;
		}
		
		return false;
	}

}
