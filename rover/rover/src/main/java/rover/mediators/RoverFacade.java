package rover.mediators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import rover.Rover;
import rover.mediators.data.message.OutboundTeamMessage;
import rover.mediators.data.message.OutboundUserMessage;
import rover.mediators.data.update.UpdateEvent;
import rover.mediators.data.update.UpdateEventType;
import rover.mediators.data.update.UpdateStatus;

/**
 * Created by dominic on 25/10/16.
 */
public class RoverFacade {
  private final Rover rover;
  private final Logger log;
  private Consumer<UpdateEvent> errorReporter;

  public RoverFacade(Rover rover) {
    this.rover = rover;
    log = LoggerFactory.getLogger("AGENT");
    errorReporter = (UpdateEvent x) -> log.error("CRITICAL ERROR - FAILED TO REPORT ERROR: "
            + x.toString());
  }

  public synchronized void setErrorReporter(Consumer<UpdateEvent> errorReporter) {
    this.errorReporter = errorReporter;
  }

  public void deposit() {
    try {
      log.info("ROVER ACTION - DEPOSIT");
      rover.deposit();
    } catch (Exception e) {
      log.error("ROVER ACTION FAIL - DEPOSIT - Check payload for items.");
      errorReporter.accept(new UpdateEvent(UpdateEventType.DEPOSIT, UpdateStatus.FAILED));
    }
  }

  public void collect() {
    try {
      log.info("ROVER ACTION - COLLECT");
      rover.collect();
    } catch (Exception e) {
      log.error("ROVER ACTION FAIL - COLLECT - Check if over items");
      errorReporter.accept(new UpdateEvent(UpdateEventType.COLLECT, UpdateStatus.FAILED));
    }
  }

  public void scan(Double range) {
    try {
      log.info("ROVER ACTION - SCAN - range:{}", range);
      rover.scan(range);
    } catch (Exception e) {
      log.error("ROVER ACTION FAIL - SCAN - Check requested scan range: " + range);
      errorReporter.accept(new UpdateEvent(UpdateEventType.SCAN, UpdateStatus.FAILED));
    }
  }

  public void move(Double xOffset, Double yOffset, Double speed) {
    try {
      log.info("ROVER ACTION - MOVE - x:{}, y:{}, speed:{}", xOffset, yOffset, speed);
      rover.move(xOffset, yOffset, speed);
    } catch (Exception e) {
      log.error("ROVER ACTION FAIL - MOVE - Check requested speed: " + speed);
      errorReporter.accept(new UpdateEvent(UpdateEventType.MOVE, UpdateStatus.FAILED));
    }
  }

  public void stop() {
    try {
      log.info("ROVER ACTION - STOP");
      rover.stop();
    } catch (Exception e) {
      log.error("ROVER ACTION FAIL - STOP - Action may have completed.");
      errorReporter.accept(new UpdateEvent(UpdateEventType.WORLD_STOPPED, UpdateStatus.FAILED));
    }
  }

  public void sendMessage(OutboundTeamMessage outboundTeamMessage) {
    log.info("ROVER ACTION - SEND MESSAGE TEAM - {}", outboundTeamMessage.toString());
    rover.broadCastToTeam(outboundTeamMessage.getMessage());
  }

  public void sendMessage(OutboundUserMessage outboundUserMessage) {
    log.info("ROVER ACTION - SEND MESSAGE USER - to:{}, message:{}", outboundUserMessage.getTargetUserId(), outboundUserMessage.toString());
    rover.broadCastToUnit(outboundUserMessage.getTargetUserId(), outboundUserMessage.getMessage());
  }
}
