package rover.model.maplocation;

import com.google.common.base.Objects;

/**
 * Created by dominic on 27/10/16.
 *
 * Coordinate with fuzzy compare.
 */
public class Coordinate {
  private static double ALLOWED_ERROR = Math.pow(10, -8);

  private final double x;
  private final double y;

  Coordinate(double x, double y) {
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Coordinate)) return false;
    Coordinate that = (Coordinate) o;
    return roughEquals(getX(), that.getX()) &&
            roughEquals(getY(), that.getY());
  }

  boolean roughEquals(double a, double b) {
    return a - b < ALLOWED_ERROR;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getX(), getY());
  }

  @Override
  public String toString() {
    return "Coordinate{" +
            "x=" + getX() +
            ", y=" + getY() +
            '}';
  }
}
