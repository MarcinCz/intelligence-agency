package pl.edu.pw.wsd.agency.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import jade.core.AID;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.json.deserializer.Point2dDeserializer;
import pl.edu.pw.wsd.agency.location.PhisicalDeviceLocation;

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
    private Map<AID, PhisicalDeviceLocation> agentsLocation;

    private Configuration() {
        mapper = new ObjectMapper();
        SimpleModule simple = new SimpleModule();
        simple.addDeserializer(Point2D.class, new Point2dDeserializer());
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
    public Map<AID, PhisicalDeviceLocation> getAgentsLocation() {
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
    public Map<AID, PhisicalDeviceLocation> getAgentsLocationWithout(AID aid) {
        Map<AID, PhisicalDeviceLocation> agentsLocation = new HashMap<>();
        agentsLocation.putAll(this.agentsLocation);
        agentsLocation.remove(aid);

        return agentsLocation;
    }

    public void updateAgentLocation(AID aid, PhisicalDeviceLocation location) {
        agentsLocation.put(aid, location);
    }
}
