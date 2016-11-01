package rover.model;

import rover.model.maplocation.Coordinate;
import rover.model.maplocation.ResourceType;
import rover.model.maplocation.CoordinateFactory;

/**
 * Created by dominic on 27/10/16.
 */
public class RoverInfo {
  private final Coordinate coordinate;
  private final ResourceType cargoCapability = ResourceType.SOLID;
  private final Integer payloadSpace = 5; //FIXME Magic Number
  private final Integer payloadCount;

  public RoverInfo() {
    coordinate = CoordinateFactory.buildCartesian(0, 0);
    payloadCount = 0;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public ResourceType getCargoCapability() {
    return cargoCapability;
  }

  public Integer getPayloadCount() {
    return payloadCount;
  }

  public Integer getPayloadSpace() {
    return payloadSpace;
  }
}
