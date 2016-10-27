package rover.mediators.bus;

import com.google.common.collect.ImmutableList;

import java.util.stream.Stream;

import rover.PollResult;
import rover.ScanItem;
import rover.mediators.data.message.InboundMessage;
import rover.mediators.data.update.UpdateEvent;
import rover.mediators.data.update.UpdateEventType;
import rover.mediators.data.update.UpdateStatus;
import rover.mediators.data.update.item.RelativeCoordinates;
import rover.mediators.data.update.item.ScannerItem;
import rover.mediators.data.update.item.ScannerItemType;
import util.ImmutableListCollector;

/**
 * Created by dominic on 27/10/16.
 *
 * Converter utils for Rover Messages
 */
abstract class RoverBusBrokerUtils {

  static InboundMessage stringToMessage(String message) {
    return new InboundMessage(message);
  }

  static UpdateEvent pollResultToUpdate(PollResult pollResult) {
    final UpdateEventType updateEventType = UpdateEventType.fromInt(pollResult.getResultType());
    final UpdateStatus updateStatus = UpdateStatus.fromInt(pollResult.getResultStatus());
    final ImmutableList<ScannerItem> scannerItems = Stream.of(pollResult.getScanItems())
            .map(RoverBusBrokerUtils::itemConverter)
            .collect(new ImmutableListCollector<>());
    return new UpdateEvent(updateEventType, updateStatus, scannerItems);
  }

  private static ScannerItem itemConverter(ScanItem scanItem) {
    RelativeCoordinates relativeCoordinates =
            new RelativeCoordinates(scanItem.getxOffset(), scanItem.getyOffset());
    ScannerItemType scannerItemType = ScannerItemType.fromInt(scanItem.getItemType());
    return new ScannerItem(scannerItemType, relativeCoordinates);
  }
}
