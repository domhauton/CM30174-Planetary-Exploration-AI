package rover.mediators.data.update.item;

/**
 * Created by dominic on 26/10/16.
 */
public class RelativeCoordinates {
  private final double x;
  private final double y;

  public RelativeCoordinates(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}
