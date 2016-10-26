package rover.mediators;

import rover.Rover;

/**
 * Created by dominic on 25/10/16.
 */
public class RoverActionService {
  private final Rover rover;

  public RoverActionService(Rover rover) {
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
}
