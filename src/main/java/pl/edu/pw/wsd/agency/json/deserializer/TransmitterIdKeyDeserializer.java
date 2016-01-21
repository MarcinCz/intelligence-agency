package pl.edu.pw.wsd.agency.json.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;

import java.io.IOException;

/**
 * @author apapros
 */
public class TransmitterIdKeyDeserializer extends KeyDeserializer {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        PhysicalAgentId physicalAgentId = objectMapper.readValue(s, PhysicalAgentId.class);
        return physicalAgentId;
    }



}
