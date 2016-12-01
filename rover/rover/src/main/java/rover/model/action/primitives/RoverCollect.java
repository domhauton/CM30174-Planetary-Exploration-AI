package rover.model.action.primitives;

import rover.Scenario;
import rover.mediators.RoverFacade;
import rover.model.roverinfo.RoverInfo;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;

/**
 * Created by dominic on 29/11/16.
 */
public class RoverCollect extends RoverAction {
  private Resource resource;
  private ItemManager itemManager;

  public RoverCollect(RoverInfo roverInfo, Resource resource, ItemManager itemManager) {
    super(roverInfo);
    this.resource = resource;
    this.itemManager = itemManager;
  }

  @Override
  public ActionCost getActionCost() {
    return new ActionCost(5, 5);
  }

  @Override
  public void execute(RoverFacade roverFacade) {
    roverFacade.collect();
  }

  @Override
  public void complete() {
    roverInfo.addPayload();
    itemManager.recordItemCollected(resource.getCoordinate());
  }

  @Override
  public void failed() {
    logger.error("Failed to pick up payload. Scenario may have changed.");
    resource.setCount(-1);
  }
}
