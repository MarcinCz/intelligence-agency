package pl.edu.pw.wsd.agency.location.message.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apapros
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRegistryData {

	@JsonProperty
	private double x;
	@JsonProperty
	private double y;
	@JsonProperty("signal_range")
	private double signalRange;
	@JsonProperty("is_client")
	private boolean isClient;


	public double distance(double x1, double y1) {
		double a = getX() - x1;
		double b = getY() - y1;
		return Math.sqrt(a * a + b * b);
	}

	public double distance(LocationRegistryData other) {
		return distance(other.getX(), other.getY());
	}

}
