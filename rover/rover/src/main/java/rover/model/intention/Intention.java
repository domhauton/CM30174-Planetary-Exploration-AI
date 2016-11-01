package rover.model.intention;

import java.util.Collection;
import java.util.Collections;

import rover.model.WorldManager;

/**
 * Created by dominic on 31/10/16.
 */
enum Intention {
  SEARCH_FOR_RESOURCE(
          Collections.emptyList(),
          Collections.singletonList(Goals.RESOURCE_LOCATED)
  ),
  COLLECT_RESOURCE(
          Collections.singletonList(Goals.RESOURCE_LOCATED),
          Collections.singletonList(Goals.RESOURCE_LOADED)
  ),
  RESOURCE_DELIVERED(
          Collections.singletonList(Goals.RESOURCE_LOADED),
          Collections.singletonList(Goals.RESOURCE_DELIVERED)
  );

  private final Collection<Goals> requirements;
  private final Collection<Goals> reward;

  Intention(Collection<Goals> requirements, Collection<Goals> reward) {
    this.requirements = requirements;
    this.reward = reward;
  }

  public Integer rankViability(WorldManager manager) {
    return requirements.stream().mapToInt(goals -> goals.rank(manager)).sum();
  }

  public Integer rankReward(WorldManager manager) {
    return reward.stream().mapToInt(goals -> goals.rank(manager)).sum();
  }
}
