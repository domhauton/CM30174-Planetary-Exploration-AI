package rover.model.action.primitives;

import rover.mediators.RoverFacade;
import rover.model.roverinfo.RoverInfo;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;

/**
 * Created by dominic on 29/11/16.
 */
public class RoverCollect extends RoverAction {
  private ItemManager itemManager;

  public RoverCollect(RoverInfo roverInfo, Resource resource, ItemManager itemManager) {
    super(roverInfo);
    this.itemManager = itemManager;
  }

  @Override
  public ActionCost getActionCost() {
    return new ActionCost(5, 5);
  }

  @Override
  public void execute(RoverFacade roverFacade) {
    roverFacade.deposit();
  }

  @Override
  public void complete() {
    //FIXME COMPLETE Finisher
    roverInfo.addPayload();
    trsd
  }

  @Override
  public void failed() {
    logger.error("Failed to pick up payload. Could be wrong type or cargo hold full. Please investigate.");
    while(roverInfo.getCurrentPayload() > 0) {
      roverInfo.removePayload();
    }
  }
}
