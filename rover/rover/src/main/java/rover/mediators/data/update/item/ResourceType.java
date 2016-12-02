package rover.mediators.data.update.item;

import java.util.stream.Stream;

/**
 * Created by dominic on 31/10/16.
 */
public enum ResourceType {
  SOLID(1),
  LIQUID(2),
  INVALID(-1);

  private int id;

  ResourceType(Integer id) {
    this.id = id;
  }

  public static ResourceType getById(int id) {
    return Stream.of(ResourceType.values())
            .filter(resourceType -> resourceType.id == id)
            .findFirst()
            .orElseGet(() -> ResourceType.INVALID);
  }

  public int getId() {
    return id;
  }
}
