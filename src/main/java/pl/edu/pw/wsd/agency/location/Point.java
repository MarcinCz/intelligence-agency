package pl.edu.pw.wsd.agency.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Class that represents point.
 * Independent form any presentation-level point class.
 *
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
@Getter
@Setter
public class Point {

    @JsonProperty
    private double x;
    @JsonProperty
    private double y;
    @JsonProperty("signal_range")
    private Double signalRange;

    @JsonProperty("message_id_list")
    private Set<String> messageIdList;

    public Point() {

    }

    // FIXME :: na chwię, w ogóle ten Point2D trzeba wyrzucic
    public Point(Point2D point, Double signalRange) {
        this.x = point.getX();
        this.y = point.getY();
        this.signalRange = signalRange;
    }

    public Point(double x, double y, Double signalRange) {
        this.x = x;
        this.y = y;
        this.signalRange = signalRange;
    }

    public static MessageId createMessageId(String clientId, String messageId) {
        return new MessageId(clientId, messageId);
    }


}
