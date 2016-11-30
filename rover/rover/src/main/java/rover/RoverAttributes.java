package rover;

import rover.mediators.data.update.item.ResourceType;

/**
 * Created by dominic on 26/10/16.
 */
public enum RoverAttributes {
  DEFAULT(3, 3, 3, ResourceType.SOLID),
  CLOSE_SCAN(2, 7, 0, ResourceType.SOLID),
  FAR_SCAN(4, 5, 0, ResourceType.SOLID);

  private final int maxSpeed, scanRange, maxLoad;
  private final ResourceType cargoType;

  RoverAttributes(int maxSpeed, int scanRange, int maxLoad, ResourceType cargoType) {
    if(maxSpeed + scanRange + maxLoad != 9) {
      throw new IllegalArgumentException("Attributes must add up to 9");
    }
    this.maxSpeed = maxSpeed;
    this.scanRange = scanRange;
    this.maxLoad = maxLoad;
    this.cargoType = cargoType;
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

  public ResourceType getCargoType() {
    return cargoType;
  }
}
