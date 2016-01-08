package pl.edu.pw.wsd.agency.agent;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.config.AgentConfigurationProvider;
import pl.edu.pw.wsd.agency.config.AgentConfigurationProviderImpl;
import pl.edu.pw.wsd.agency.message.content.AgentStatistics;

/**
 * Base class for all agents.
 * @author marcin.czerwinski
 *
 */
public abstract class BaseAgent extends Agent {

    private static final long serialVersionUID = 851946783328690212L;

    protected AgentConfigurationProvider configProvider;
    private AgentStatistics statistics;
    private String propertiesFileName;

	private static final Logger log = LogManager.getLogger();

	/**
     * Default constructor, called by JADE.
     */
    public BaseAgent(String propertiesFileName) {
    	this(new AgentConfigurationProviderImpl());
    	
    	this.statistics = new AgentStatistics();
    	this.propertiesFileName = propertiesFileName;
	}
    
    /**
     * Use this constructor if you want to create Agent from code and set non-default configuration provider.
     * Useful for tests.
     * @param configProvider
     */
    public BaseAgent(AgentConfigurationProvider configProvider) {
    	this.configProvider = configProvider;
    }
    
    @Override
    protected void setup() {
        log.info("Agent starting.");
        log.info("Loading configuration: " + propertiesFileName);
        try {
            loadConfiguration(propertiesFileName);
        } catch (ConfigurationException e) {
            log.info("Could not load configuration. Agent terminating");
            doDelete();
        }
    }
    
    public AgentStatistics getAgentStatistics() {
		return statistics;
	}
    
    public ACLMessage receiveAndUpdateStatistics() {
    	ACLMessage message = super.receive();
    	if(message != null) {
    		statistics.incrementMessagesReceived();
    	}
    	return message;
    }
    
    public void sendAndUpdateStatistics(ACLMessage message) {
    	super.send(message);
    	statistics.incrementMessagesSent();
    }

	protected abstract void loadConfiguration(String propertiesFileName) throws ConfigurationException;
	
}
