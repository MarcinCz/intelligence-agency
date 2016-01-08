package pl.edu.pw.wsd.agency.config;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import jade.core.AID;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.json.deserializer.Point2dDeserializer;

/**
 * Contains global configuration.
 * @author Adrian Sidor
 *
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
    private Map<AID, Point2D> agentsLocation;
    
    private Configuration() {
        mapper = new ObjectMapper();
        SimpleModule simple = new SimpleModule();
        simple.addDeserializer(Point2D.class, new Point2dDeserializer());
        mapper.registerModule(simple);
        mapper.registerModule(new JodaModule());
        agentsLocation = new HashMap<AID, Point2D>();
    }
    
    public static Configuration getInstance() {
        if(instance == null) {
            instance = new Configuration();
            return instance;
        }
        
        return instance;
    }
    
    public ObjectMapper getObjectMapper() {
        return mapper;
    }
    
    /**
     * Contains Location of only Transmitter Agents.
     * @return
     */
    public Map<AID, Point2D> getAgentsLocation() {
        return agentsLocation;
    }
    
    /**
     * Returns Agents Location information without information about Agent it ask for.
     * Agent doesn't need information about its own location.
     * Contains Location of only Transmitter Agents.
     * @param aid
     * @return
     */
    public Map<AID, Point2D> getAgentsLocationWithout(AID aid) {
        Map<AID, Point2D> agentsLocation = new HashMap<AID, Point2D>();
        agentsLocation.putAll(getAgentsLocation());
        agentsLocation.remove(aid);
        
        return agentsLocation;
    }
 
    public void updateAgentLocation(AID aid, Point2D location) {
        agentsLocation.put(aid, location);
    }
}
