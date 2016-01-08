package pl.edu.pw.wsd.agency.message.content;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javafx.geometry.Point2D;

@JsonRootName("AgentStatus")
public class AgentStatus {

    @JsonProperty("sender-id")
    private String senderId;
    
    @JsonProperty("timestamp")
    private DateTime timestamp;
    
    @JsonProperty("statistics")
    private AgentStatistics statistics;
    
    @JsonProperty("location") 
	private Point2D location;

	public Point2D getLocation() {
		return location;
	}

	public void setLocation(Point2D location) {
		this.location = location;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public AgentStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(AgentStatistics statistics) {
		this.statistics = statistics;
	}

}
