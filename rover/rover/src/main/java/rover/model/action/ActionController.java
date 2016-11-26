package rover.model.action;

import rover.mediators.RoverFacade;
import rover.mediators.data.update.UpdateEvent;
import rover.mediators.data.update.UpdateStatus;
import rover.model.action.primitives.RoverAction;

/**
 * Created by dominic on 23/11/16.
 */
public class ActionController {
  private final RoverFacade roverFacade;
  private RoverAction previousAction;

  public ActionController(RoverFacade roverFacade) {
    this.roverFacade = roverFacade;
  }

  public synchronized void executeAction(RoverAction action) {
    previousAction = action;
    action.execute(roverFacade);
  }

  public void response(UpdateEvent updateEvent) {
    switch(updateEvent.getUpdateStatus()) {
      case COMPLETE:
      case CANCELLED:
        previousAction.complete();
        //TODO Clear Failed Action List
        break;
      case FAILED:
        //TODO Add to Failed Action List
        break;
    }
    previousAction = null;
  }
  // Accept Response and run complete
}
