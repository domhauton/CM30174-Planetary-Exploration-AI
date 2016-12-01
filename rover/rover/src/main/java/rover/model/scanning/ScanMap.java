package rover.model.scanning;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dominic on 25/11/16.
 */
public class ScanMap {
  private ScanState[][] scanStates;
  private final int size;
  private final double longestTravelDistance;

  private static double HALF_SQRT2 = Math.sqrt(2.0)/2;

  ScanMap(int size) {
    this(size, createEmptyStateMatrix(size));
  }

  private ScanMap(int size, ScanState[][] scanStates) {
    this.size = size;
    longestTravelDistance = size*HALF_SQRT2;
    this.scanStates = scanStates;
  }

  private static ScanState[][] createEmptyStateMatrix(int size) {
    ScanState[][] retStates = new ScanState[size][size];
    for (ScanState[] line : retStates) {
      Arrays.fill(line, ScanState.UNKNOWN);
    }
    return retStates;
  }

  void put(int x, int y, ScanState state) {
    int normX = normaliseCoordinate(x);
    int normY = normaliseCoordinate(y);
    if (scanStates[normX][normY].getValue() < state.getValue()) {
      scanStates[normX][normY] = state;
    }
  }

  ScanState get(int x, int y) {
    int normX = normaliseCoordinate(x);
    int normY = normaliseCoordinate(y);
    return scanStates[normX][normY];
  }

  int normaliseCoordinate(int val) {
    while (val < 0) {
      val += size;
    }
    return val % size;
  }

  public double getLongestTravelDistance() {
    return longestTravelDistance;
  }

  public int getSize() {
    return size;
  }

  /**
   * Value of making every tile scanned that would guarantee finding all resources
   */
  int unscannedValue() {
    return Stream.of(scanStates)
            .flatMap(Stream::of)
            .mapToInt(state -> 2 - state.getValue())
            .sum();
  }

  public ScanMap clone() {
    ScanState[][] clonedStates = Arrays.stream(scanStates)
            .map(ScanState[]::clone)
            .toArray(ScanState[][]::new);
    return new ScanMap(size, clonedStates);
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
