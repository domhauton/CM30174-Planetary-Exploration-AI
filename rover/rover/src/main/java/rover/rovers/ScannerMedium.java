package rover.rovers;

import rover.RoverAttributes;
import rover.RoverDecorator;

/**
 * Created by dominic on 01/12/16.
 */
public class ScannerMedium extends RoverDecorator {
  public ScannerMedium() {
    super(RoverAttributes.MEDIUM_RANGE_SCANNER);
  }
}
