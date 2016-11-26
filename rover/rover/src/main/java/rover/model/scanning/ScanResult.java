package rover.model.scanning;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dominic on 25/11/16.
 */
class ScanResult {
  private final Set<ScanCoordinate> newScanned;
  private final Set<ScanCoordinate> newPartial;

  ScanResult(final int xCenter, final int yCenter, final int scanRange) {
    newScanned = new HashSet<>();
    newPartial = new HashSet<>();
    scanRasterizer(xCenter, yCenter, scanRange);
  }

  private void scanRasterizer(final int xCenter, int yCenter, int scanRange) {
    int x = scanRange - 1;
    int y = 0;
    int err = 0;


    while (x >= y) {
      newPartial.addAll(getMirroredValueSet(xCenter, yCenter, x, y));
      for (int tmpX = x - 1; tmpX >= y; tmpX--) {
        newScanned.addAll(getMirroredValueSet(xCenter, yCenter, tmpX, y));
      }

      y += 1;
      err += 1 + 2 * y;
      if (2 * (err - x) + 1 > 0) {
        x -= 1;
        err += 1 - 2 * x;
      }
    }
  }

  private Set<ScanCoordinate> getMirroredValueSet(int xCenter, int yCenter, int x, int y) {
    Set<ScanCoordinate> coordinateSet = new HashSet<>(8);
    coordinateSet.add(new ScanCoordinate(xCenter + x, yCenter + y));
    coordinateSet.add(new ScanCoordinate(xCenter + y, yCenter + x));
    coordinateSet.add(new ScanCoordinate(xCenter - y, yCenter + x));
    coordinateSet.add(new ScanCoordinate(xCenter - x, yCenter + y));
    coordinateSet.add(new ScanCoordinate(xCenter - x, yCenter - y));
    coordinateSet.add(new ScanCoordinate(xCenter - y, yCenter - x));
    coordinateSet.add(new ScanCoordinate(xCenter + y, yCenter - x));
    coordinateSet.add(new ScanCoordinate(xCenter + x, yCenter - y));
    return coordinateSet;
  }

  public void applyScan(ScanMap scanMap) {
    newPartial.forEach(val -> scanMap.put(val.getX(), val.getY(), ScanState.PARTIAL));
    newScanned.forEach(val -> scanMap.put(val.getX(), val.getY(), ScanState.SCANNED));
  }

  public int calculateScanValue(ScanMap scanMap) {
    int partialValue = newPartial.stream()
            .filter(val -> scanMap.get(val.getX(), val.getY()).getValue() < ScanState.PARTIAL.getValue())
            .mapToInt(val -> ScanState.PARTIAL.getValue() - scanMap.get(val.getX(), val.getY()).getValue())
            .sum();
    int scannedValue = newScanned.stream()
            .filter(val -> scanMap.get(val.getX(), val.getY()).getValue() < ScanState.SCANNED.getValue())
            .mapToInt(val -> ScanState.SCANNED.getValue() - scanMap.get(val.getX(), val.getY()).getValue())
            .sum();
    return partialValue + scannedValue;
  }
}
