package pl.edu.pw.wsd.agency.agent.behaviour;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.config.Configuration;

public class AgentsLocationServiceBehaviour extends Behaviour {

    private static final long serialVersionUID = 6320047876997878496L;

    private static final Logger log = LogManager.getLogger();

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId("Agents-Location");
        log.trace("Czekam na wiadomosc.");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            LocationRegistryAgent agent = (LocationRegistryAgent) getAgent();
            if (msg.getPerformative() == ACLMessage.REQUEST) {
                ACLMessage reply = msg.createReply();
                HashMap<AID, java.awt.geom.Point2D> agentsLocationWithout = agent.getAgentsLocationWithout(msg.getSender());
                try {
                    reply.setContentObject(agentsLocationWithout);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                agent.send(reply);
            } else if (msg.getPerformative() == ACLMessage.INFORM) {
                String content = msg.getContent();
                ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
                try {
                    Point2D position = mapper.readValue(content, Point2D.class);
                    AID sender = msg.getSender();
                    agent.updateAgentLocation(sender, position);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

}
