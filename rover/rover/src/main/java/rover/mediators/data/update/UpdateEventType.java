package rover.mediators.data.update;

import rover.PollResult;

/**
 * Created by dominic on 26/10/16.
 *
 * An enum for UpdateEventType Types.
 */
public enum UpdateEventType {
  MOVE,
  SCAN,
  COLLECT,
  DEPOSIT,
  WORLD_STARTED,
  WORLD_STOPPED,
  ROVER_UNKNOWN;

  public static UpdateEventType fromInt(int i) {
    switch (i) {
      case PollResult.MOVE:
        return MOVE;
      case PollResult.SCAN:
        return SCAN;
      case PollResult.COLLECT:
        return COLLECT;
      case PollResult.DEPOSIT:
        return DEPOSIT;
      case PollResult.WORLD_STARTED:
        return WORLD_STARTED;
      case PollResult.WORLD_STOPPED:
        return WORLD_STOPPED;
      default:
        return ROVER_UNKNOWN;
    }
  }
}
