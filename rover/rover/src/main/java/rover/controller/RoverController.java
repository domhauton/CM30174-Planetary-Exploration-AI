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
import rover.model.action.routine.RoverRoutine;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;
import rover.model.communication.CommunicationManager;
import rover.model.roverinfo.RoverInfo;
import rover.model.action.ActionController;
import rover.model.scanning.ScanManager;

/**
 * Created by dominic on 26/10/16.
 *
 * Main controller for the rover
 */
public class RoverController {
  private final int MAP_SCAN_RESOLUTION = 2;

  private final RoverAttributes roverAttributes;
  private final ActionController actionController;
  private final IntentionGenerator intentionGenerator;
  private final ItemManager itemManager;
  private final CommunicationManager communicationManager;
  private final Logger logger;

  private Scenario scenario;
  private RoverInfo liveRoverInfo;
  private ScanManager scanManager;

  public RoverController(RoverAttributes attributes,
                         RoverFacade roverFacade,
                         RoverBusSubProvider<UpdateEvent> updateSubService,
                         RoverBusSubProvider<InboundMessage> messageSubService,
                         RoverBusSubProvider<ScenarioInfo> scenarioSubService,
                         RoverBusSubProvider<RoverStateInfo> stateSubService) {
    logger = LoggerFactory.getLogger("AGENT");
    logger.info("Creating Rover Logic");
    this.roverAttributes = attributes;
    scenario = Scenario.SCENARIO_0;
    itemManager = new ItemManager();
    scanManager = new ScanManager(scenario.getSize(), attributes.getScanRange(), MAP_SCAN_RESOLUTION);
    intentionGenerator = new IntentionGenerator(itemManager, scanManager);
    roverFacade.setErrorReporter(this::processUpdate);
    actionController = new ActionController(roverFacade);
    // ROVER FACADE MUST BE INITIALIZED FIRST. CAUTION!
    liveRoverInfo = new RoverInfo(attributes, scenario);
    communicationManager = new CommunicationManager(roverFacade, liveRoverInfo, scanManager, itemManager);
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
            .map(scannerItem -> new Resource(scannerItem.getResourceType(), liveRoverInfo.getPosition().moveCoordinate(scannerItem.getRelativeCoordinates().getX(), scannerItem.getRelativeCoordinates().getY(), liveRoverInfo.getScenarioInfo().getSize()), scenario.getResourcePileSize()))
            .filter(resource -> itemManager.foundResource(resource, scenario.getResourcePileSize()))
            .forEach(communicationManager::informOthersAboutNewResource);
    actionController.response(updateEvent);
    if(actionController.actionsRemainingInRoutine() == 0) {
      RoverRoutine roverRoutine = intentionGenerator.getNextBestRoutine(liveRoverInfo);
      logger.info("Setting new routine to execute");
      actionController.setRoutineToExecute(roverRoutine);
    }
    actionController.executeAction();
  }

  private void processMessage(InboundMessage inboundMessage) {
    logger.info("ROVER RECEIVED - {}", inboundMessage.toString());
    communicationManager.receiveMessage(inboundMessage.getMessage());
  }

  private void processScenarioUpdate(ScenarioInfo scenarioInfo) {
    logger.trace("ROVER RECEIVED - {}", scenarioInfo.toString());
    scenario = Scenario.getById(scenarioInfo.getScenario());
    liveRoverInfo = new RoverInfo(roverAttributes, scenario);
    scanManager = new ScanManager(scenario.getSize(), liveRoverInfo.getAttributes().getScanRange(), MAP_SCAN_RESOLUTION);
    intentionGenerator.setScanManager(scanManager);
    communicationManager.setScanManager(scanManager);
  }

  private void processRoverUpdate(RoverStateInfo stateInfo) {
    logger.trace("ROVER RECEIVED - {}", stateInfo.toString());
    liveRoverInfo.setRoverStateInfo(stateInfo);
  }

  public void end() {
    logger.info("Shutting down agent Controller");
  }

}
