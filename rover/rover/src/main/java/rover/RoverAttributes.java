package rover;

/**
 * Created by dominic on 26/10/16.
 */
public enum RoverAttributes {
  DEFAULT(3, 3, 3),
  FAST(5, 2, 2),
  SLOW(1, 4, 4);

  private final int maxSpeed, scanRange, maxLoad;

  RoverAttributes(int maxSpeed, int scanRange, int maxLoad) {
    if(maxSpeed + scanRange + maxLoad != 9) {
      throw new IllegalArgumentException("Attributes must add up to 9");
    }
    this.maxSpeed = maxSpeed;
    this.scanRange = scanRange;
    this.maxLoad = maxLoad;
  }

  public int getMaxSpeed() {
    return maxSpeed;
  }

  public int getScanRange() {
    return scanRange;
  }

  public int getMaxLoad() {
    return maxLoad;
  }
}
