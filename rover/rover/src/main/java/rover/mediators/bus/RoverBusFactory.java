package rover.mediators.bus;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

import rover.PollResult;
import rover.mediators.data.RoverScenarioInfo;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.message.InboundMessage;
import rover.mediators.data.update.UpdateEvent;

/**
 * Created by dominic on 27/10/16.
 */
public abstract class RoverBusFactory {

  public static RoverBusSubProvider<InboundMessage> getRoverMessageService(
          Supplier<Collection<String>> messageSupplier) {
    return new RoverPollingBus<>(
            RoverBusBrokerUtils::stringToMessage,
            Duration.ofMillis(100),
            messageSupplier)
            .getSubProvider();
  }

  public static RoverBus<PollResult, UpdateEvent> getRoverUpdateService() {
    return new RoverBus<>(RoverBusBrokerUtils::pollResultToUpdate);
  }

  public static RoverBusSubProvider<RoverScenarioInfo> getRoverScenarioService(
          Supplier<RoverScenarioInfo> supplier) {
    return new RoverPollingBus<>(
            (RoverScenarioInfo item) -> item,
            Duration.ofMillis(100),
            () -> Collections.singleton(supplier.get()))
            .getSubProvider();
  }

  public static RoverBusSubProvider<RoverStateInfo> getRoverStateService(
          Supplier<RoverStateInfo> supplier) {
    return new RoverPollingBus<>(
            (RoverStateInfo item) -> item,
            Duration.ofMillis(100),
            () -> Collections.singleton(supplier.get()))
            .getSubProvider();
  }
}
