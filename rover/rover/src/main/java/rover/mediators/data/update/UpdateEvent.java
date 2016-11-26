package rover.mediators.data.update;

import java.util.Collections;
import java.util.List;

import rover.mediators.data.update.item.ScannerItem;

/**
 * Created by dominic on 26/10/16.
 *
 * Holds an update event from the main bot.
 */
public class UpdateEvent {
  private final UpdateEventType updateEventType;
  private final UpdateStatus updateStatus;
  private final List<ScannerItem> scannerItems;

  public UpdateEvent(UpdateEventType updateEventType,
                     UpdateStatus updateStatus) {
    this(updateEventType, updateStatus, Collections.emptyList());
  }

  public UpdateEvent(UpdateEventType updateEventType,
                     UpdateStatus updateStatus,
                     List<ScannerItem> scannerItems) {
    this.updateEventType = updateEventType;
    this.updateStatus = updateStatus;
    this.scannerItems = scannerItems;
  }

  public UpdateEventType getUpdateEventType() {
    return updateEventType;
  }

  public UpdateStatus getUpdateStatus() {
    return updateStatus;
  }

  public List<ScannerItem> getScannerItems() {
    return scannerItems;
  }

  @Override
  public String toString() {
    return "UpdateEvent{" +
            "updateEventType=" + updateEventType +
            ", updateStatus=" + updateStatus +
            ", scannerItems=" + scannerItems +
            '}';
  }
}
