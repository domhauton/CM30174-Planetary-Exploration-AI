package rover.model.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import rover.mediators.data.update.item.ResourceType;
import rover.model.maplocation.Coordinate;
import util.Pair;

/**
 * Created by dominic on 31/10/16.
 */
public class ItemManager {
  private Logger logger;
  private Collection<Resource> currentResources;
  private Collection<Resource> plannedResources;
  private int totalItemsCollected;

  public ItemManager() {
    logger = LoggerFactory.getLogger("AGENT");
    currentResources = new LinkedList<>();
    totalItemsCollected = 0;
    revertPlannedCollections();
  }

  public void revertPlannedCollections() {
    plannedResources = new LinkedList<>();
    currentResources.stream()
            .map(Resource::clone)
            .forEach(plannedResources::add);
    logger.info("Reverted planned collections");
  }

  public Optional<Resource> getMostDesirable(Integer movementDesire,
                                             Integer remainingCargoCapacity,
                                             Coordinate roverPosition,
                                             Integer mapSize,
                                             ResourceType roverResourceCapability) {
    return plannedResources.stream()
            .filter(resource -> resource.getResourceType() == roverResourceCapability)
            .filter(resource -> resource.getCount() > 0)
            .map(resource -> new Pair<>(resource, resource.getDesirability(roverPosition, mapSize, movementDesire, remainingCargoCapacity)))
            .filter(pair -> pair.getB() > 0)
            .sorted((o1, o2) -> -o1.getB().compareTo(o2.getB()))
            .map(Pair::getA)
            .findFirst();
  }

  public void itemCollectPlanned(Coordinate coordinate) {
    decrementItemCount(coordinate, plannedResources);
  }

  public void itemNonExistent(Coordinate coordinate) {
    List<Resource> itemsAtLocation = currentResources.stream()
            .filter(resource -> resource.getCoordinate().equals(coordinate))
            .collect(Collectors.toList());
    currentResources.removeAll(itemsAtLocation);
    itemsAtLocation = plannedResources.stream()
            .filter(resource -> resource.getCoordinate().equals(coordinate))
            .collect(Collectors.toList());
    plannedResources.removeAll(itemsAtLocation);
  }

  public void itemCollected(Coordinate coordinate) {
    int newCount = decrementItemCount(coordinate, currentResources);
    setItemCount(coordinate, plannedResources, newCount);
    totalItemsCollected++;
  }

  private void setItemCount(Coordinate coordinate, Collection<Resource> resources, int count) {
    Optional<Resource> itemOptional = getItemAtLocation(coordinate, resources);
    if(itemOptional.isPresent()) {
      Resource resource = itemOptional.get();
      resource.setCount(count);
    } else {
      logger.error("Could not determine correct item. Error!");
    }
  }

  private int decrementItemCount(Coordinate coordinate, Collection<Resource> resources) {
    Optional<Resource> itemOptional = getItemAtLocation(coordinate, resources);
    if(itemOptional.isPresent()) {
      Resource resource = itemOptional.get();
      int oldCount = resource.getCount();
      resource.setCount(oldCount - 1);
      return resource.getCount();
    } else {
      logger.error("Could not determine correct item. Error!");
      return 0;
    }
  }

  private Optional<Resource> getItemAtLocation(Coordinate coordinate, Collection<Resource> resources) {
    List<Resource> itemsAtLocation = resources.stream()
            .filter(resource -> resource.getCoordinate().equals(coordinate))
            .collect(Collectors.toList());
    if(itemsAtLocation.size() == 1) {
      return Optional.of(itemsAtLocation.get(0));
    } else {
      logger.error("Could not determine correct item. Error!");
      return Optional.empty();
    }
  }

  public int getTotalItemsCollected() {
    return totalItemsCollected;
  }

  /**
   * True if resource is new.
   */
  public boolean foundResource(Resource resource, Integer count) {
    long itemsAtLocation = currentResources.stream()
            .filter(x -> x.getCoordinate().equals(resource.getCoordinate()))
            .count();
    if(itemsAtLocation == 0) {
      resource.setCount(count);
      logger.info("Adding new resource: {}", resource);
      currentResources.add(resource);
      plannedResources.add(resource.clone());
      return true;
    } else {
      logger.info("Found pre-existing resource: {}", resource);
      return false;
    }
  }
}
