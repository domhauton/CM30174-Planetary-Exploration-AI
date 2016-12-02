package rover.rovers;

import rover.RoverAttributes;
import rover.RoverDecorator;

/**
 * Created by dominic on 01/12/16.
 */
public class CollectorLiquidClose extends RoverDecorator {
  public CollectorLiquidClose() {
    super(RoverAttributes.CLOSE_RANGE_COLLECTOR_L);
  }
}
