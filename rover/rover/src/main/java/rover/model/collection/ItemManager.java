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
  private Collection<Resource> resources;
  private Collection<Resource> plannedResources;
  private int totalItemsCollected;
  private int plannedItemsCollected;

  public ItemManager() {
    logger = LoggerFactory.getLogger("AGENT");
    resources = new LinkedList<>();
    totalItemsCollected = 0;
    revertPlannedCollections();
  }

  public void revertPlannedCollections() {
    plannedResources = new LinkedList<>();
    resources.stream()
            .map(Resource::clone)
            .forEach(plannedResources::add);
    plannedItemsCollected = totalItemsCollected;
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

  public void recordItemCollectPlanned(Coordinate coordinate) {
    collectFromResourceAtLocation(coordinate, plannedResources);
  }

  public void recordItemCollected(Coordinate coordinate) {
    recordItemCollectPlanned(coordinate);
    collectFromResourceAtLocation(coordinate, resources);
  }

  private void collectFromResourceAtLocation(Coordinate coordinate, Collection<Resource> resources) {
    List<Resource> itemsAtLocation = resources.stream()
            .filter(resource -> resource.getCoordinate().equals(coordinate))
            .collect(Collectors.toList());
    if(itemsAtLocation.size() == 1) {
      Resource resource = itemsAtLocation.get(0);
      resource.setCount(resource.getCount() - 1);
      totalItemsCollected++;
    } else {
      logger.error("Could not determine correct item. Error!");
    }
  }

  public int getTotalItemsCollected() {
    return plannedItemsCollected;
  }

  public void foundResource(Resource resource, Integer count) {
    if(!resources.contains(resource)) {
      resource.setCount(count);
      logger.info("Adding new resource: {}", resource);
      resources.add(resource);
      plannedResources.add(resource.clone());
    } else {
      logger.info("Found pre-existing resource: {}", resource);
    }
  }
}
