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
public class PhysicalAgentId {

	@JsonProperty("local_name")
	private String localName;

	@JsonProperty("is_client")
	private boolean isClient;


	public PhysicalAgentId(AID aid) {
		Object clone = aid.clone();
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
