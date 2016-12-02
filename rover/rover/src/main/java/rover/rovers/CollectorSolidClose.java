package rover.rovers;

import rover.RoverAttributes;
import rover.RoverDecorator;

/**
 * Created by dominic on 01/12/16.
 */
public class CollectorSolidClose extends RoverDecorator {
  public CollectorSolidClose() {
    super(RoverAttributes.CLOSE_RANGE_COLLECTOR_S);
  }
}
