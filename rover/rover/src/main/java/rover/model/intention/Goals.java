package rover.model.intention;

import java.util.function.Function;

import rover.model.WorldManager;

/**
 * Created by dominic on 31/10/16.
 */
public enum Goals {
  RESOURCE_LOCATED(WorldManager::getPayloadLocationCount),
  RESOURCE_LOADED(WorldManager::getCurrentPayloadCount),
  RESOURCE_DELIVERED(WorldManager::getRemainingStorageSpace);

  private Function<WorldManager, Integer> check;

  Goals(Function<WorldManager, Integer> check) {
    this.check = check;
  }

  public Integer rank(WorldManager worldManager) {
    return check.apply(worldManager);
  }
}
