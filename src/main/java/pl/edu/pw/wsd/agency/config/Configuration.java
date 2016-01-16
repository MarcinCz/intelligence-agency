package pl.edu.pw.wsd.agency.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.json.deserializer.Point2dDeserializer;
import pl.edu.pw.wsd.agency.json.deserializer.TransmitterIdDeserializer;
import pl.edu.pw.wsd.agency.json.deserializer.TransmitterIdSerializer;
import pl.edu.pw.wsd.agency.location.PhysicalDeviceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains global configuration.
 *
 * @author Adrian Sidor
 */
public class Configuration {

    private static Configuration instance = null;
    /**
     * Mapper for JSON serialization and deserialization
     */
    private ObjectMapper mapper;

    /**
     * Agents location
     */
    private Map<TransmitterId, PhysicalDeviceLocation> agentsLocation;

    private Configuration() {
        mapper = new ObjectMapper();
        SimpleModule simple = new SimpleModule();
        simple.addDeserializer(Point2D.class, new Point2dDeserializer());
        simple.addKeyDeserializer(TransmitterId.class, new TransmitterIdDeserializer());
        simple.addSerializer(TransmitterId.class, new TransmitterIdSerializer());
        mapper.registerModule(simple);
        mapper.registerModule(new JodaModule());
        agentsLocation = new HashMap<>();
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
            return instance;
        }

        return instance;
    }

    public ObjectMapper getObjectMapper() {
        return mapper;
    }

    /**
     * @return
     */
    public Map<TransmitterId, PhysicalDeviceLocation> getAgentsLocation() {
        return agentsLocation;
    }

    /**
     * Returns Agents Location information without information about Agent it ask for.
     * Agent doesn't need information about its own location.
     * Contains Location of only Transmitter Agents.
     *
     * @param aid
     * @return
     */
    public Map<TransmitterId, PhysicalDeviceLocation> getAgentsLocationWithout(TransmitterId aid) {
        Map<TransmitterId, PhysicalDeviceLocation> agentsLocation = new HashMap<>();
        agentsLocation.putAll(this.agentsLocation);
        agentsLocation.remove(aid);

        return agentsLocation;
    }

    public void updateAgentLocation(TransmitterId aid, PhysicalDeviceLocation location) {
        agentsLocation.put(aid, location);
    }
}
