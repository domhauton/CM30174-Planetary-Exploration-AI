package rover.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rover.RoverAttributes;
import rover.mediators.RoverFacade;
import rover.mediators.bus.RoverBusSubProvider;
import rover.mediators.data.ScenarioInfo;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.message.InboundMessage;
import rover.mediators.data.update.UpdateEvent;
import rover.model.IntentionGenerator;
import rover.model.RoverInfo;
import rover.model.action.ActionController;
import rover.model.action.primitives.RoverAction;

/**
 * Created by dominic on 26/10/16.
 *
 * Main controller for the rover
 */
public class RoverController {
  private final RoverInfo roverInfo;
  private final ActionController actionController;
  private final IntentionGenerator intentionGenerator;
  private final Logger logger;
  private boolean first = true;

  public RoverController(RoverAttributes attributes,
                         RoverFacade roverFacade,
                         RoverBusSubProvider<UpdateEvent> updateSubService,
                         RoverBusSubProvider<InboundMessage> messageSubService,
                         RoverBusSubProvider<ScenarioInfo> scenarioSubService,
                         RoverBusSubProvider<RoverStateInfo> stateSubService) {
    logger = LoggerFactory.getLogger("AGENT");
    intentionGenerator = new IntentionGenerator();
    roverFacade.setErrorReporter(this::processUpdate);
    actionController = new ActionController(roverFacade);
    // ROVER FACADE MUST BE INITIALIZED FIRST. CAUTION!
    RoverStateInfo currentRoverStateInfo = stateSubService
            .fetch()
            .stream()
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unable to fetch initial rover state"));
    ScenarioInfo currentScenarioInfo = scenarioSubService
            .fetch()
            .stream()
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unable to fetch initial scenario"));
    roverInfo = new RoverInfo(attributes, currentRoverStateInfo, currentScenarioInfo);
    updateSubService.subscribe(this::processUpdate);
    messageSubService.subscribe(this::processMessage);
    scenarioSubService.subscribe(this::processScenarioUpdate);
    stateSubService.subscribe(this::processRoverUpdate);
    logger.info("Initiated Rover");
    // DO NOT CALL FACADE FROM CONSTRUCTOR!
  }

  private void processUpdate(UpdateEvent updateEvent) {
    logger.info("ROVER RECEIVED - {}", updateEvent.toString());
    actionController.response(updateEvent);
    RoverAction action = intentionGenerator.getNextBestAction(roverInfo);
    actionController.executeAction(action);
  }

  private void processMessage(InboundMessage inboundMessage) {
    logger.info("ROVER RECEIVED - {}", inboundMessage.toString());
    //TODO Implement Message parsing
  }

  private void processScenarioUpdate(ScenarioInfo scenarioInfo) {
    logger.info("ROVER RECEIVED - {}", scenarioInfo.toString());
    roverInfo.setScenarioInfo(scenarioInfo);
  }

  private void processRoverUpdate(RoverStateInfo stateInfo) {
    logger.info("ROVER RECEIVED - {}", stateInfo.toString());
    roverInfo.setRoverStateInfo(stateInfo);
  }

  public void end() {
    logger.info("Shutting down agent Controller");
  }

}
