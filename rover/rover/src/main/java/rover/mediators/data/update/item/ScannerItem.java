package rover.mediators.data.update.item;

/**
 * Created by dominic on 26/10/16.
 *
 * Holds a given scanner item
 */
public class ScannerItem {
  private final ScannerItemType scannerItemType;
  private final RelativeCoordinates relativeCoordinates;

  public ScannerItem(ScannerItemType scannerItemType, RelativeCoordinates relativeCoordinates) {
    this.scannerItemType = scannerItemType;
    this.relativeCoordinates = relativeCoordinates;
  }

  public ScannerItemType getScannerItemType() {
    return scannerItemType;
  }

  public RelativeCoordinates getRelativeCoordinates() {
    return relativeCoordinates;
  }
}
