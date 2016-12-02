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
    logger.info("ROVER ACTION - ADDING NEW ROUTINE");
    currentRoutine = roverRoutine;
    roverRoutine.getActionList().forEach(RoverAction::selected);
    roverRoutine.getActionList().forEach(action -> logger.info("ROVER ACTION - ADDING NEW ROUTINE -" + action));
    remainingActions = roverRoutine.getActionList();
  }

  public synchronized ActionCost getRemainingRoutineCost() {
    RoverRoutine tmp = new RoverRoutine(currentRoutine.getRoutineType(), currentRoutine.getValue());
    remainingActions.forEach(tmp::addAction);
    return tmp.getTotalCost();
  }

  public synchronized void executeAction() {
    if(previousAction != null) {
      logger.error("ROVER ACTION - WARNING - NEW ACTION EXECUTED WHILE PREVIOUS NOT CLEARED.");
    }
    Optional<RoverAction> roverActionOptional = Optional.ofNullable(remainingActions.pollFirst());

    if (roverActionOptional.isPresent()) {
      logger.info("ROVER ACTION - EXECUTING ACTION - TYPE: {}", roverActionOptional.isPresent() ? roverActionOptional.get().getType() : "NULL");
      RoverAction roverAction = roverActionOptional.get();
      previousAction = roverAction;
      roverAction.execute(roverFacade);
    } else {
      currentRoutine = new RoverRoutine(RoutineType.IDLE, Double.MIN_VALUE/2.0);
      //previousAction = null;
      decideOnNextAction.run();
    }
  }

  public synchronized void response(UpdateEvent updateEvent) {
    RoverAction originalAction = previousAction;
    logger.info("ROVER ACTION - RESPONSE - ORIGINAL ACTION TYPE: {}", originalAction != null ? originalAction.getType() : "NULL");
    previousAction = null;
    switch (updateEvent.getUpdateStatus()) {
      case COMPLETE:
        if (originalAction != null) {
          originalAction.complete();
        }
        break;
      case CANCELLED:
      case FAILED:
        if (originalAction != null) {
          logger.info("Executing fail for failed action!");
          originalAction.failed();
          logger.info("Executing fail for remaining actions!");
          remainingActions.forEach(RoverAction::failed);
          logger.info("Clearing routine");
          remainingActions.clear();
          logger.info("Nothing more to do in routine. Bid instead by executing action.");
        }
        break;
    }
    executeAction();
  }
}
