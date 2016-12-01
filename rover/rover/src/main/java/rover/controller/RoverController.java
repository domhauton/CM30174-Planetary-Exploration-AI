package rover.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rover.RoverAttributes;
import rover.Scenario;
import rover.mediators.RoverFacade;
import rover.mediators.bus.RoverBusSubProvider;
import rover.mediators.data.ScenarioInfo;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.message.InboundMessage;
import rover.mediators.data.update.UpdateEvent;
import rover.mediators.data.update.item.ScannerItemType;
import rover.model.IntentionGenerator;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;
import rover.model.roverinfo.RoverInfo;
import rover.model.action.ActionController;
import rover.model.action.primitives.RoverAction;

/**
 * Created by dominic on 26/10/16.
 *
 * Main controller for the rover
 */
public class RoverController {
  private final RoverAttributes roverAttributes;
  private final ActionController actionController;
  private final IntentionGenerator intentionGenerator;
  private final ItemManager itemManager;
  private final Logger logger;

  private RoverInfo liveRoverInfo;

  public RoverController(RoverAttributes attributes,
                         RoverFacade roverFacade,
                         RoverBusSubProvider<UpdateEvent> updateSubService,
                         RoverBusSubProvider<InboundMessage> messageSubService,
                         RoverBusSubProvider<ScenarioInfo> scenarioSubService,
                         RoverBusSubProvider<RoverStateInfo> stateSubService) {
    logger = LoggerFactory.getLogger("AGENT");
    logger.info("Creating Rover Logic");
    this.roverAttributes = attributes;
    itemManager = new ItemManager();
    intentionGenerator = new IntentionGenerator(itemManager);
    roverFacade.setErrorReporter(this::processUpdate);
    actionController = new ActionController(roverFacade);
    // ROVER FACADE MUST BE INITIALIZED FIRST. CAUTION!
    liveRoverInfo = new RoverInfo(attributes, Scenario.SCENARIO_0);
    updateSubService.subscribe(this::processUpdate);
    messageSubService.subscribe(this::processMessage);
    scenarioSubService.subscribe(this::processScenarioUpdate);
    stateSubService.subscribe(this::processRoverUpdate);
    logger.info("Initiated Rover");
    // DO NOT CALL FACADE FROM CONSTRUCTOR!
  }

  private void processUpdate(UpdateEvent updateEvent) {
    logger.info("ROVER RECEIVED - {}", updateEvent.toString());
    updateEvent.getScannerItems().stream()
            .filter(scannerItem -> scannerItem.getScannerItemType() == ScannerItemType.RESOURCE)
            .map(scannerItem -> new Resource(scannerItem.getResourceType(), liveRoverInfo.getPosition().moveCoordinate(scannerItem.getRelativeCoordinates().getX(), scannerItem.getRelativeCoordinates().getY(), liveRoverInfo.getScenarioInfo().getSize())))
            .forEach(resource -> itemManager.foundResource(resource, Scenario.getById(liveRoverInfo.getScenarioInfo().getScenario()).getResourcePileSize()));
    actionController.response(updateEvent);
    RoverAction action = intentionGenerator.getNextBestAction(liveRoverInfo);
    actionController.executeAction(action);
  }

  private void processMessage(InboundMessage inboundMessage) {
    logger.info("ROVER RECEIVED - {}", inboundMessage.toString());
    //TODO Implement Message parsing
  }

  private void processScenarioUpdate(ScenarioInfo scenarioInfo) {
    logger.trace("ROVER RECEIVED - {}", scenarioInfo.toString());
    liveRoverInfo = new RoverInfo(roverAttributes, Scenario.getById(scenarioInfo.getScenario()));
  }

  private void processRoverUpdate(RoverStateInfo stateInfo) {
    logger.trace("ROVER RECEIVED - {}", stateInfo.toString());
    liveRoverInfo.setRoverStateInfo(stateInfo);
  }

  public void end() {
    logger.info("Shutting down agent Controller");
  }

}
