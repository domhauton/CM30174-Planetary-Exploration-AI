package rover.model.action.primitives;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import rover.mediators.RoverFacade;
import rover.model.communication.CommunicationManager;
import rover.model.roverinfo.RoverInfo;

/**
 * Created by dominic on 23/11/16.
 *
 * A possible action to be executed by the rover
 */
public abstract class RoverAction {
  final RoverInfo roverInfo;
  final CommunicationManager communicationManager;
  final Logger logger;

  RoverAction(RoverInfo roverInfo, CommunicationManager communicationManager) {
    this.roverInfo = roverInfo;
    this.communicationManager = communicationManager;
    logger = LoggerFactory.getLogger("AGENT");
  }

  public abstract ActionCost getActionCost();
  public abstract void selected();
  public abstract void execute(RoverFacade roverFacade);
  public abstract void complete();
  public abstract void failed();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoverAction)) return false;
    RoverAction that = (RoverAction) o;
    return Objects.equals(roverInfo, that.roverInfo) &&
            Objects.equals(communicationManager, that.communicationManager) &&
            Objects.equals(logger, that.logger);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roverInfo, communicationManager, logger);
  }
}
