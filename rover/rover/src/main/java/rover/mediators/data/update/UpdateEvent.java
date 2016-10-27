package rover.mediators.data.update;

import com.google.common.collect.ImmutableList;

import rover.mediators.data.update.item.ScannerItem;

/**
 * Created by dominic on 26/10/16.
 *
 * Holds an update event from the main bot.
 */
public class UpdateEvent {
  private final UpdateEventType updateEventType;
  private final UpdateStatus updateStatus;
  private final ImmutableList<ScannerItem> scannerItems;

  public UpdateEvent(UpdateEventType updateEventType,
                     UpdateStatus updateStatus,
                     ImmutableList<ScannerItem> scannerItems) {
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

  public ImmutableList<ScannerItem> getScannerItems() {
    return scannerItems;
  }
}
