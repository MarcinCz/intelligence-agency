package pl.edu.pw.wsd.agency.json.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import jade.core.AID;

/**
 * Partial AID deserializer, should be enough for this project.
 * @author marcin.czerwinski
 *
 */
public class AIDDeserializer extends JsonDeserializer<AID> {

	@Override
	public AID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		AID aid = new AID();
		TreeNode node = p.getCodec().readTree(p);
		aid.setName(((JsonNode) node.get("name")).asText());
		aid.setLocalName(((JsonNode) node.get("localName")).asText());
        ArrayNode addresses = ((ArrayNode) node.get("addressesArray"));
        for (JsonNode jsonNode : addresses) {
			String address = jsonNode.asText();
			aid.addAddresses(address);
		}
		return aid;
	}

}
