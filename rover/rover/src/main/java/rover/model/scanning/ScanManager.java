package rover.model.scanning;

/**
 * Created by dominic on 24/11/16.
 */
public class ScanManager {
  private final double gridWidthInRealUnits;
  private final ScanMap scanMap;

  public ScanManager(int realMapWidth, double gridWidthInRealUnits) {
    this.gridWidthInRealUnits = gridWidthInRealUnits;
    scanMap = new ScanMap(realToGridConverter(realMapWidth));
  }

  public int realToGridConverter(int val) {
    return (int) ((double) val / gridWidthInRealUnits);
  }
}
