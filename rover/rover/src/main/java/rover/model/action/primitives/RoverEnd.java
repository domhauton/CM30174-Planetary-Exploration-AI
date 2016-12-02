package rover.model.action.primitives;

import rover.mediators.RoverFacade;
import rover.model.communication.CommunicationManager;
import rover.model.roverinfo.RoverInfo;

/**
 * Created by dominic on 29/11/16.
 */
public class RoverEnd extends RoverAction {

  public RoverEnd(RoverInfo roverInfo, CommunicationManager communicationManager) {
    super(roverInfo, communicationManager);
  }

  @Override
  public ActionCost getActionCost() {
    return new ActionCost(0, 0);
  }

  @Override
  public void selected() {
    // Nothing to tell others.
  }

  @Override
  public void execute(RoverFacade roverFacade) {
    roverFacade.stop();
  }

  @Override
  public void complete() {
    //Nothing
  }

  @Override
  public void failed() {
    //Nothing
  }
}
