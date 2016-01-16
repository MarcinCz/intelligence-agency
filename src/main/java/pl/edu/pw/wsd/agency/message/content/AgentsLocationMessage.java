package pl.edu.pw.wsd.agency.message.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.json.deserializer.TransmitterIdKeyDeserializer;
import pl.edu.pw.wsd.agency.json.deserializer.TransmitterIdSerializer;
import pl.edu.pw.wsd.agency.location.PhysicalDeviceLocation;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY, property = "clazz")
public class AgentsLocationMessage {

    @JsonProperty("agents_placement")
    @Getter
    @Setter
    @JsonDeserialize(keyUsing = TransmitterIdKeyDeserializer.class)
    @JsonSerialize(keyUsing = TransmitterIdSerializer.class)
//    @JsonSerialize(keyUsing = Tra)
    private Map<TransmitterId, PhysicalDeviceLocation> agentsLocation;

    @JsonCreator
    public AgentsLocationMessage(@JsonProperty("agents_placement") Map<TransmitterId, PhysicalDeviceLocation> agentsLocation) {
        this.agentsLocation = agentsLocation;
    }

}
