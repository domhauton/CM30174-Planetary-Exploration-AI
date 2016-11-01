package rover.model.maplocation;

/**
 * Created by dominic on 27/10/16.
 */
public class CoordinateFactory {

  public static Coordinate buildPolar(double magnitude, double radians) {
    return new Coordinate(
            Math.cos(radians) * magnitude,
            Math.sin(radians) * magnitude);
  }

  public static Coordinate buildCartesian(double x, double y) {
    return new Coordinate(x, y);
  }
}
