package rover.model.scanning;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by dominic on 24/11/16.
 */
public class ScanManager {
  private final double mapWidthX;
  private final double mapWidthY;
  private final double scanningCoverageResolution;

  public ScanManager(int mapWidthX, int mapWidthY, double scanningCoverageResolution) {
    this.mapWidthX = mapWidthX;
    this.mapWidthY = mapWidthY;
    this.scanningCoverageResolution = scanningCoverageResolution;
    int xGridSize = (int) (mapWidthX/scanningCoverageResolution);
    int yGridSize = (int) (mapWidthY/scanningCoverageResolution);
  }

//  public int getScanUsefulness(double x, double y, double range) {
//
//  }


}
