package rover.model.scanning;

import util.PrintUtil;

/**
 * Created by dominic on 25/11/16.
 */
enum ScanState {
  SCANNED(PrintUtil.ANSI_GREEN + "S" + PrintUtil.ANSI_RESET, 2, 100),
  PARTIAL(PrintUtil.ANSI_YELLOW + "P" + PrintUtil.ANSI_RESET, 1, -600),
  UNKNOWN(PrintUtil.ANSI_RED + "U" + PrintUtil.ANSI_RESET, 0, 0);

  private final String tile;
  private final int value;
  private final int desirable;

  ScanState(String tile, int value, int desirable) {
    this.tile = tile;
    this.value = value;
    this.desirable = desirable;
  }

  public int getValue() {
    return value;
  }

  public int getDesirable() {
    return desirable;
  }

  @Override
  public String toString() {
    return tile;
  }
}
