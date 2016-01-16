package pl.edu.pw.wsd.agency.agent.behaviour;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

/**
 * @author apapros
 */
public class UserInputMessageBehaviourTest {

    @Test
    public void should_Name() throws Exception {
        // given

        // when

        // JSON
        // {"message_id":{"client_id":"client_1","message_id":"asd"},"end-client":"ClientAgent_3083","message":"Hello WSD","message-life":1452786321172}

        ClientMessage clientMessage = new ClientMessage(new MessageId("client_1", "asd"), "ClientAgent_3083", "Hello WSD", System.currentTimeMillis());

        ObjectMapper m = new ObjectMapper();
        System.out.println(m.writeValueAsString(clientMessage));

        // then

    }
}