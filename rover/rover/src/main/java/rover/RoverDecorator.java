package rover;

import com.google.common.collect.ImmutableList;

import org.iids.aos.api.AgentCloseRule;

import java.util.Optional;
import java.util.function.Consumer;

import rover.controller.RoverController;
import rover.mediators.RoverFacade;
import rover.mediators.bus.RoverBus;
import rover.mediators.bus.RoverBusSubProvider;
import rover.mediators.data.RoverScenarioInfo;
import rover.mediators.bus.RoverBusFactory;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.update.UpdateEvent;
import util.ImmutableListCollector;


/**
 * Created by dominic on 23/10/16.
 */
public class RoverDecorator extends Rover {
  private static final String DEFAULT_TEAM_NAME = "dh499";
  private final Consumer<PollResult> pollResultConsumer;

  @Override
  void begin() {
    getLog().info("Bot has been started.");
  }

  @Override
  void end() {
    getLog().info("Bot has been ended.");
  }

  @Override
  void poll(PollResult pr) {
    pollResultConsumer.accept(pr);
  }

  public RoverDecorator() {
    this(RoverAttributes.DEFAULT);
  }

  private RoverDecorator(RoverAttributes roverAttributes) {
    this(DEFAULT_TEAM_NAME, roverAttributes);
  }

  private RoverDecorator(String teamName, RoverAttributes roverAttributes)
          throws IllegalArgumentException {
    super();
    try {
      setAttributes(
              roverAttributes.getSpeed(),
              roverAttributes.getScan(),
              roverAttributes.getRange());
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to set attributes. Cannot start bot. "
              + e.getMessage());
    }
    setTeam(teamName);
    setCloseRule(AgentCloseRule.KeepRunning);

    // Service Setup

    RoverFacade roverFacade = new RoverFacade(this);
    RoverBus<PollResult, UpdateEvent> roverUpdateBus
            = RoverBusFactory.getRoverUpdateService();
    pollResultConsumer = roverUpdateBus::push;
    RoverBusSubProvider<UpdateEvent> updateSubService
            = roverUpdateBus.getSubProvider();

    RoverController roverController = new RoverController(roverFacade,
            updateSubService,
            RoverBusFactory.getRoverMessageService(this::fetchNewMessages),
            RoverBusFactory.getRoverScenarioService(this::getWorldInfo),
            RoverBusFactory.getRoverStateService(this::getRoverInfo),
            getLog());
  }

  private synchronized ImmutableList<String> fetchNewMessages() {
    super.retrieveMessages();
    ImmutableList<String> messagesList = messages
            .stream()
            .collect(new ImmutableListCollector<>());
    messages.clear();
    return messagesList;
  }

  private RoverScenarioInfo getWorldInfo() {
    return new RoverScenarioInfo(
            getWorldHeight(),
            getWorldWidth(),
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
