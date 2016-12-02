package rover.mediators.data.update.item;

/**
 * Created by dominic on 26/10/16.
 *
 * Holds a given scanner item
 */
public class ScannerItem {
  private final ScannerItemType scannerItemType;
  private final RelativeCoordinates relativeCoordinates;
  private final ResourceType resourceType;

  public ScannerItem(ScannerItemType scannerItemType, RelativeCoordinates relativeCoordinates, ResourceType resourceType) {
    this.scannerItemType = scannerItemType;
    this.relativeCoordinates = relativeCoordinates;
    this.resourceType = resourceType;
  }

  public ScannerItemType getScannerItemType() {
    return scannerItemType;
  }

  public RelativeCoordinates getRelativeCoordinates() {
    return relativeCoordinates;
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  @Override
  public String toString() {
    return "ScannerItem{" +
            "scannerItemType=" + scannerItemType +
            ", relativeCoordinates=" + relativeCoordinates +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ScannerItem)) return false;

    ScannerItem that = (ScannerItem) o;

    if (getScannerItemType() != that.getScannerItemType()) return false;
    return getRelativeCoordinates() != null ? getRelativeCoordinates().equals(that.getRelativeCoordinates()) : that.getRelativeCoordinates() == null;

  }

  @Override
  public int hashCode() {
    int result = getScannerItemType() != null ? getScannerItemType().hashCode() : 0;
    result = 31 * result + (getRelativeCoordinates() != null ? getRelativeCoordinates().hashCode() : 0);
    return result;
  }
}
