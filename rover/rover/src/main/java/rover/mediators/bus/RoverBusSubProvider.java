package rover.mediators.bus;

import java.util.function.Consumer;

/**
 * Created by dominic on 27/10/16.
 */
public class RoverBusSubProvider<T> {

  private final Consumer<Consumer<T>> subscriptionAcceptor;

  RoverBusSubProvider(Consumer<Consumer<T>> subscriptionAcceptor) {
    this.subscriptionAcceptor = subscriptionAcceptor;
  }

  public synchronized void subscribe(Consumer<T> subscriber) {
      subscriptionAcceptor.accept(subscriber);
    }
}
