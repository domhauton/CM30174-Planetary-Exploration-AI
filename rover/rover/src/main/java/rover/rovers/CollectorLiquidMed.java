package rover.rovers;

import rover.RoverAttributes;
import rover.RoverDecorator;

/**
 * Created by dominic on 01/12/16.
 */
public class CollectorLiquidMed extends RoverDecorator {
  public CollectorLiquidMed() {
    super(RoverAttributes.MEDIUM_RANGE_COLLECTOR_L);
  }
}
