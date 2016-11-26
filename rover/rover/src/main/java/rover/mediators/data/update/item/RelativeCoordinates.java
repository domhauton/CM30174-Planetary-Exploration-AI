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

  @Override
  public String toString() {
    return "RelativeCoordinates{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RelativeCoordinates)) return false;

    RelativeCoordinates that = (RelativeCoordinates) o;

    if (Double.compare(that.getX(), getX()) != 0) return false;
    return Double.compare(that.getY(), getY()) == 0;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(getX());
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(getY());
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
