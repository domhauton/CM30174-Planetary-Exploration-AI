package rover.model.collection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import rover.mediators.data.update.item.ResourceType;
import rover.model.maplocation.Coordinate;

/**
 * Created by dominic on 01/12/16.
 */
public class ItemManagerTest {

  private ItemManager itemManager;

  @Before
  public void setUp() throws Exception {
    itemManager = new ItemManager();
  }

  @Test
  public void addItem() throws Exception {
    Resource resource = new Resource(ResourceType.LIQUID, Coordinate.ORIGIN);
    itemManager.foundResource(resource, 1);
    Optional<Resource> desirableResource = itemManager.getMostDesirable(1, 1, Coordinate.ORIGIN, 10, ResourceType.LIQUID);
    Assert.assertTrue(desirableResource.isPresent());
  }

  @Test
  public void addItemWrongType() throws Exception {
    Resource resource = new Resource(ResourceType.SOLID, Coordinate.ORIGIN);
    itemManager.foundResource(resource, 1);
    Optional<Resource> desirableResource = itemManager.getMostDesirable(1, 1, Coordinate.ORIGIN, 10, ResourceType.LIQUID);
    Assert.assertFalse(desirableResource.isPresent());
  }

  @Test
  public void addItemPlanRevert() throws Exception {
    Resource resource = new Resource(ResourceType.LIQUID, Coordinate.ORIGIN);
    itemManager.foundResource(resource, 1);
    itemManager.recordItemCollectPlanned(Coordinate.ORIGIN);
    Optional<Resource> desirableResource = itemManager.getMostDesirable(1, 1, Coordinate.ORIGIN, 10, ResourceType.LIQUID);
    Assert.assertFalse(desirableResource.isPresent());
    itemManager.revertPlannedCollections();
    Optional<Resource> desirableResource2 = itemManager.getMostDesirable(1, 1, Coordinate.ORIGIN, 10, ResourceType.LIQUID);
    Assert.assertTrue(desirableResource2.isPresent());
    itemManager.recordItemCollected(Coordinate.ORIGIN);
    Optional<Resource> desirableResource3 = itemManager.getMostDesirable(1, 1, Coordinate.ORIGIN, 10, ResourceType.LIQUID);
    Assert.assertFalse(desirableResource3.isPresent());
    itemManager.revertPlannedCollections();
    Optional<Resource> desirableResource4 = itemManager.getMostDesirable(1, 1, Coordinate.ORIGIN, 10, ResourceType.LIQUID);
    Assert.assertFalse(desirableResource4.isPresent());
  }

  @Test
  public void addItemIgnore() throws Exception {
    Resource resource = new Resource(ResourceType.LIQUID, Coordinate.ORIGIN);
    itemManager.foundResource(resource, 1);
    itemManager.foundResource(resource, 1);
    itemManager.recordItemCollectPlanned(Coordinate.ORIGIN);
    Optional<Resource> desirableResource = itemManager.getMostDesirable(1, 1, Coordinate.ORIGIN, 10, ResourceType.LIQUID);
    Assert.assertFalse(desirableResource.isPresent());
  }
}