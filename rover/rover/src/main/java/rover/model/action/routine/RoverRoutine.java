package rover.model.action.routine;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import rover.Rover;
import rover.model.action.primitives.ActionCost;
import rover.model.action.primitives.RoverAction;

/**
 * Created by dominic on 29/11/16.
 */
public class RoverRoutine {
  private final Queue<RoverAction> actionList;
  private final RoutineType routineType;
  private final Double value;
  private final String roverId;
  private final Integer actionID;
  private RoverAction finalAction;

  public RoverRoutine(RoutineType routineType, Double value, String roverId, Integer actionId) {
    actionList = new LinkedBlockingQueue<>();
    this.routineType = routineType;
    this.value = value;
    this.roverId = roverId;
    this.actionID = actionId;
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

  public Queue<RoverAction> getActionList() {
    return actionList;
  }

  public String getRoverId() {
    return roverId;
  }

  public Integer getActionID() {
    return actionID;
  }

  public RoverAction getFinalAction() {
    return finalAction;
  }
}
