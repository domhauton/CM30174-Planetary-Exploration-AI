package rover.mediators;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

import rover.PollResult;
import rover.ScanItem;
import rover.mediators.update.UpdateEvent;
import rover.mediators.update.UpdateEventType;
import rover.mediators.update.UpdateStatus;
import rover.mediators.update.item.RelativeCoordinates;
import rover.mediators.update.item.ScannerItem;
import rover.mediators.update.item.ScannerItemType;
import util.ImmutableListCollector;

/**
 * Created by dominic on 26/10/16.
 *
 * Provides updates on events from the Rover
 */
public class RoverUpdateBus {
  private final Collection<Consumer<UpdateEvent>> subscribers;

  public RoverUpdateBus() {
    subscribers = new CopyOnWriteArrayList<>();
  }

  public void push(PollResult pollResult) {
    final UpdateEvent updateEvent = updateConverter(pollResult);
    subscribers.forEach(sub -> sub.accept(updateEvent));
  }

  public synchronized void subscribe(Consumer<UpdateEvent> subscriber) {
    subscribers.add(subscriber);
  }

  private UpdateEvent updateConverter(PollResult pollResult) {
    final UpdateEventType updateEventType = UpdateEventType.fromInt(pollResult.getResultType());
    final UpdateStatus updateStatus = UpdateStatus.fromInt(pollResult.getResultStatus());
    final ImmutableList<ScannerItem> scannerItems = Stream.of(pollResult.getScanItems())
            .map(this::itemConverter)
            .collect(new ImmutableListCollector<>());
    return new UpdateEvent(updateEventType, updateStatus, scannerItems);
  }

  private ScannerItem itemConverter(ScanItem scanItem) {
    RelativeCoordinates relativeCoordinates =
            new RelativeCoordinates(scanItem.getxOffset(), scanItem.getyOffset());
    ScannerItemType scannerItemType = ScannerItemType.fromInt(scanItem.getItemType());
    return new ScannerItem(scannerItemType, relativeCoordinates);
  }
}
