package rover.mediators.bus;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dominic on 22/11/16.
 *
 * Polls the return value of a function, converts the result and sends the converted
 * result to notifier when the value changes.
 */
class RoverMonitoringBus<A, B> extends RoverPollingBus<A, B> {
  private B previousValue;

  RoverMonitoringBus(Function<A, B> converter,
                     Duration pollingRate,
                     Supplier<Collection<A>> supplier) {
    super(converter, pollingRate, supplier);
  }

  @Override
  public synchronized void push(A item) {
    B convertedItem = super.converter.apply(item);
    if (previousValue == null || !previousValue.equals(convertedItem)) {
      previousValue = convertedItem;
      updateSubs(convertedItem);
    }
  }
}
