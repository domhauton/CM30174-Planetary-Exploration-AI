package rover.mediators.bus;


import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import rover.PollResult;
import rover.ScanItem;
import rover.mediators.data.message.InboundMessage;
import rover.mediators.data.update.UpdateEvent;
import rover.mediators.data.update.UpdateEventType;
import rover.mediators.data.update.UpdateStatus;
import rover.mediators.data.update.item.RelativeCoordinates;
import rover.mediators.data.update.item.ResourceType;
import rover.mediators.data.update.item.ScannerItem;
import rover.mediators.data.update.item.ScannerItemType;

/**
 * Created by dominic on 27/10/16.
 *
 * Converter utils for Rover Messages
 */
abstract class RoverBusBrokerUtils {

  static InboundMessage stringToMessage(String message) {
    LoggerFactory.getLogger("AGENT").info("PROCESSING MESSAGE.");
    return new InboundMessage(message);
  }

  static UpdateEvent pollResultToUpdate(PollResult pollResult) {
    final UpdateEventType updateEventType = UpdateEventType.fromInt(pollResult.getResultType());
    final UpdateStatus updateStatus = UpdateStatus.fromInt(pollResult.getResultStatus());
    final ScanItem[] scanItems = pollResult.getScanItems();
    if(scanItems == null) {
      return new UpdateEvent(updateEventType, updateStatus, Collections.emptyList());
    } else {
      final List<ScannerItem> scannerItems = Stream.of(pollResult.getScanItems())
              .map(RoverBusBrokerUtils::itemConverter)
              .collect(Collectors.toList());
      return new UpdateEvent(updateEventType, updateStatus, scannerItems);
    }
  }

  private static ScannerItem itemConverter(ScanItem scanItem) {
    RelativeCoordinates relativeCoordinates =
            new RelativeCoordinates(scanItem.getxOffset(), scanItem.getyOffset());
    ScannerItemType scannerItemType = ScannerItemType.fromInt(scanItem.getItemType());
    ResourceType resourceType = ResourceType.getById(scanItem.getResourceType());
    return new ScannerItem(scannerItemType, relativeCoordinates, resourceType);
  }
}
