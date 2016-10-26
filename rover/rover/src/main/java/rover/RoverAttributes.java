package rover;

/**
 * Created by dominic on 26/10/16.
 */
public enum RoverAttributes {
  DEFAULT(3, 3, 3);

  private final int speed, scan, range;

  RoverAttributes(int speed, int scan, int range) {
    if(speed + scan + range != 9) {
      throw new IllegalArgumentException("Attributes must add up to 9");
    }
    this.speed = speed;
    this.scan = scan;
    this.range = range;
  }

  public int getSpeed() {
    return speed;
  }

  public int getScan() {
    return scan;
  }

  public int getRange() {
    return range;
  }
}
