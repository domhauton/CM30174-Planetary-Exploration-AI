package rover.rovers;

import rover.RoverAttributes;
import rover.RoverDecorator;

/**
 * Created by dominic on 01/12/16.
 */
public class MediumRangeScanCollectRover extends RoverDecorator {
  public MediumRangeScanCollectRover() {
    super(RoverAttributes.MEDIUM_RANGE_SCAN_COLLECT_S);
  }
}
