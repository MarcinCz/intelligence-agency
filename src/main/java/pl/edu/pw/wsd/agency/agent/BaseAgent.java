package pl.edu.pw.wsd.agency.agent;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.Agent;

public class BaseAgent extends Agent {

    private static final long serialVersionUID = 851946783328690212L;
    private static final Logger LOGGER = LogManager.getLogger();
    protected Configuration CFG;
    protected float posx;
    protected float posy;
    
    public float getPosx() {
        return posx;
    }

    public void setPosx(float posx) {
        this.posx = posx;
    }

    public float getPosy() {
        return posy;
    }

    public void setPosy(float posy) {
        this.posy = posy;
    }

    @Override
    protected void setup() {
        LOGGER.info("Agent starting.");
        LOGGER.info("Loading configuration.");
        Object[] args = getArguments();
        if(args != null && args.length == 1) {
            String propertiesFileName = (String) args[0];
            try {
                loadConfiguration(propertiesFileName);
            } catch (ConfigurationException e) {
                LOGGER.info("Could not load configuration. Agent terminating");
                doDelete();
            }
        } else {
            LOGGER.info("No properties file passed to agent. Agent terminating.");
            doDelete();
        }
        //pozycja startowa agenta
        posx = Float.valueOf(CFG.getString("posx"));
        posy = Float.valueOf(CFG.getString("posy"));
        LOGGER.info("Agent starting position. X:" + posx + " Y:" + posy);
    }
    
    private void loadConfiguration(String propertiesFielName) throws ConfigurationException {
        CFG = new PropertiesConfiguration(propertiesFielName);
    }
    
}
