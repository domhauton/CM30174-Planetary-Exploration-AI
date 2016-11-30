package rover.model.action.primitives;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rover.mediators.RoverFacade;
import rover.model.roverinfo.RoverInfo;

/**
 * Created by dominic on 23/11/16.
 *
 * A possible action to be executed by the rover
 */
public abstract class RoverAction {
  final RoverInfo roverInfo;
  final Logger logger;

  RoverAction(RoverInfo roverInfo) {
    this.roverInfo = roverInfo;
    logger = LoggerFactory.getLogger("AGENT");
  }

  public abstract ActionCost getActionCost();
  public abstract void execute(RoverFacade roverFacade);
  public abstract void complete();
  public abstract void failed();
}
