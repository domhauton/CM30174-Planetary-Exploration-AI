package rover.mediators.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by dominic on 27/10/16.
 *
 * Pub-sub bus that stores messages in a buffer if there are no subscribers present so no message
 * are lost
 */
public class RoverBus<A, B> {
  private final CopyOnWriteArrayList<Consumer<B>> subscribers;
  private final Queue<B> startBuffer;
  final Function<A, B> converter;
  private final Logger log;

  RoverBus(Function<A, B> converter) {
    subscribers = new CopyOnWriteArrayList<>();
    startBuffer = new ConcurrentLinkedQueue<>();
    this.converter = converter;
    this.log = LoggerFactory.getLogger("AGENT");
  }

  private synchronized void subscribe(Consumer<B> consumer) {
    log.info("New Subscriber!", startBuffer.size());
    if (subscribers.isEmpty()) {
      log.info("Pushing {} messages out to new sub", startBuffer.size());
      startBuffer.forEach(consumer);
      startBuffer.clear();
    }
    subscribers.add(consumer);
  }

  public RoverBusSubProvider<B> getSubProvider() {
    return new RoverBusSubProvider<>(this::subscribe, this::getCurrent);
  }

  public Collection<B> getCurrent() {
    return Collections.emptyList();
  }

  public void push(A item) {
    B convertedItem = converter.apply(item);
    updateSubs(convertedItem);
  }

  void updateSubs(B item) {
    //log.info("Sent from interconnect: {}", item.toString());
    if (subscribers.isEmpty()) {
      startBuffer.add(item);
      log.info("Stored for later: {}. Current subs: {}", item.toString(), subscribers.size());
    } else {
      subscribers.forEach(sub -> sub.accept(item));
    }
  }
}
