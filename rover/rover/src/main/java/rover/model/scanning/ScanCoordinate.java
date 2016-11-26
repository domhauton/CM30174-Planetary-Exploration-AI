package rover.model.scanning;

/**
 * Created by dominic on 26/11/16.
 */
public class ScanCoordinate {
  private final int x;
  private final int y;

  public ScanCoordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ScanCoordinate)) return false;

    ScanCoordinate that = (ScanCoordinate) o;

    return getX() == that.getX() && getY() == that.getY();

  }

  @Override
  public int hashCode() {
    int result = getX();
    result = 31 * result + getY();
    return result;
  }
}
