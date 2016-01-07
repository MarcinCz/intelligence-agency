package pl.edu.pw.wsd.agency.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

import javafx.geometry.Point2D;

/**
 * Deserialize JSON to {@link Point2D} Object
 * @author Adrian Sidor
 *
 */
public class Point2dDeserializer extends JsonDeserializer<Point2D> {

    @Override
    public Point2D deserialize(JsonParser jp, DeserializationContext _any) throws IOException {
        TreeNode node = jp.getCodec().readTree(jp);
        double x = Double.valueOf(node.get("x").toString());
        double y = Double.valueOf(node.get("y").toString());
        return new Point2D(x, y);
    }
}
