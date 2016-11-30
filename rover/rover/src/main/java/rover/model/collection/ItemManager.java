package rover.model.collection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import rover.mediators.data.update.item.ResourceType;
import rover.model.maplocation.Coordinate;
import util.Pair;

/**
 * Created by dominic on 31/10/16.
 */
public class ItemManager {
  private Collection<Resource> resources;

  public ItemManager() {
    resources = new LinkedList<>();
  }

  public Optional<Resource> getMostDesirable(Integer remainingCargoCapacity,
                                             Coordinate roverPosition,
                                             Integer mapSize,
                                             ResourceType roverResourceCapability) {
    return resources
            .stream()
            .filter(resource -> resource.getResourceType() == roverResourceCapability)
            .map(resource -> new Pair<>(resource, resource.getTotalMoveDistance(roverPosition, mapSize)))
            .sorted((o1, o2) -> -o1.getB().compareTo(o2.getB()))
            .map(Pair::getA)
            .findFirst();
  }

  public void clearItemManager() {
    resources = new LinkedList<>();
  }

  public void addResource(Resource resource) {
    resources.add(resource);
  }
}
