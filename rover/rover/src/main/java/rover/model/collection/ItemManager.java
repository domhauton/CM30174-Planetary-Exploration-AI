package rover.model.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import rover.mediators.data.update.item.ResourceType;
import rover.mediators.data.update.item.ScannerItem;
import rover.model.IntentionGenerator;
import rover.model.maplocation.Coordinate;
import util.Pair;

/**
 * Created by dominic on 31/10/16.
 */
public class ItemManager {
  private Logger logger;
  private Collection<Resource> resources;
  private int totalItemsCollected;

  public ItemManager() {
    logger = LoggerFactory.getLogger("AGENT");
    resources = new LinkedList<>();
    totalItemsCollected = 0;
  }

  public Optional<Resource> getMostDesirable(Integer movementDesire,
                                             Integer remainingCargoCapacity,
                                             Coordinate roverPosition,
                                             Integer mapSize,
                                             ResourceType roverResourceCapability) {

    return resources.stream()
            .filter(resource -> resource.getResourceType() == roverResourceCapability)
            .filter(resource -> resource.getCount() > 0)
            .map(resource -> new Pair<>(resource, resource.getDesirability(roverPosition, mapSize, movementDesire, remainingCargoCapacity)))
            .filter(pair -> pair.getB() > 0)
            .sorted((o1, o2) -> -o1.getB().compareTo(o2.getB()))
            .map(Pair::getA)
            .findFirst();
  }

  public void clearItemManager() {
    resources = new LinkedList<>();
  }

  public void recordItemCollected() {
    totalItemsCollected++;
  }

  public int getTotalItemsCollected() {
    return totalItemsCollected;
  }

  public void addResource(Resource resource, Integer count) {
    if(!resources.contains(resource)) {
      resource.setCount(count);
      logger.info("Adding new resource: {}", resource);
      resources.add(resource);
    } else {
      logger.info("Found pre-existing resource: {}", resource);
    }
  }
}
