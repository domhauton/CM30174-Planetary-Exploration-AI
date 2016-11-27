package rover.model.scanning;

/**
 * Created by dominic on 24/11/16.
 */
public class ScanManager {
  private final double gridWidthInRealUnits;
  private final ScanMap scanMap;

  public ScanManager(int realMapWidthX, int realMapWidthY, double gridWidthInRealUnits) {
    this.gridWidthInRealUnits = gridWidthInRealUnits;
    scanMap = new ScanMap(realToGridConverter(realMapWidthX), realToGridConverter(realMapWidthY));
  }

  public int realToGridConverter(int val) {
    return (int) ((double) val / gridWidthInRealUnits);
  }
}
