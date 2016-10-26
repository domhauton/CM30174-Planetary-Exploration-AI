package rover.mediators.update.item;

import rover.PollResult;

/**
 * Created by dominic on 26/10/16.
 */
public enum ScannerItemType {
  RESOURCE,
  ROVER,
  BASE,
  UNKNOWN;

  public static ScannerItemType fromInt(int i) {
    switch (i) {
      case PollResult.RESOURCE:
        return RESOURCE;
      case PollResult.ROVER:
        return ROVER;
      case PollResult.BASE:
        return BASE;
      default:
        return UNKNOWN;
    }
  }
}
