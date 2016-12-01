package rover.rovers;

import rover.RoverAttributes;
import rover.RoverDecorator;

/**
 * Created by dominic on 01/12/16.
 */
public class Scenario1Rover extends RoverDecorator {
  public Scenario1Rover() {
    super(RoverAttributes.SCENARIO_1);
  }
}
