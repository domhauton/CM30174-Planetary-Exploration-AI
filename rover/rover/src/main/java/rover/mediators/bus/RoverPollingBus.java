package rover.mediators.bus;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by dominic on 27/10/16.
 *
 * Polls the result of a function, converts the value and sends to all subscribers.
 */
class RoverPollingBus<A, B> extends RoverBus<A, B> {
  private Supplier<Collection<A>> supplier;

  RoverPollingBus(Function<A, B> converter, Duration pollingRate, Supplier<Collection<A>> supplier) {
    super(converter);
    this.supplier = supplier;
    Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(() -> supplier.get().forEach(this::push), 0,
                    pollingRate.toMillis(), TimeUnit.MILLISECONDS);
  }

  @Override
  public Collection<B> getCurrent() {
    Collection<A> newValues = supplier.get();
    newValues.forEach(this::push);
    return newValues.stream().map(converter).collect(Collectors.toList());
  }
}
