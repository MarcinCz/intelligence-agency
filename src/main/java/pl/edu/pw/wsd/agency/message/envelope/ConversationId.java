package pl.edu.pw.wsd.agency.message.envelope;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * Enum for FIPA envelope parameter <b>conversation-id</b>
 *
 * @author marcin.czerwinski
 */
public enum ConversationId {

	CLIENT_MESSAGE,
	/**
	 * Conversation between client/transmitter and transmitter.
	 * Transmitter is asked to propagate agent status so it can be delivered to supervisor.
	 */
	PROPAGATE_AGENT_STATUS,
	/**
	 * Conversation between supervisor and transmitter.
	 * Supervisor asks for agent status stored by transmitter.
	 */
	DELIVER_AGENT_STATUSES,

	// FIXME HAVE TO BE REMOVED
	DELIVER_CLIENT_MESSAGE,

	/**
	 * Request send by transmitter to client that is in range.
	 * This request means: "Dear Client, please, give me your messages. I'll handle them!"
	 */
	CLIENT_MESSAGE_REQUEST,

	UNKOWN;

	/**
	 * Generate random id for this conversation type
	 */
	public String generateId() {
		return name() + "_" + UUID.randomUUID().toString();
	}

	public static ConversationId resolveConversationType(String conversationId) {
		if (StringUtils.isBlank(conversationId)) {
			return ConversationId.UNKOWN;
		}

		for (ConversationId type : ConversationId.values()) {
			if (conversationId.startsWith(type.name())) {
				return type;
			}
		}

		return ConversationId.UNKOWN;
	}
}
