package rover.model.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import rover.mediators.data.update.item.ResourceType;
import rover.model.maplocation.Coordinate;

/**
 * Created by dominic on 31/10/16.
 */
public class Resource {
  private final Logger logger;
  private final ResourceType resourceType;
  private final Coordinate coordinate;
  private Integer count;

  public Resource(ResourceType resourceType, Coordinate coordinate) {
    logger = LoggerFactory.getLogger("AGENT");
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

  public Double getDesirability(Coordinate roverPosition, Integer mapSize, Integer roverMoveEase, Integer roverRemainingPayloadSize) {
    Double moveDesire = 1.0 - (getTotalMoveDistance(roverPosition, mapSize)/ ((double)(mapSize*2)));
    logger.info("Collection move desire is: {}", moveDesire);
    Double collectionDesire = moveDesire * (double) roverMoveEase;
    logger.info("Collection desire 1 is: {}", collectionDesire);
    collectionDesire *= Math.min(roverRemainingPayloadSize, count);
    logger.info("Collection desire 2 is: {}", collectionDesire);
    return collectionDesire;
  }

  public Double getTotalMoveDistance(Coordinate roverPosition, Integer mapSize) {
    Double distance = coordinate.getDistanceTo(roverPosition, mapSize) + coordinate.getDistanceTo(Coordinate.ORIGIN, mapSize);
    logger.info("Total move distance: {}", distance);
    return Math.abs(distance);
  }

  @Override
  public String toString() {
    return "Resource{" +
            "resourceType=" + resourceType +
            ", coordinate=" + coordinate +
            ", count=" + count +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Resource)) return false;
    Resource resource = (Resource) o;
    return getResourceType() == resource.getResourceType() &&
            Objects.equals(getCoordinate(), resource.getCoordinate());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getResourceType(), getCoordinate());
  }
}
