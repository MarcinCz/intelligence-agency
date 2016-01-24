package pl.edu.pw.wsd.agency.message.propagate;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import pl.edu.pw.wsd.agency.message.content.AgentCertificate;

public class AgentCertificateMessageQueue extends MessageToPropagateQueue<AgentCertificate> {

	static final int SECONDS_TO_EXPIRE = 60;
	
	public AgentCertificateMessageQueue() {
		super(AgentCertificate.class);
	}

	@Override
	protected boolean isExpired(AgentCertificate contentObject) {
		//messages older than 1 minute are expired (short time because of simulation)
		return Seconds.secondsBetween(contentObject.getTimestamp(), DateTime.now()).getSeconds() > SECONDS_TO_EXPIRE;
	}

	@Override
	protected boolean shouldReplaceObject(AgentCertificate currentObject, AgentCertificate newObject) {
		boolean sameAgentId = currentObject.getAgentId().equals(newObject.getAgentId());
		boolean newerDate = newObject.getTimestamp().isAfter(currentObject.getTimestamp());
		if(sameAgentId && newerDate) {
			return true;
		}
		
		return false;
	}

}
