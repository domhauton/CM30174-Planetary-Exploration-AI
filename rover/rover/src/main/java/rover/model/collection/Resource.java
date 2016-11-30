package rover.model.collection;

import rover.mediators.data.update.item.ResourceType;
import rover.model.maplocation.Coordinate;

/**
 * Created by dominic on 31/10/16.
 */
public class Resource {
  private final ResourceType resourceType;
  private final Coordinate coordinate;
  private Integer count;

  public Resource(ResourceType resourceType, Coordinate coordinate) {
    this.resourceType = resourceType;
    this.coordinate = coordinate;
    count = 1;
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public Integer getCount() { return count; }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Double getTotalMoveDistance(Coordinate roverPosition, Integer mapSize) {
    return coordinate.getDistanceTo(roverPosition, mapSize);
  }
}
