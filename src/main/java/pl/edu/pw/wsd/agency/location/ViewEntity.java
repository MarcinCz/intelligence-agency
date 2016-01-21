package pl.edu.pw.wsd.agency.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pw.wsd.agency.location.message.content.LocationRegistryData;

import java.util.Set;

/**
 * Class that represents point.
 * Independent form any presentation-level point class.
 *
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
@Getter
@Setter
public class ViewEntity {

    @JsonProperty
    private double x;

    @JsonProperty
    private double y;

    @JsonProperty("signal_range")
    private Double signalRange;

    @JsonProperty("message_id_list")
    private Set<MessageId> messageIdList;

    @JsonProperty
    private boolean isClient;

    public ViewEntity() {

    }

    // FIXME :: na chwię, w ogóle ten Point2D trzeba wyrzucic
    public ViewEntity(Point2D point, Double signalRange) {
        this(point.getX(), point.getY(), signalRange);
    }

    public ViewEntity(LocationRegistryData l) {
        this(l.getX(), l.getY(), l.getSignalRange());
    }


    public ViewEntity(double x, double y, Double signalRange) {
        this.x = x;
        this.y = y;
        this.signalRange = signalRange;
        // FIXME :: dirty hack
        isClient = signalRange != 0;
    }

    public static MessageId createMessageId(String clientId, String messageId) {
        return new MessageId(clientId, messageId);
    }
}
