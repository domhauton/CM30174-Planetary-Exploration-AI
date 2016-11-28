package rover.mediators.bus;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by dominic on 27/10/16.
 */
public class RoverBusSubProvider<T> {

  private final Consumer<Consumer<T>> subscriptionAcceptor;
  private final Supplier<Collection<T>> currentSupplier;

  RoverBusSubProvider(Consumer<Consumer<T>> subscriptionAcceptor, Supplier<Collection<T>> currentSupplier) {
    this.subscriptionAcceptor = subscriptionAcceptor;
    this.currentSupplier = currentSupplier;
  }

  public synchronized void subscribe(Consumer<T> subscriber) {
      subscriptionAcceptor.accept(subscriber);
  }

  public synchronized Collection<T> fetch() {
    return currentSupplier.get();
  }
}
