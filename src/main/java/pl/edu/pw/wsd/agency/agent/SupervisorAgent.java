package pl.edu.pw.wsd.agency.agent;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import pl.edu.pw.wsd.agency.config.BaseAgentConfiguration;

public class SupervisorAgent extends BaseAgent {

	private static final long serialVersionUID = 8604486033349286351L;

	private static final Logger log = LogManager.getLogger();
	
	public SupervisorAgent(String propertiesFileName) {
		super(propertiesFileName);
	}
	
	@Override
    protected void setup() {
        super.setup();
        registerAgent();
        
        // TODO: add behaviours
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Registers Agent in DF Agent's YellowPages.
     */
    private void registerAgent() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("supervisor");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
            log.error("Could not register agent. Agent terminating");
            doDelete();
        }
    }

	@Override
	protected void loadConfiguration(String propertiesFileName) throws ConfigurationException {
		BaseAgentConfiguration cfg = configProvider.getBaseAgentConfiguration(propertiesFileName);
	}
}
