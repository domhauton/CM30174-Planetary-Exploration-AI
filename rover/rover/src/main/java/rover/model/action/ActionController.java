package rover.model.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private Logger logger;

  private RoverRoutine currentRoutine;
  private LinkedList<RoverAction> remainingActions;
  private Runnable decideOnNextAction;

  public ActionController(RoverFacade roverFacade, Runnable decideOnNextAction) {
    logger = LoggerFactory.getLogger("AGENT");
    this.decideOnNextAction = decideOnNextAction;
    this.roverFacade = roverFacade;
    this.currentRoutine = new RoverRoutine(RoutineType.IDLE, Double.MIN_VALUE/2.0);
    this.remainingActions = new LinkedList<>();
  }

  public synchronized void setRoutineToExecute(RoverRoutine roverRoutine) {
    currentRoutine = roverRoutine;
    roverRoutine.getActionList().forEach(RoverAction::selected);
    remainingActions = roverRoutine.getActionList();
  }

  public synchronized ActionCost getRemainingRoutineCost() {
    RoverRoutine tmp = new RoverRoutine(currentRoutine.getRoutineType(), currentRoutine.getValue());
    remainingActions.forEach(tmp::addAction);
    return tmp.getTotalCost();
  }

  public synchronized void executeAction() {
    Optional<RoverAction> roverActionOptional = Optional.ofNullable(remainingActions.pollFirst());
    if (roverActionOptional.isPresent()) {
      RoverAction roverAction = roverActionOptional.get();
      roverAction.execute(roverFacade);
      previousAction = roverAction;
    } else {
      currentRoutine = new RoverRoutine(RoutineType.IDLE, Double.MIN_VALUE/2.0);
      decideOnNextAction.run();
    }
  }

  public void response(UpdateEvent updateEvent) {
    switch (updateEvent.getUpdateStatus()) {
      case COMPLETE:
        if (previousAction != null) {
          previousAction.complete();
        }
        break;
      case CANCELLED:
      case FAILED:
        if (previousAction != null) {
          logger.info("Executing fail for failed action!");
          previousAction.failed();
          logger.info("Executing fail for remaining actions!");
          remainingActions.forEach(RoverAction::failed);
          logger.info("Clearing routine");
          remainingActions.clear();
          logger.info("Nothing more to do in routine. Bid instead by executing action.");
        }
        break;
    }
    previousAction = null;
    executeAction();
  }
}
