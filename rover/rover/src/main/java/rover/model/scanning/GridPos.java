package rover.model.scanning;

/**
 * Created by dominic on 26/11/16.
 */
public class GridPos {
  private final int x;
  private final int y;

  public GridPos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public double distanceToPos(final GridPos that, final int mapSize) {
    int halfMap = mapSize/2;
    int diffX = Math.abs(this.getX() - that.getX());
    int diffY = Math.abs(this.getY() - that.getY());
    diffX = diffX > halfMap ? mapSize - diffX : diffX;
    diffY = diffY > halfMap ? mapSize - diffY : diffY;
    return Math.sqrt((diffX * diffX) + (diffY * diffY));
  }

  public double distanceToOrigin(final int mapSize) {
    int halfMap = mapSize/2;
    int moveX = x > halfMap ? mapSize - x : x;
    int moveY = y > halfMap ? mapSize - y : y;
    return Math.sqrt((moveX*moveX) + (moveY*moveY));
  }

  @Override
  public String toString() {
    return "Pos{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GridPos)) return false;

    GridPos that = (GridPos) o;

    return getX() == that.getX() && getY() == that.getY();

  }

  @Override
  public int hashCode() {
    int result = getX();
    result = 31 * result + getY();
    return result;
  }
}
