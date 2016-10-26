package rover.mediators.update;

import rover.PollResult;

/**
 * Created by dominic on 26/10/16.
 *
 * Enum for possible status events
 */
public enum UpdateStatus {
  COMPLETE,
  CANCELLED,
  FAILED;

  public static UpdateStatus fromInt(int i) {
    switch (i) {
      case PollResult.COMPLETE:
        return COMPLETE;
      case PollResult.CANCELLED:
        return CANCELLED;
      default:
        return FAILED;
    }
  }
}
