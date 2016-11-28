package rover.model.maplocation;

/**
 * Created by dominic on 27/10/16.
 *
 * Coordinate with fuzzy compare.
 */
public class Coordinate {
  private static double ALLOWED_ERROR = Math.pow(10, -8);

  private final double x;
  private final double y;

  public Coordinate(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public Coordinate moveCoordinate(double xOffset, double yOffset, double xSize, double ySize) {
    double newX = (x + xOffset) % xSize;
    double newY = (y + yOffset) % ySize;
    while(newX < 0 ) {
      newX += xSize;
    }
    while(newY < 0 ) {
      newY += ySize;
    }
    return new Coordinate(newX, newY);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Coordinate)) return false;

    Coordinate that = (Coordinate) o;

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

  boolean roughEquals(double a, double b) {
    return a - b < ALLOWED_ERROR;
  }

  @Override
  public String toString() {
    return "Coordinate{" +
            "x=" + getX() +
            ", y=" + getY() +
            '}';
  }
}
