package rover.model.action.primitives;

import rover.mediators.RoverFacade;
import rover.model.roverinfo.RoverInfo;

/**
 * Created by dominic on 29/11/16.
 */
public class RoverDeposit extends RoverAction {
  public RoverDeposit(RoverInfo roverInfo) {
    super(roverInfo);
  }

  @Override
  public ActionCost getActionCost() {
    return new ActionCost(0, 0);
  }

  @Override
  public void execute(RoverFacade roverFacade) {
    roverFacade.deposit();
  }

  @Override
  public void complete() {
    roverInfo.removePayload();
  }

  @Override
  public void failed() {
    logger.error("Failed to deposit. Resetting current payload to ZERO. Please investigate.");
    while(roverInfo.getCurrentPayload() > 0) {
      roverInfo.removePayload();
    }
  }
}
