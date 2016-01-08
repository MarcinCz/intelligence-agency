package pl.edu.pw.wsd.agency.agent.behaviour;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Point2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.json.deserializer.Point2dDeserializer;
import pl.edu.pw.wsd.agency.message.content.AgentsLocationMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentsLocationServiceBehaviour extends Behaviour {

    private static final long serialVersionUID = 6320047876997878496L;

    private static final Logger log = LogManager.getLogger();

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId("Agents-Location");
        log.info("Czekam na wiadomosc.");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            LocationRegistryAgent agent = (LocationRegistryAgent) getAgent();
            if (msg.getPerformative() == ACLMessage.REQUEST) {
                ACLMessage reply = msg.createReply();
                ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
                AgentsLocationMessage alm = new AgentsLocationMessage(agent.getAgentsLocationWithout(msg.getSender()));
                try {
                    String content = mapper.writeValueAsString(alm);
                    reply.setContent(content);
                } catch (JsonProcessingException e) {
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
