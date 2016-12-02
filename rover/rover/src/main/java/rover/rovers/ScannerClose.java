package rover.rovers;

import rover.RoverAttributes;
import rover.RoverDecorator;

/**
 * Created by dominic on 01/12/16.
 */
public class ScannerClose extends RoverDecorator {
  public ScannerClose() {
    super(RoverAttributes.CLOSE_RANGE_SCANNER);
  }
}
