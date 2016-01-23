package pl.edu.pw.wsd.agency.message.content;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("AgentStatus")
public class AgentStatus {

	public static class Location {

		@JsonProperty("x")
	    private double x;
	    
	    @JsonProperty("y")
	    private double y;

		@JsonCreator
	    public Location(@JsonProperty("x") double x, @JsonProperty("y")double y) {
	    	this.x = x;
	    	this.y = y;
	    }
	    
	    public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}
	}

    @JsonProperty("sender-id")
    private String senderId;
    
    @JsonProperty("timestamp")
    private DateTime timestamp;
    
    @JsonProperty("statistics")
    private AgentStatistics statistics;
    
    @JsonProperty("location")
    private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
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
