package rover.model.action.primitives;

import rover.mediators.RoverFacade;
import rover.model.communication.CommunicationManager;
import rover.model.roverinfo.RoverInfo;

/**
 * Created by dominic on 29/11/16.
 */
public class RoverDeposit extends RoverAction {
  public RoverDeposit(RoverInfo roverInfo, CommunicationManager communicationManager) {
    super(roverInfo, communicationManager);
  }

  @Override
  public ActionCost getActionCost() {
    return new ActionCost(5, 5);
  }

  @Override
  public void selected() {
    // Nothing to tell others.
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

  @Override
  public String getType() {
    return "DEPOSIT";
  }
}
