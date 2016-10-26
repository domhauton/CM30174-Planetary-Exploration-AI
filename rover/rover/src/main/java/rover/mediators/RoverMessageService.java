package rover.mediators;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import rover.mediators.message.InboundMessage;
import rover.mediators.message.OutboundTeamMessage;
import rover.mediators.message.OutboundUserMessage;
import rover.RoverDecorator;

/**
 * Created by dominic on 25/10/16.
 *
 * Controls message sending and retrieval for bot.
 */
public class RoverMessageService {

  private static final Duration TICK_RATE_MILLIS = Duration.ofMillis(100);

  private final RoverDecorator rover;
  private Collection<Consumer<InboundMessage>> subscribers;

  public RoverMessageService(RoverDecorator rover) {
    this.rover = rover;
    subscribers = new CopyOnWriteArrayList<>();
    Executors
            .newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(this::updateSubscribers, 0,
                    TICK_RATE_MILLIS.toMillis(), TimeUnit.MILLISECONDS);
  }

  public synchronized void subscribe(Consumer<InboundMessage> messageConsumer) {
    subscribers.add(messageConsumer);
  }

  public void sendMessage(OutboundTeamMessage outboundTeamMessage) {
    rover.broadCastToTeam(outboundTeamMessage.getMessage());
  }

  public void sendMessage(OutboundUserMessage outboundUserMessage) {
    rover.broadCastToUnit(outboundUserMessage.getTargetUserId(), outboundUserMessage.getMessage());
  }

  /**
   * Request a subscriber update. Will block until update is processed.
   */
  public synchronized void updateSubscribers() {
    rover.getMessages().forEach(this::sendMessageToSubscribers);
  }

  private void sendMessageToSubscribers(InboundMessage inboundMessage) {
    subscribers.forEach(sub -> sub.accept(inboundMessage));
  }
}
