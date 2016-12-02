package rover.rovers;

import rover.RoverAttributes;
import rover.RoverDecorator;

/**
 * Created by dominic on 01/12/16.
 */
public class GeneralPurpose extends RoverDecorator {
  public GeneralPurpose() {
    super(RoverAttributes.DEFAULT);
  }
}
