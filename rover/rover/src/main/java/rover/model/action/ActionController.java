package rover.model.action;

import java.util.HashSet;
import java.util.Set;

import rover.mediators.RoverFacade;
import rover.mediators.data.update.UpdateEvent;
import rover.model.action.primitives.RoverAction;

/**
 * Created by dominic on 23/11/16.
 */
public class ActionController {
  private final RoverFacade roverFacade;
  private RoverAction previousAction;
  private final Set<RoverAction> failedActions;

  public ActionController(RoverFacade roverFacade) {
    this.roverFacade = roverFacade;
    this.failedActions = new HashSet<>();
  }

  public synchronized void executeAction(RoverAction action) {
    previousAction = action;
    action.execute(roverFacade);
  }

  public void response(UpdateEvent updateEvent) {
    switch(updateEvent.getUpdateStatus()) {
      case COMPLETE:
      case CANCELLED:
        if(previousAction != null){
          previousAction.complete();
        }
        failedActions.clear();
        break;
      case FAILED:
        failedActions.add(previousAction);
        break;
    }
    previousAction = null;
  }

  public boolean isFailedAction(RoverAction action) {
    return failedActions.contains(action);
  }
}
