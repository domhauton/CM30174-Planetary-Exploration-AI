package rover.rovers;

import rover.RoverAttributes;
import rover.RoverDecorator;

/**
 * Created by dominic on 01/12/16.
 */
public class CollectorLiquidScannerClose extends RoverDecorator {
  public CollectorLiquidScannerClose() {
    super(RoverAttributes.CLOSE_RANGE_SCANNER_COLLECTOR_L);
  }
}
