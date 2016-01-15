package pl.edu.pw.wsd.agency.message.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pw.wsd.agency.location.PhisicalDeviceLocation;

import java.util.Map;

public class AgentsLocationMessage {

    @JsonProperty("agents-placement")
    @Getter
    @Setter
    private Map<AID, PhisicalDeviceLocation> agentsLocation;

    @JsonCreator
    public AgentsLocationMessage(@JsonProperty("agents-placement") Map<AID, PhisicalDeviceLocation> agentsLocation) {
        this.agentsLocation = agentsLocation;
    }

}
