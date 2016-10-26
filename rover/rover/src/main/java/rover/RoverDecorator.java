package rover;

import com.google.common.collect.ImmutableList;

import org.iids.aos.api.AgentCloseRule;

import java.util.Optional;

import rover.mediators.RoverActionService;
import rover.mediators.RoverMessageService;
import rover.mediators.RoverUpdateBus;
import rover.mediators.message.InboundMessage;
import util.ImmutableListCollector;


/**
 * Created by dominic on 23/10/16.
 */
public class RoverDecorator extends Rover {

  private static final int TOTAL_ATTRIBUTE_POINTS = 9;
  private static final String DEFAULT_TEAM_NAME = "dh499";

  private final RoverUpdateBus roverUpdateBus;

  @Override
  void begin() {

  }

  @Override
  void end() {

  }

  @Override
  void poll(PollResult pr) {
    roverUpdateBus.push(pr);
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

    RoverActionService roverActionService = new RoverActionService(this);
    RoverMessageService roverMessageService = new RoverMessageService(this);
    RoverScenarioInfo roverScenarioInfo = getWorldInfo();
    roverUpdateBus = new RoverUpdateBus();
  }



  public synchronized ImmutableList<InboundMessage> getMessages() {
    super.retrieveMessages();
    ImmutableList<InboundMessage> messagesList = messages
            .stream()
            .map(InboundMessage::new)
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
