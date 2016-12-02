package rover;

import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import rover.controller.RoverController;
import rover.mediators.RoverFacade;
import rover.mediators.bus.RoverBus;
import rover.mediators.bus.RoverBusFactory;
import rover.mediators.bus.RoverBusSubProvider;
import rover.mediators.data.ScenarioInfo;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.update.UpdateEvent;


/**
 * Created by dominic on 23/10/16.
 */
@SuppressWarnings("unused")
public class RoverDecorator extends Rover {
  private static final String DEFAULT_TEAM_NAME = "dh499";
  private Consumer<PollResult> pollResultConsumer;
  private RoverAttributes roverAttributes;
  private RoverController roverController;
  private org.slf4j.Logger log;

  @SuppressWarnings("unused")
  public RoverDecorator() {
    this(RoverAttributes.DEFAULT);
  }

  protected RoverDecorator(RoverAttributes roverAttributes) {
    this(DEFAULT_TEAM_NAME, roverAttributes);
  }

  private RoverDecorator(String teamName, RoverAttributes roverAttributes)
          throws IllegalArgumentException {
    super();
    this.roverAttributes = roverAttributes;
    log = LoggerFactory.getLogger("AGENT");
    setTeam(teamName);
    try {
      setAttributes(
              roverAttributes.getMaxSpeed(),
              roverAttributes.getScanRange(),
              roverAttributes.getMaxLoad(),
              roverAttributes.getCargoType().getId());
    } catch (Exception e) {
      log.error("Failed to set attributes. Cannot start bot.");
      throw new IllegalArgumentException("Failed to set attributes. Cannot start bot. "
              + e.getMessage());
    }
  }

  @Override
  void begin() {
    log.info("Bot has been started.");
    try {
      move(0, 0, 1);
    } catch (Exception e) {
      log.error(e.toString());
    }
    log.info("Dummy step to trigger poll.");
    // Service Setup
    RoverFacade roverFacade = new RoverFacade(this);
    RoverBus<PollResult, UpdateEvent> roverUpdateBus
            = RoverBusFactory.getRoverUpdateService();
    pollResultConsumer = roverUpdateBus::push;
    RoverBusSubProvider<UpdateEvent> updateSubService
            = roverUpdateBus.getSubProvider();

    roverController = new RoverController(
            roverAttributes,
            roverFacade,
            updateSubService,
            RoverBusFactory.getRoverMessageService(this::fetchNewMessages),
            RoverBusFactory.getRoverScenarioService(this::getWorldInfo),
            RoverBusFactory.getRoverStateService(this::getRoverInfo));

  }

  @Override
  void end() {
    log.info("Bot has been ended.");
  }

  @Override
  void poll(PollResult pr) {
    pollResultConsumer.accept(pr);
  }

  private synchronized List<String> fetchNewMessages() {
    super.retrieveMessages();
    List<String> messagesList = messages
            .stream()
            .collect(Collectors.toList());
    messages.clear();
    if(!messagesList.isEmpty()) {
      getLog().info("Pushing {} new messages!", messagesList.size());
    }
    return messagesList;
  }

  private ScenarioInfo getWorldInfo() {
    return new ScenarioInfo(
            getWorldHeight(),
            isWorldCompetitive(),
            getWorldResources(),
            getScenario());
  }

  private RoverStateInfo getRoverInfo() {
    int load = getCurrentLoad();
    double energy = getEnergy();
    Optional<Integer> optionalLoad = load == -1 ? Optional.empty() : Optional.of(load);
    Optional<Double> optionalEnergy = energy == -1 ? Optional.empty() : Optional.of(energy);
    return new RoverStateInfo(
            optionalLoad,
            optionalEnergy,
            IsStarted(),
            getTeam(),
            getID());
  }
}
