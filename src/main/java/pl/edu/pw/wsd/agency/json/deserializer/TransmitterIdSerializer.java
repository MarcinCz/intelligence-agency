package pl.edu.pw.wsd.agency.json.deserializer;

/**
 * @author apapros
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.edu.pw.wsd.agency.common.TransmitterId;

import java.io.IOException;

public class  TransmitterIdSerializer extends JsonSerializer<TransmitterId> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void serialize(TransmitterId transmitterId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        String json = objectMapper.writeValueAsString(transmitterId);

        jsonGenerator.writeFieldName(json);

    }
}
