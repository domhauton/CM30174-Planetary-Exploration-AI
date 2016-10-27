package rover.mediators;

import rover.Rover;
import rover.mediators.data.message.OutboundTeamMessage;
import rover.mediators.data.message.OutboundUserMessage;

/**
 * Created by dominic on 25/10/16.
 */
public class RoverFacade {
  private final Rover rover;

  public RoverFacade(Rover rover) {
    this.rover = rover;
  }

  public void deposit() throws RoverActionException {
    try {
      rover.deposit();
    } catch (Exception e) {
      throw new RoverActionException(
              "Error depositing. Check payload for items.", e);
    }
  }

  public void collect() throws RoverActionException {
    try {
      rover.collect();
    } catch (Exception e) {
      throw new RoverActionException(
              "Error collecting. Check if over items", e);
    }
  }

  public void scan(Double range) throws RoverActionException {
    try {
      rover.scan(range);
    } catch (Exception e) {
      throw new RoverActionException(
              "Error scanning. Check requested scan range: " + range, e);
    }
  }

  public void move(Double xOffset, Double yOffset, Double speed) throws RoverActionException {
    try {
      rover.move(xOffset, yOffset, speed);
    } catch (Exception e) {
      throw new RoverActionException(
              "Error Moving. Check requested speed: " + speed, e);
    }
  }

  public void stop() throws RoverActionException {
    try {
      rover.stop();
    } catch (Exception e) {
      throw new RoverActionException(
              "Error stopping action. Action may have completed.", e);
    }
  }

  public void sendMessage(OutboundTeamMessage outboundTeamMessage) {
    rover.broadCastToTeam(outboundTeamMessage.getMessage());
  }

  public void sendMessage(OutboundUserMessage outboundUserMessage) {
    rover.broadCastToUnit(outboundUserMessage.getTargetUserId(), outboundUserMessage.getMessage());
  }
}
