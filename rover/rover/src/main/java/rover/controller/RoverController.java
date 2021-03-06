package rover.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import rover.RoverAttributes;
import rover.Scenario;
import rover.mediators.RoverFacade;
import rover.mediators.bus.RoverBusSubProvider;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.ScenarioInfo;
import rover.mediators.data.message.InboundMessage;
import rover.mediators.data.update.UpdateEvent;
import rover.mediators.data.update.item.ScannerItemType;
import rover.model.IntentionGenerator;
import rover.model.action.ActionController;
import rover.model.action.routine.RoutineType;
import rover.model.action.routine.RoverRoutine;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;
import rover.model.communication.CommunicationManager;
import rover.model.communication.ontology.inform.ActionBid;
import rover.model.roverinfo.RoverInfo;
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
  private BidCollector bidCollector;
  private Random rn = new Random();

  public RoverController(RoverAttributes attributes,
                         RoverFacade roverFacade,
                         RoverBusSubProvider<UpdateEvent> updateSubService,
                         RoverBusSubProvider<InboundMessage> messageSubService,
                         RoverBusSubProvider<ScenarioInfo> scenarioSubService,
                         RoverBusSubProvider<RoverStateInfo> stateSubService) {
    logger = LoggerFactory.getLogger("AGENT");
    logger.info("Creating Rover Logic");
    this.roverAttributes = attributes;
    roverFacade.setErrorReporter(this::processUpdate);
    scenario = Scenario.SCENARIO_0;
    itemManager = new ItemManager();
    scanManager = new ScanManager(scenario.getSize(), attributes.getScanRange(), MAP_SCAN_RESOLUTION);
    actionController = new ActionController(roverFacade, this::bidOnAction);
    bidCollector = new BidCollector(scenario.getRoverCount(), actionController);
    communicationManager = new CommunicationManager(roverFacade, liveRoverInfo, scanManager, itemManager, bidCollector);
    intentionGenerator = new IntentionGenerator(itemManager, scanManager, communicationManager);
    // ROVER FACADE MUST BE INITIALIZED FIRST. CAUTION!
    liveRoverInfo = new RoverInfo(attributes, scenario);
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
  }

  private void processMessage(InboundMessage inboundMessage) {
    logger.info("ROVER RECEIVED - {}", inboundMessage.toString());
    communicationManager.receiveMessage(inboundMessage.getMessage());
  }

  private void processScenarioUpdate(ScenarioInfo scenarioInfo) {
    logger.trace("ROVER RECEIVED - {}", scenarioInfo.toString());
    scenario = Scenario.getById(scenarioInfo.getScenario());
    liveRoverInfo = new RoverInfo(roverAttributes, scenario);
    communicationManager.setRoverInfo(liveRoverInfo);
    scanManager = new ScanManager(scenario.getSize(), liveRoverInfo.getAttributes().getScanRange(), MAP_SCAN_RESOLUTION);
    intentionGenerator.setScanManager(scanManager);
    communicationManager.setScanManager(scanManager);
    bidCollector.setTotalRovers(scenario.getRoverCount());
  }

  private void processRoverUpdate(RoverStateInfo stateInfo) {
    logger.trace("ROVER RECEIVED - {}", stateInfo.toString());
    liveRoverInfo.setRoverStateInfo(stateInfo);
  }

  private void bidOnAction() {
    logger.info("Finding bid for next action.");
    RoverRoutine nextBestRoutine = findNewAction();
    double nudgedValue = nextBestRoutine.getValue() - (rn.nextDouble()/10.0);
    String bidToAll = new ActionBid().generateCommand(nudgedValue);
    communicationManager.sendAll(bidToAll);
    bidCollector.acceptPersonalBid(nudgedValue, nextBestRoutine);
  }

  private RoverRoutine findNewAction() {
    long startCounter, endCounter;
    RoverRoutine roverRoutine;
    do  {
      logger.info("Finding new routine to do.");
      startCounter = communicationManager.getReceivedMessageCounter();
      roverRoutine = intentionGenerator.getNextBestRoutine(liveRoverInfo);
      randomSleep();
      endCounter = communicationManager.getReceivedMessageCounter();
      if(startCounter != endCounter) {
        logger.info("Got message since last routine. Finding Better option!");
      }
    } while (startCounter != endCounter);
    return roverRoutine;
  }

  private void randomSleep() {
    try {
      Thread.sleep(rn.nextInt(300) + 200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
