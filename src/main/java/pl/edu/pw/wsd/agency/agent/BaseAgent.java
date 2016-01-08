package pl.edu.pw.wsd.agency.agent;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.Agent;
import pl.edu.pw.wsd.agency.config.AgentConfigurationProvider;
import pl.edu.pw.wsd.agency.config.AgentConfigurationProviderImpl;

/**
 * Base class for all agents.
 * @author marcin.czerwinski
 *
 */
public abstract class BaseAgent extends Agent {

    private static final long serialVersionUID = 851946783328690212L;

    protected AgentConfigurationProvider configProvider;

    private static final Logger log = LogManager.getLogger();

	/**
     * Default constructor, called by JADE.
     */
    public BaseAgent() {
    	this.configProvider = new AgentConfigurationProviderImpl();
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
        log.info("Loading configuration.");
        Object[] args = getArguments();
        if (args != null && args.length == 1) {
            String propertiesFileName = (String) args[0];
            try {
                loadConfiguration(propertiesFileName);
            } catch (ConfigurationException e) {
                log.info("Could not load configuration. Agent terminating");
                doDelete();
            }
        } else {
            log.info("No properties file passed to agent. Agent terminating.");
            doDelete();
        }
    }

	protected abstract void loadConfiguration(String propertiesFileName) throws ConfigurationException;
	
}
