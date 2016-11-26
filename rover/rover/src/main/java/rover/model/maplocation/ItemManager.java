package rover.model.maplocation;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by dominic on 31/10/16.
 */
public class ItemManager {
  private Collection<Resource> resources;

  public ItemManager() {
    resources = new LinkedList<>();
  }

  public void registerResource(Resource resource) {
    resources.add(resource);
  }

  public void removeResource(Resource resource) {
    resources.remove(resource);
  }

  public Integer getResourceCount() {
    return resources.size();
  }
}
