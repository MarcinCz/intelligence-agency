package pl.edu.pw.wsd.agency.message.envelope;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * Enum for FIPA envelope parameter <b>conversation-id</b>
 * @author marcin.czerwinski
 *
 */
public enum ConversationId {
	
	CLIENT_MESSAGE,
	AGENT_STATUS,
	UNKOWN
	;
	
	/**
	 * Generate random id for this conversation type
	 */
	public String generateId() {
		return name() + "_" + UUID.randomUUID().toString();
	}
	
	public static ConversationId resolveConversationType(String conversationId) {
		if(StringUtils.isBlank(conversationId)) {
			return ConversationId.UNKOWN;
		}
		
		for(ConversationId type: ConversationId.values()) {
			if(conversationId.startsWith(type.name())) {
				return type;
			}
		}
		
		return ConversationId.UNKOWN;
	}
}
