package rover.model.action.primitives;

import rover.mediators.RoverFacade;
import rover.model.RoverInfo;

/**
 * Created by dominic on 23/11/16.
 *
 * A possible action to be executed by the rover
 */
public abstract class RoverAction {
  final RoverInfo roverInfo;

  RoverAction(RoverInfo roverInfo) {
    this.roverInfo = roverInfo;
  }

  public abstract ActionCost getActionCost();
  public abstract void execute(RoverFacade roverFacade);
  public abstract void complete();
  public abstract void failed();
}
