package rover.model.action;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import rover.mediators.RoverFacade;
import rover.mediators.data.update.UpdateEvent;
import rover.model.action.primitives.ActionCost;
import rover.model.action.primitives.RoverAction;
import rover.model.action.routine.RoutineType;
import rover.model.action.routine.RoverRoutine;

/**
 * Created by dominic on 23/11/16.
 */
public class ActionController {
  private final RoverFacade roverFacade;
  private RoverAction previousAction;
  private final Set<RoverAction> failedActions;

  private RoverRoutine currentRoutine;
  private LinkedList<RoverAction> remainingActions;

  public ActionController(RoverFacade roverFacade) {
    this.roverFacade = roverFacade;
    this.failedActions = new HashSet<>();
    this.currentRoutine = new RoverRoutine(RoutineType.IDLE, 0.0);
    this.remainingActions = new LinkedList<>();
  }

  public synchronized void setRoutineToExecute(RoverRoutine roverRoutine) {
    currentRoutine = roverRoutine;
    remainingActions = roverRoutine.getActionList();
  }

  public synchronized int actionsRemainingInRoutine() {
    return remainingActions.size();
  }

  public synchronized ActionCost getRemainingRoutineCost() {
    RoverRoutine tmp = new RoverRoutine(currentRoutine.getRoutineType(), currentRoutine.getValue());
    remainingActions.forEach(tmp::addAction);
    return tmp.getTotalCost();
  }

  public synchronized void executeAction() {
    Optional<RoverAction> roverAction = Optional.ofNullable(remainingActions.pollFirst());
    if(roverAction.isPresent()) {
      roverAction.get().execute(roverFacade);
      previousAction = roverAction.get();
    }
    if(remainingActions.isEmpty()){
      currentRoutine = new RoverRoutine(RoutineType.IDLE, 0.0);
    }
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
