package rover.model.planner;

import java.util.Objects;

import rover.model.action.primitives.RoverAction;
import rover.model.action.routine.RoverRoutine;

/**
 * Created by dominic on 30/11/16.
 */
public class PlannedAction {
  private final RoverAction roverAction;
  private final RoverRoutine parentRoutine;
  private final StoredActionState state;
  private final StoredActionType type;
  private final Integer actionSequenceNumber;
  private final String uid;

  public PlannedAction(RoverAction roverAction, RoverRoutine parentRoutine, StoredActionState state, StoredActionType type, Integer actionSequenceNumber, String uid) {
    this.roverAction = roverAction;
    this.parentRoutine = parentRoutine;
    this.state = state;
    this.type = type;
    this.actionSequenceNumber = actionSequenceNumber;
    this.uid = uid;
  }

  public RoverAction getRoverAction() {
    return roverAction;
  }

  public StoredActionState getState() {
    return state;
  }

  public RoverRoutine getParentRoutine() {
    return parentRoutine;
  }

  public Integer getActionSequenceNumber() {
    return actionSequenceNumber;
  }

  public String getUid() {
    return uid;
  }

  public StoredActionType getType() {
    return type;
  }

  public PlannedAction clone() {
    return clone(state);
  }

  public PlannedAction clone(StoredActionState storedActionState) {
    return new PlannedAction(roverAction, parentRoutine, state, type, actionSequenceNumber, uid);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PlannedAction)) return false;
    PlannedAction that = (PlannedAction) o;
    return Objects.equals(getRoverAction(), that.getRoverAction()) &&
            Objects.equals(getParentRoutine(), that.getParentRoutine()) &&
            getState() == that.getState();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getRoverAction(), getParentRoutine(), getState());
  }
}
