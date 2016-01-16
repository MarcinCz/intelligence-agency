package pl.edu.pw.wsd.agency.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jade.core.AID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apapros
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransmitterId {

    @JsonProperty("local_name")
    private String localName;

    public TransmitterId(AID sender) {
        Object clone = sender.clone();
        AID cloned = (AID) clone;
        cloned.clearAllAddresses();
        cloned.clearAllResolvers();
        this.localName = cloned.getLocalName();
    }

    @JsonIgnore
    public AID toAID() {
        return new AID(localName, AID.ISLOCALNAME);
    }

}
