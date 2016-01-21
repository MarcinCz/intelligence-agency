package pl.edu.pw.wsd.agency.json.deserializer;

/**
 * @author apapros
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;

import java.io.IOException;

public class  TransmitterIdSerializer extends JsonSerializer<PhysicalAgentId> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void serialize(PhysicalAgentId physicalAgentId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        String json = objectMapper.writeValueAsString(physicalAgentId);

        jsonGenerator.writeFieldName(json);

    }
}
