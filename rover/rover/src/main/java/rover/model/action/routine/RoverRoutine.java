package rover.model.action.routine;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import rover.Rover;
import rover.model.action.primitives.ActionCost;
import rover.model.action.primitives.RoverAction;

/**
 * Created by dominic on 29/11/16.
 */
public class RoverRoutine {
  private final LinkedList<RoverAction> actionList;
  private final RoutineType routineType;
  private final Double value;
  private RoverAction finalAction;

  public RoverRoutine(RoutineType routineType, Double value) {
    actionList = new LinkedList<>();
    this.routineType = routineType;
    this.value = value;
  }

  public synchronized void addAction(RoverAction roverAction) {
    actionList.add(roverAction);
    finalAction = roverAction;
  }

  public ActionCost getTotalCost() {
    double energyCost = actionList
            .stream()
            .map(RoverAction::getActionCost)
            .mapToDouble(ActionCost::getEnergy)
            .sum();
    double timeCost = actionList
            .stream()
            .map(RoverAction::getActionCost)
            .mapToDouble(ActionCost::getTime)
            .sum();
    return new ActionCost(energyCost, timeCost);
  }

  public RoutineType getRoutineType() {
    return routineType;
  }

  public Double getValue() {
    return value;
  }

  public LinkedList<RoverAction> getActionList() {
    return actionList;
  }

  public RoverAction getFirstAction() { return actionList.getFirst(); }

  public RoverAction getFinalAction() {
    return finalAction;
  }
}
