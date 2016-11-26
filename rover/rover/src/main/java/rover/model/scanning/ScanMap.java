package rover.model.scanning;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dominic on 25/11/16.
 */
public class ScanMap {
  private ScanState[][] scanStates;

  public ScanMap(int xSize, int ySize) {
    scanStates = new ScanState[xSize][ySize];
    for (ScanState[] line: scanStates) {
      Arrays.fill(line, ScanState.UNKNOWN);
    }
  }

  void put(int x, int y, ScanState state) {
    int normX = normaliseCoordinate(x, scanStates[0].length);
    int normY = normaliseCoordinate(y, scanStates.length);
    if(scanStates[normX][normY].getValue() < state.getValue()) {
      scanStates[normX][normY] = state;
    }
  }

  ScanState get(int x, int y) {
    int normX = normaliseCoordinate(x, scanStates[0].length);
    int normY = normaliseCoordinate(y, scanStates.length);
    return scanStates[normX][normY];
  }

  private int normaliseCoordinate(int val, int size) {
    while(val < 0) {
      val += size;
    }
    return val%size;
  }

  @Override
  public String toString() {
    return "ScanMap{ \n" +
            Stream.of(scanStates)
                    .map(x -> Stream.of(x).map(ScanState::toString).collect(Collectors.joining()))
                    .collect(Collectors.joining("\n")) +
            "\n}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ScanMap)) return false;

    ScanMap scanMap = (ScanMap) o;

    return Arrays.deepEquals(scanStates, scanMap.scanStates);

  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(scanStates);
  }


}
