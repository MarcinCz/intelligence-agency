package pl.edu.pw.wsd.agency.message.content;

import java.security.PublicKey;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@JsonRootName("AgentCertificate")
@Data
public class AgentCertificate {

	@JsonProperty("public-key")
	private PublicKey publicKey;
	
	@JsonProperty("agent-id")
	private String agentId;
	
	@JsonProperty("timestamp")
	private DateTime timestamp;
}
