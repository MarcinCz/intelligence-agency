package pl.edu.pw.wsd.agency.message.content;

import jade.core.AID;

import java.util.Map;

import javafx.geometry.Point2D;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentsLocationMessage {

    @JsonProperty("agents-placement") 
    private Map<AID, Point2D> agentsLocation;
    
    @JsonCreator
    public AgentsLocationMessage(@JsonProperty("agents-placement") Map<AID, Point2D> agentsLocation) {
        this.agentsLocation = agentsLocation;
    }
    
    public Map<AID, Point2D> getAgentsLocation() {
        return agentsLocation;
    }
    
    public void setAgentsLocation(Map<AID, Point2D> agentsLocation) {
        this.agentsLocation = agentsLocation;
    }

}
