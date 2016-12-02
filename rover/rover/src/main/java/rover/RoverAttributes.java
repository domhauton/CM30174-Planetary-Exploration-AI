package rover;

import rover.mediators.data.update.item.ResourceType;

/**
 * Created by dominic on 26/10/16.
 */
public enum RoverAttributes {
  DEFAULT(3, 3, 3, ResourceType.SOLID),
  MEDIUM_RANGE_SCAN_COLLECT_S(3, 5, 1, ResourceType.SOLID),
  MEDIUM_RANGE_COLLECTOR_S(4, 0, 5, ResourceType.SOLID),
  MEDIUM_RANGE_COLLECTOR_L(4, 0, 5, ResourceType.LIQUID),
  CLOSE_RANGE_SCANNER_COLLECTOR_L(2, 5, 2, ResourceType.LIQUID),
  CLOSE_RANGE_SCANNER_COLLECTOR_S(2, 5, 2, ResourceType.SOLID),
  MEDIUM_RANGE_SCANNER(4, 5, 0, ResourceType.SOLID),
  CLOSE_RANGE_SCANNER(2, 7, 0, ResourceType.SOLID),
  CLOSE_RANGE_COLLECTOR_S(2, 0, 7, ResourceType.SOLID),
  CLOSE_RANGE_COLLECTOR_L(2, 0, 7, ResourceType.LIQUID);

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
