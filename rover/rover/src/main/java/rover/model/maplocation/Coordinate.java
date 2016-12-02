package rover.model.maplocation;

/**
 * Created by dominic on 27/10/16.
 *
 * Coordinate with fuzzy compare.
 */
public class Coordinate {
  private static double ALLOWED_ERROR = Math.pow(10, -8);
  public static Coordinate ORIGIN = new Coordinate(0, 0);

  private final double x;
  private final double y;

  public Coordinate(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Coordinate(double x, double y, int mapSize) {
    this(x, y);
    moveCoordinate(0, 0, mapSize);
  }

  public Double getX() {
    return x;
  }

  public Double getY() {
    return y;
  }

  public Coordinate moveCoordinate(double xOffset, double yOffset, double size) {
    double newX = (x + xOffset) % size;
    double newY = (y + yOffset) % size;
    while(newX < 0 ) {
      newX += size;
    }
    while(newY < 0 ) {
      newY += size;
    }
    return new Coordinate(newX, newY);
  }

  public Double getDistanceTo(Coordinate coordinate, Integer mapSize) {
    double xDist = distanceToMove(x, coordinate.getX(), mapSize);
    double yDist = distanceToMove(y, coordinate.getY(), mapSize);
    return Math.sqrt((xDist*xDist)+(yDist*yDist));
  }

  public static double distanceToMove(double oldPosition, double targetPosition, double mapSize) {
    double halfMapSize = mapSize/2;
    double naiveMove = targetPosition-oldPosition;
    if(naiveMove > halfMapSize) naiveMove -= mapSize;
    if(naiveMove < -halfMapSize) naiveMove += mapSize;
    return naiveMove;
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
