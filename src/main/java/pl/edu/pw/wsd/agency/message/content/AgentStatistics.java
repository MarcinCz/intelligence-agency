package pl.edu.pw.wsd.agency.message.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentStatistics {

    @JsonProperty("messages-sent") 
	private int messagesSent;

    @JsonProperty("messages-received") 
	private int messagesReceived;

	public int getMessagesSent() {
		return messagesSent;
	}

	public void setMessagesSent(int messagesSent) {
		this.messagesSent = messagesSent;
	}

	public int getMessagesReceived() {
		return messagesReceived;
	}

	public void setMessagesReceived(int messagesReceived) {
		this.messagesReceived = messagesReceived;
	}
	
	public void incrementMessagesReceived() {
		++messagesReceived;
	}
	
	public void incrementMessagesSent() {
		++messagesSent;
	}
}
