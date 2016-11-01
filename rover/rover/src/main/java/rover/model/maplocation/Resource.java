package rover.model.maplocation;

/**
 * Created by dominic on 31/10/16.
 */
public class Resource {
  private final ResourceType resourceType;
  private final Coordinate coordinate;

  public Resource(ResourceType resourceType, Coordinate coordinate) {
    this.resourceType = resourceType;
    this.coordinate = coordinate;
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }
}
